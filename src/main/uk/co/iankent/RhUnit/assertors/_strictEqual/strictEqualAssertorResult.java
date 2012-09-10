package uk.co.iankent.RhUnit.assertors._strictEqual;

import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class strictEqualAssertorResult extends AbstractAssertorResult {

    protected Object actual;
    protected Object expected;

    public strictEqualAssertorResult(String message, Object actual, Object expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getName() {
        return "strictEqual";
    }

    @Override
    public boolean getPassed() {
        return actual.equals(expected) && actual.getClass().equals(expected.getClass());
    }
}
