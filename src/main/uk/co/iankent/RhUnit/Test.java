package uk.co.iankent.RhUnit;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (c) Ian Kent, 2012
 */
public class Test {

    protected String message = null;
    protected Integer expected = null;
    protected List<Assert> asserts = new LinkedList<Assert>();

    protected int passed = 0, failed = 0, total = 0;

    @Override
    public String toString() {
        return "Test{" +
                "message='" + message + '\'' +
                ", passed=" + passed +
                ", failed=" + failed +
                ", total=" + total +
                '}';
    }

    public Test(String message) {
        this.message = message;
    }

    public void assertion(Assert assertion) {
        asserts.add(assertion);

        if(assertion.getResult())
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
            throw new RuntimeException("Expected " + expected + " tests, ran " + total + " tests");
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

    public List<Assert> getAsserts() {
        return asserts;
    }
}
