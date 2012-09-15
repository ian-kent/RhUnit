package uk.co.iankent.RhUnit.assertors._ok;

import uk.co.iankent.RhUnit.assertors.AbstractAssertor;
import uk.co.iankent.RhUnit.assertors.Assertor;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class okAssertor extends AbstractAssertor {

    @Assertor("ok")
    public void ok(boolean result, String message) {
        logger.trace(String.format("Result[%s], Message[%s]", result, message));
        result(new okAssertorResult(message, result));
    }

    @Assertor("ok")
    public void ok(Object param, String message) {
        logger.trace(String.format("Result[%s], Message[%s]", param != null, message));
        result(new okAssertorResult(message, param != null));
    }
}
