package uk.co.iankent.RhUnit.assertors._notEqual;

import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class notEqualAssertorResult extends AbstractAssertorResult {

    protected Object actual;
    protected Object expected;

    public notEqualAssertorResult(String message, Object actual, Object expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getName() {
        return "notEqual";
    }

    @Override
    public String toString() {
        return super.toString() + " (Expected: " + expected + ", Actual: " + actual + ")";
    }

    @Override
    public boolean getPassed() {
        return !actual.toString().equals(expected.toString());
    }
}
