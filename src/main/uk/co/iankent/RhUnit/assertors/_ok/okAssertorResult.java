package uk.co.iankent.RhUnit.assertors._ok;

import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class okAssertorResult extends AbstractAssertorResult {

    protected boolean result;

    public okAssertorResult(String message, boolean result) {
        super(message);
        this.result = result;
    }

    @Override
    public String getName() {
        return "ok";
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public boolean getPassed() {
        return result;
    }
}
