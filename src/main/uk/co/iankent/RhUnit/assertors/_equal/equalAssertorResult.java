package uk.co.iankent.RhUnit.assertors._equal;

import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class equalAssertorResult extends AbstractAssertorResult {

    protected Object actual;
    protected Object expected;

    public equalAssertorResult(String message, Object actual, Object expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getName() {
        return "equal";
    }

    @Override
    public String toString() {
        return super.toString() + " (Expected: " + expected + ", Actual: " + actual + ")";
    }

    @Override
    public boolean getPassed() {
        if(actual == null || expected == null) return actual == expected ? true : false;
        return actual.toString().equals(expected.toString());
    }
}
