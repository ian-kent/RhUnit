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
    public boolean getPassed() {
        return actual.toString().equals(expected.toString());
    }
}
