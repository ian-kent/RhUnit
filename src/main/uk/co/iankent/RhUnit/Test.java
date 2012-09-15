package uk.co.iankent.RhUnit;

import org.apache.log4j.Logger;
import org.mozilla.javascript.NativeFunction;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

import java.util.LinkedList;
import java.util.List;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class Test {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected String message = null;
    protected Module module = null;
    protected NativeFunction block = null;
    protected Integer expected = null;
    protected List<AbstractAssertorResult> results = new LinkedList<AbstractAssertorResult>();

    protected int passed = 0, failed = 0, total = 0;
    protected boolean async = false;

    protected RhUnit rhUnit;

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "Test{" +
                "message='" + message + '\'' +
                ", passed=" + passed +
                ", failed=" + failed +
                ", total=" + total +
                '}';
    }

    public Test(RhUnit rhUnit, String message, NativeFunction block, Integer expected) {
        this.rhUnit = rhUnit;

        this.message = message;
        this.block = block;
        this.expected = expected;
    }

    public void execute() {
        if(isAsync()) rhUnit._stop();

        rhUnit.getQUnit().callQUnitTestStart();
        beforeTest();
        try {
            block.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[]{ rhUnit.getScope().get("RhUnitAssert", rhUnit.getScope()) });
        } catch (RuntimeException re) {
            logger.error(re, re);
        }
        rhUnit.getRhinoEnvironment().getRhinoTimer().join();
        afterTest();
        rhUnit.getQUnit().callQUnitTestDone();
    }

    public void result(AbstractAssertorResult result) {
        results.add(result);

        if(result.getPassed())
            passed++;
        else
            failed++;
    }

    public void expects(Integer expected) {
        this.expected = expected;
    }

    public void afterTest() {
        logger.trace("afterTest for " + getMessage());
        total = passed + failed;

        if(expected != null)
            if(expected != total)
                logger.error(
                        (getModule() != null ? getModule().name + ": " : "") +
                        getMessage() + ": Expected " + expected + " tests, ran " + total + " tests"
                );
    }

    public void beforeTest() {
        // TODO
    }

    public String getMessage() {
        return message;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return total;
    }

    public List<AbstractAssertorResult> getResults() {
        return results;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
