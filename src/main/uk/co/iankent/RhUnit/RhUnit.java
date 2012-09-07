package uk.co.iankent.RhUnit;

import org.apache.log4j.Logger;
import org.mozilla.javascript.*;

import java.util.LinkedList;
import java.util.List;

/**
 * RhUnit - A Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnit {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected Context context;
    protected Scriptable scope;

    protected List<Test> tests = new LinkedList<Test>();
    protected Test currentTest = null;

    protected int total = 0, passed = 0, failed = 0;

    public RhUnit(Context context, Scriptable scope) {
        this.context = context;
        this.scope = scope;
        beforeRhUnit();
    }

    protected void beforeRhUnit() {
        Object wrappedRhUnit = Context.javaToJS(this, scope);
        ScriptableObject.putProperty(scope, "RhUnit", wrappedRhUnit);

        // This maps javascript functions to RhUnit methods
        String code =
                "function test(message, block) { RhUnit._test(message, block); };\n" +
                "function ok(result, message) { RhUnit._ok(result, message); };\n" +
                "function not_ok(result, message) { RhUnit._not_ok(result, message); };\n" +
                "function equal(actual, expected, message) { RhUnit._equal(actual, expected, message); };\n" +
                "function not_equal(actual, expected, message) { RhUnit._not_equal(actual, expected, message); };\n" +
                "function expect(tests) { RhUnit._expect(tests); };\n" +
                "function throws(block, expected, message) { RhUnit._throws(block, expected, message); };\n" +
                "function not_throws(block, expected, message) { RhUnit._not_throws(block, expected, message); };";

        context.evaluateString(scope, code, "RhUnit", 1, null);
    }

    public void afterRhUnit() {
        if(currentTest == null)
            throw new RuntimeException("No tests were run");

        currentTest.afterTest();
        currentTest = null;

        logger.info("RhUnit tests complete");
        for(Test test : tests) {
            total += test.getTotal();
            passed += test.getPassed();
            failed += test.getFailed();
            logger.info(test.toString());

            for(Assert a : test.getAsserts()) {
                logger.info(a.toString());
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

    public void _test(String message, NativeFunction block) {
        logger.trace("Called test() with name: " + message);

        Test test = new Test(message);
        if(currentTest != null) currentTest.afterTest();
        tests.add(test);
        currentTest = test;
        currentTest.beforeTest();

        block.call(context, scope, scope, new Object[]{});
    }

    public void _ok(boolean result, String message) {
        logger.trace("Called ok() with result: " + result + "; message: " + message);

        if(currentTest == null)
            throw new RuntimeException("ok() called without test()");

        Assert a = new Assert(result, message);
        currentTest.assertion(a);
    }

    public void _not_ok(boolean result, String message) {
        logger.trace("Called not_ok() with result: " + result + "; message: " + message);

        if(currentTest == null)
            throw new RuntimeException("not_ok() called without test()");

        Assert a = new Assert(!result, message);
        currentTest.assertion(a);
    }

    public void _equal(String actual, String expected, String message) {
        logger.trace("Called equal(): " + actual + ", " + expected + ": " + message);

        if(currentTest == null)
            throw new RuntimeException("equal() called without test()");

        Assert a = new Assert(expected, actual, actual.equals(expected), message);
        currentTest.assertion(a);
    }

    public void _not_equal(String actual, String expected, String message) {
        logger.trace("Called not_equal(): " + actual + ", " + expected + ": " + message);

        if(currentTest == null)
            throw new RuntimeException("not_equal() called without test()");

        Assert a = new Assert(expected, actual, !actual.equals(expected), message);
        currentTest.assertion(a);
    }

    public void _expect(int tests) {
        logger.trace("Called expect() with tests: " + tests);

        if(currentTest == null)
            throw new RuntimeException("expect() called without test()");

        currentTest.expects(tests);
    }

    public void _throws(Object block, String expected, String message) {
        if(message == null) {
            // expected is optional in qUnit!
            message = expected;
            expected = null;
        }
        logger.trace("Called throws() with message: " + message);

        if(currentTest == null)
            throw new RuntimeException("throws() called without test()");

        if(NativeFunction.class.isInstance(block)) {
            try {
                logger.trace("Given native function");
                ((NativeFunction)block).call(context, scope, scope, new Object[] {});

                // No exception thrown so its a failure
                Assert a = new Assert(false, message);
                a.setExpected(expected);
                currentTest.assertion(a);
            } catch (RhinoException e) {
                logger.trace("Exception thrown by native function");

                // Exception thrown so its a pass
                // TODO need to actually check the messages returned here!
                Assert a = new Assert(true, message);
                a.setExpected(expected);
                a.setActual(e.getMessage());
                currentTest.assertion(a);
            }
        } else {
            logger.trace("Not given native function, got " + block.getClass() + ": " + block);

            // We weren't given a block...
            Assert a = new Assert(false, message);
            currentTest.assertion(a);
        }
    }

    public void _not_throws(Object block, String expected, String message) {
        if(message == null) {
            // expected is optional in qUnit!
            message = expected;
            expected = null;
        }
        logger.trace("Called not_throws() with message: " + message);

        if(currentTest == null)
            throw new RuntimeException("not_throws() called without test()");

        if(NativeFunction.class.isInstance(block)) {
            try {
                logger.trace("Given native function");
                ((NativeFunction)block).call(context, scope, scope, new Object[] {});

                // No exception thrown so its a pass
                Assert a = new Assert(true, message);
                a.setExpected(expected);
                currentTest.assertion(a);
            } catch (RhinoException e) {
                logger.trace("Exception thrown by native function");

                // Exception thrown so its a fail
                // TODO need to actually check the messages returned here!
                Assert a = new Assert(false, message);
                a.setExpected(expected);
                a.setActual(e.getMessage());
                currentTest.assertion(a);
            }
        } else {
            logger.trace("Not given native function, got " + block.getClass() + ": " + block);

            // We weren't given a block...
            Assert a = new Assert(true, message);
            currentTest.assertion(a);
        }
    }

}
