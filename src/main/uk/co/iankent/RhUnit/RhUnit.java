package uk.co.iankent.RhUnit;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mozilla.javascript.*;
import org.mozilla.javascript.Context;
import uk.co.iankent.RhUnit.QUnit.QUnit;
import uk.co.iankent.RhUnit.QUnit.QUnitFactory;
import uk.co.iankent.RhUnit.QUnit.QUnitFactoryImpl;
import uk.co.iankent.RhUnit.Rhino.RhinoEnvironment;
import uk.co.iankent.RhUnit.Rhino.RhinoEnvironmentFactory;
import uk.co.iankent.RhUnit.Rhino.RhinoEnvironmentFactoryImpl;
import uk.co.iankent.RhUnit.assertors.AbstractAssertor;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;
import uk.co.iankent.RhUnit.assertors.Assertor;
import uk.co.iankent.RhUnit.assertors._deepEqual.deepEqualAssertor;
import uk.co.iankent.RhUnit.assertors._equal.equalAssertor;
import uk.co.iankent.RhUnit.assertors._notEqual.notEqualAssertor;
import uk.co.iankent.RhUnit.assertors._ok.okAssertor;
import uk.co.iankent.RhUnit.assertors._strictEqual.strictEqualAssertor;
import uk.co.iankent.RhUnit.assertors._throws.throwsAssertor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnit {

    protected Logger logger = Logger.getLogger(this.getClass());
    protected static Logger staticLogger = Logger.getLogger(RhUnit.class);

    /* Rhino environment */
    protected static RhinoEnvironmentFactory rhinoEnvironmentFactory = new RhinoEnvironmentFactoryImpl();
    protected static void setRhinoEnvironmentFactory(RhinoEnvironmentFactory factory) {
        rhinoEnvironmentFactory = factory;
    }
    protected RhinoEnvironment rhinoEnvironment;
    protected Hashtable<String, AbstractAssertor> assertors;
    protected static LinkedList<AbstractAssertor> registeredAssertors = new LinkedList<AbstractAssertor>();
    protected static void registerAssertor(Class<?> klass) throws InvalidArgumentException {
        if(!AbstractAssertor.class.isAssignableFrom(klass)) {
            throw new InvalidArgumentException(
                    new String[] {"Assertor class must inherit from " + AbstractAssertor.class.getName()}
            );
        }

        try {
            Constructor c = klass.getConstructor(new Class[] {});
            registeredAssertors.add((AbstractAssertor)c.newInstance(new Object[] {}));
        } catch (NoSuchMethodException e) {
            staticLogger.error(e, e);
            InvalidArgumentException ex = new InvalidArgumentException(
                    new String[] {
                            "Assertor class " + klass.getName() + " does not provide a constructor which takes no arguments"
                    }
            );
            ex.initCause(e);
            throw ex;
        } catch (InstantiationException e) {
            staticLogger.error(e, e);
            InvalidArgumentException ex = new InvalidArgumentException(
                    new String[] {
                            "Assertor class " + klass.getName() + " could not be instantiated"
                    }
            );
            ex.initCause(e);
            throw ex;
        } catch (IllegalAccessException e) {
            staticLogger.error(e, e);
            InvalidArgumentException ex = new InvalidArgumentException(
                    new String[] {
                            "Assertor class " + klass.getName() + " is not public or does not have a public constructor"
                    }
            );
            ex.initCause(e);
            throw ex;
        } catch (InvocationTargetException e) {
            staticLogger.error(e, e);
            InvalidArgumentException ex = new InvalidArgumentException(
                    new String[] {
                            "Assertor class " + klass.getName() + " could not be instantiated"
                    }
            );
            ex.initCause(e);
            throw ex;
        }
    }

    /* Module control */
    protected Queue<Module> queuedModules;
    protected List<Module> completedModules;
    protected Module currentModule;

    /* Execution control */
    protected boolean running;
    protected boolean executing;
    protected RhUnitTimer rhUnitTimer;

    /* QUnitImpl compatibility */
    protected static QUnitFactory qUnitFactory = new QUnitFactoryImpl();
    protected static void setQUnitFactory(QUnitFactory factory) {
        qUnitFactory = factory;
    }
    protected QUnit qUnit;

    public RhUnit() {
        initRhUnit();
    }

    protected void initRhUnit() {
        // rhino setup
        rhinoEnvironment = rhinoEnvironmentFactory.getRhinoEnvironment();

        // module setup
        queuedModules = new LinkedList<Module>();
        completedModules = new LinkedList<Module>();
        currentModule = new DefaultModule(this);
        queuedModules.add(currentModule);

        // reset execution control
        running = true;
        executing = false;
        rhUnitTimer = new RhUnitTimer();

        // setup default assertors
        assertors = new Hashtable<String, AbstractAssertor>();
        addAssertor(new okAssertor());
        addAssertor(new equalAssertor());
        addAssertor(new throwsAssertor());
        addAssertor(new strictEqualAssertor());
        addAssertor(new notEqualAssertor());
        addAssertor(new deepEqualAssertor());

        for(AbstractAssertor assertor : registeredAssertors) {
            addAssertor(assertor);
        }

        // setup RhUnit
        injectRhUnit();
        injectAssertors();

        // setup QUnit
        qUnit = qUnitFactory.getQUnit(this);
    }

    private void injectAssertors() {
        for(AbstractAssertor assertor : assertors.values()) {
            // Add the assertor to the javascript environment
            assertor.setRhUnit(this);
            Object wrappedAssertor = Context.javaToJS(assertor, getScope());
            ScriptableObject.putProperty(getScope(), assertor.getClass().getSimpleName(), wrappedAssertor);

            // Setup the assertion methods
            String className = assertor.getClass().getSimpleName();
            for(Method method : assertor.getClass().getMethods()) {
                Assertor a = method.getAnnotation(Assertor.class);
                if(a == null) continue;

                int paramCount = method.getParameterTypes().length;

                LinkedList<String> params = new LinkedList<String>();
                for(int i = 0; i < paramCount; i++) {
                    params.add(String.format("param%s", i));
                }

                String paramList = StringUtils.join(params.toArray(), ", ");
                String js = String.format(
                        "function %s(%s) { return %s.%s(%s); }",
                        a.value(),
                        paramList,
                        className,
                        method.getName(),
                        paramList
                );

                logger.trace("Executing javascript: " + js);

                getContext().evaluateString(getScope(), js, className, 1, null);
                getContext().evaluateString(getScope(), "RhUnitAssert." + a.value() + " = " + a.value(), "RhUnitAssert", 1, null);
            }
        }
    }

    private void injectRhUnit() {
        Object wrappedRhUnit = Context.javaToJS(this, getScope());
        ScriptableObject.putProperty(getScope(), "RhUnit", wrappedRhUnit);
        rhinoEnvironment.requireResource("rhunit.js");
    }

    public void addAssertor(AbstractAssertor assertor) {
        if(assertors.get(assertor.getClass().getSimpleName()) != null) return;
        assertors.put(assertor.getClass().getSimpleName(), assertor);
    }

    public void test(String test) {
        // Load the javascript
        this.rhinoEnvironment.requireResource(test);

        // Now run the tests
        currentModule = null;
        executeTests();
    }

    public void rhUnitBegin() {
        // Start the execution timer
        if(!rhUnitTimer.start()) return;

        // Run any QUnitImpl.begin() callbacks
        qUnit.callQUnitBegin();
    }
    public void rhUnitDone() {
        afterRhUnit();

        // Stop the execution timer
        rhUnitTimer.stop();

        // Run any QUnitImpl.done() callbacks
        qUnit.callQUnitDone(rhUnitTimer.getRuntime(), getTotal(), getPassed(), getFailed());

        // Log the execution runtime
        logger.trace("Runtime was " + rhUnitTimer.getRuntime() + "ms");
    }

    public Context getContext() {
        return rhinoEnvironment.getContext();
    }

    public Scriptable getScope() {
        return rhinoEnvironment.getScope();
    }

    public RhinoEnvironment getRhinoEnvironment() {
        return rhinoEnvironment;
    }

    public QUnit getQUnit() {
        return qUnit;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isExecuting() {
        return executing;
    }

    public void afterRhUnit() {
        waitForTestCompletion();

        if(queuedModules.size() > 0)
            logger.error("Some modules were not complete");

        if(getTotal() == 0)
            logger.error("No tests were run");

        logger.info("RhUnit tests complete");

        outputTestResults();
    }

    public void outputTestResults() {
        for(Module module : completedModules) {
            module.outputTestResults();
        }
        logger.info(this);
    }

    private void waitForTestCompletion() {
        // Wait until rhinoTimer has completed all callbacks and execution has stopped
        while(rhinoEnvironment.getRhinoTimer().getOutstandingTimeouts() > 0 || executing) {
            // This trace is *overly* verbose!
            //logger.trace("Still got outstanding tests: " + outstandingTimeouts + " " + executing);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e, e);
            }
        }
    }

    public int getTotal() {
        int total = 0;
        for(Module module : completedModules) {
            total += module.getTotal();
        }
        return total;
    }

    public int getPassed() {
        int passed = 0;
        for(Module module : completedModules) {
            passed += module.getPassed();
        }
        return passed;
    }

    public int getFailed() {
        int failed = 0;
        for(Module module : completedModules) {
            failed += module.getFailed();
        }
        return failed;
    }

    @Override
    public String toString() {
        return "RhUnit{" +
                "total=" + getTotal() +
                ", passed=" + getPassed() +
                ", failed=" + getFailed() +
                '}';
    }

    public void result(AbstractAssertorResult result) {
        // do something with it - i.e. add it to the current test!
        if(currentModule == null || currentModule.currentTest == null) {
            RuntimeException e = new RuntimeException(result.getName() + "() called outside test()");
            logger.error(e);
        } else {
            result.setModule(currentModule);
            currentModule.currentTest.result(result);
        }

        qUnit.callQUnitLog(result);
    }

    public void executeTests() {
        qUnit.callQUnitBegin();
        rhUnitBegin();

        while(queuedModules.size() > 0) {
            Module module = queuedModules.remove();
            currentModule = module;
            module.execute();
            completedModules.add(module);
            currentModule = null;
        }

        rhUnitDone();
        qUnit.callQUnitDone(rhUnitTimer.getRuntime(), getTotal(), getFailed(), getPassed());
    }

    /**
     * Loads a resource into the Rhino environment
     * @param jsName
     */
    public void _load(String jsName) {
        logger.trace("load() called with jsName " + jsName);
        rhinoEnvironment.requireResource(jsName);
    }

    /* Asynchronous test control */
    public void _start() { _start(1); }
    public void _start(int decrement) {
        /* TODO
           This doesn't really do anything...
           RhUnit waits for any timers linked to a test to completed so
           asynchronous testing works out of the box without needing start()/stop() calls
         */
        running = true;
        logger.trace("start()");
    }
    public void _stop() { _stop(1); }
    public void _stop(int increment) {
        /* TODO
           This doesn't really do anything...
           RhUnit waits for any timers linked to a test to completed so
           asynchronous testing works out of the box without needing start()/stop() calls
         */
        logger.trace("stop()");
        running = false;
    }

    public void _test(String message, int expected, NativeFunction block) {
        logger.trace("Called test() with name: " + message + "; expected: " + expected);

        Test test = new Test(this, message, block, expected);
        currentModule.addTest(test);
    }
    public void _test(String message, NativeFunction block) {
        logger.trace("Called test() with name: " + message);

        Test test = new Test(this, message, block, null);
        currentModule.addTest(test);
    }

    public void _asyncTest(String message, Integer expected, NativeFunction block) {
        logger.trace("Called asyncTest() with name: " + message);

        Test test = new Test(this, message, block, expected);
        test.setAsync(true);
        currentModule.addTest(test);
    }
    public void _asyncTest(String message, NativeFunction block) {
        logger.trace("Called asyncTest() with name: " + message);

        Test test = new Test(this, message, block, null);
        test.setAsync(true);
        currentModule.addTest(test);
    }

    public void _expect(int tests) {
        logger.trace("Called expect() with tests: " + tests);

        if(currentModule == null || currentModule.currentTest == null)
            throw new RuntimeException("expect() called without test()");

        currentModule.currentTest.expects(tests);
    }

    public void _module(String name, NativeFunction setup, NativeFunction teardown) {
        logger.trace("Called module() with name: " + name);

        Module module = new Module(this, name, setup, teardown);
        queuedModules.add(module);
        currentModule = module;
    }

}
