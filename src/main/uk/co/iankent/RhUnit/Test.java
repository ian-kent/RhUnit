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
    protected String module = null;
    protected NativeFunction block = null;
    protected Integer expected = null;
    protected List<AbstractAssertorResult> results = new LinkedList<AbstractAssertorResult>();

    protected int passed = 0, failed = 0, total = 0;

    protected RhUnit rhUnit;

    public void setRhUnit(RhUnit rhUnit) {
        this.rhUnit = rhUnit;
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

    public Test(String message, NativeFunction block) {
        this.message = message;
        this.block = block;
    }

    public void execute() {
        try {
            block.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[]{});
        } catch (RuntimeException re) {
            logger.error(re, re);
        }
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
        total = passed + failed;

        if(expected != total)
            throw new RuntimeException(
                    (getModule() != null && getModule().length() > 0 ? getModule() + ": " : "") +
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
