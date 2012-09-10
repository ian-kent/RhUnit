package uk.co.iankent.RhUnit;

import com.sun.deploy.util.Waiter;
import org.apache.log4j.Logger;
import org.mozilla.javascript.*;
import uk.co.iankent.RhUnit.assertors.AbstractAssertor;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;
import uk.co.iankent.RhUnit.assertors._equal.equalAssertor;
import uk.co.iankent.RhUnit.assertors._ok.okAssertor;
import uk.co.iankent.RhUnit.assertors._strictEqual.strictEqualAssertor;
import uk.co.iankent.RhUnit.assertors._throws.throwsAssertor;

import java.util.*;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnit {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected RhinoEnvironment environment;
    protected Context context;
    protected Scriptable scope;

    protected Queue<Test> queuedTests = new LinkedList<Test>();
    protected List<Test> completedTests = new LinkedList<Test>();
    protected Test currentTest = null;

    protected int total = 0, passed = 0, failed = 0;

    protected String currentModule = null;

    protected boolean running = true;
    protected boolean executing = false;

    public RhUnit(RhinoEnvironment environment) {
        this.environment = environment;
        this.context = environment.getContext();
        this.scope = environment.getScope();

        beforeRhUnit();
    }

    public Context getContext() {
        return context;
    }

    public Scriptable getScope() {
        return scope;
    }

    protected Timer timer = new Timer();
    protected int outstandingTimeouts = 0;
    protected int timerId = 0;
    HashMap<Integer, TimerTask> timerTasks = new HashMap<Integer, TimerTask>();

    public int _setTimeout(int timeout, final NativeFunction block) {
        outstandingTimeouts++;
        timerId++;
        final int thisTimerId = timerId;

        logger.trace("Setting timeout of " + timeout + "ms with ID " + thisTimerId);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!timerTasks.containsKey(thisTimerId)) return;

                logger.trace("Executing timer with ID " + thisTimerId);

                timerTasks.remove(thisTimerId);
                block.call(getContext(), getScope(), getScope(), new Object[] {});
                outstandingTimeouts--;
            }
        };
        timerTasks.put(thisTimerId, task);
        timer.schedule(task, timeout);

        return thisTimerId;
    }

    public void _cancelTimeout(int id) {
        logger.trace("Cancelling timeout with ID " + id);
        timerTasks.remove(id);
    }

    protected void beforeRhUnit() {
        Object wrappedRhUnit = Context.javaToJS(this, scope);
        ScriptableObject.putProperty(scope, "RhUnit", wrappedRhUnit);

        // This maps javascript functions to RhUnit methods
        String code =
                "function load(file) { RhUnit._load(file); };\n" +
                "function test(message, block) { RhUnit._test(message, block); };\n" +
                "function expect(tests) { RhUnit._expect(tests); };\n" +
                "function start() { RhUnit._start(); };\n" +
                "function stop() { RhUnit._stop(); };\n" +
                "function module(name) { RhUnit._module(name); };\n" +
                "function setTimeout(block, timeout) { RhUnit._setTimeout(timeout, block); };\n" +
                "function cancelTimeout(id) { RhUnit._cancelTimeout(id); };\n";
        context.evaluateString(scope, code, "RhUnit", 1, null);

        List<AbstractAssertor> assertors = new LinkedList<AbstractAssertor>();
        assertors.add(new okAssertor());
        assertors.add(new equalAssertor());
        assertors.add(new throwsAssertor());
        assertors.add(new strictEqualAssertor());

        for(AbstractAssertor assertor : assertors) {
            assertor.setRhUnit(this);
            Object wrappedAssertor = Context.javaToJS(assertor, scope);
            ScriptableObject.putProperty(scope, assertor.getClass().getSimpleName(), wrappedAssertor);
            context.evaluateString(scope, assertor.getJavascript(), assertor.getClass().getSimpleName(), 1, null);
        }
    }

    public void afterRhUnit() {
        while(outstandingTimeouts > 0 || executing) {
            // This trace is *overly* verbose!
            //logger.trace("Still got outstanding tests: " + outstandingTimeouts + " " + executing);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e, e);
            }
        }

        if(queuedTests.size() > 0)
            logger.error("Some tests were not complete");

        if(completedTests.size() == 0)
            logger.error("No tests were run");

        logger.info("RhUnit tests complete");
        for(Test test : completedTests) {
            total += test.getTotal();
            passed += test.getPassed();
            failed += test.getFailed();
            logger.info(test.toString());

            for(AbstractAssertorResult result : test.getResults()) {
                logger.info(result.toString());
            }
        }
        logger.info(toString());
    }

    public int getTotal() {
        return total;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    @Override
    public String toString() {
        return "RhUnit{" +
                "total=" + total +
                ", passed=" + passed +
                ", failed=" + failed +
                '}';
    }

    public void result(AbstractAssertorResult result) {
        // do something with it - i.e. add it to the current test!
        if(currentTest == null)
            throw new RuntimeException(result.getName() + "() called outside test()");

        result.setModule(currentModule);
        currentTest.result(result);
    }

    public void executeTests() {
        logger.trace("Beginning executeTests");
        if(executing) {
            logger.trace("Already executing - returning");
            return;
        }
        if(!running) {
            logger.trace("Not running - returning");
            return;
        }
        executing = true;

        if(currentTest != null) {
            logger.trace("Finishing test: " + currentTest.getMessage());
            currentTest.afterTest();
            completedTests.add(currentTest);
            currentTest = null;
        }

        while(queuedTests.size() > 0) {
            if(!running) {
                logger.trace("Not running - assuming stop() was called");
                break;
            }

            Test test = queuedTests.remove();
            currentTest = test;
            test.beforeTest();
            logger.trace("Executing test: " + test.getMessage());
            test.execute();

            if(running) {
                logger.trace("Finishing test: " + test.getMessage());
                test.afterTest();
                currentTest = null;
                completedTests.add(test);
            } else {
                logger.trace("Not running - assuming stop() was called, delaying finishing test");
            }
        }

        executing = false;
        logger.trace("Done executeTests");
    }

    public void _load(String jsName) {
        logger.trace("load() called with jsName " + jsName);
        environment.loadJSResource(jsName);
    }

    public void _start() {
        running = true;
        logger.trace("start()");
        executeTests();
    }
    public void _stop() {
        logger.trace("stop()");
        running = false;
    }

    public void _test(String message, NativeFunction block) {
        logger.trace("Called test() with name: " + message);

        Test test = new Test(message, block);
        test.setRhUnit(this);

        queuedTests.add(test);
        test.setModule(currentModule);

        executeTests();
    }

    public void _expect(int tests) {
        logger.trace("Called expect() with tests: " + tests);

        if(currentTest == null)
            throw new RuntimeException("expect() called without test()");

        currentTest.expects(tests);
    }

    public void _module(String name) {
        logger.trace("Called module() with name: " + name);

        if(currentTest != null)
            throw new RuntimeException("Cannot call module() inside test()");

        if(name != null && name.length() > 0)
            currentModule = name;
        else
            currentModule = null;
    }

}
