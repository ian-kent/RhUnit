package uk.co.iankent.RhUnit.assertors._throws;

import org.mozilla.javascript.RhinoException;
import uk.co.iankent.RhUnit.RhinoEnvironment;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class throwsAssertorResult extends AbstractAssertorResult {

    protected boolean result;
    protected RhinoException exception;

    public throwsAssertorResult(String message, boolean result, RhinoException exception) {
        super(message);
        this.result = result;
        this.exception = exception;
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
