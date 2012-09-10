package uk.co.iankent.RhUnit.assertors._equal;

import uk.co.iankent.RhUnit.assertors.AbstractAssertor;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class equalAssertor extends AbstractAssertor {

    @Override
    public String getJavascript() {
        return "function equal(actual, expected, message) { equalAssertor.equal(actual, expected, message); };";
    }

    public void equal(Object actual, Object expected, String message) {
        logger.trace(String.format("Actual[%s], Expected[%s], Message[%s]", actual, expected, message));
        result(new equalAssertorResult(message, actual, expected));
    }
}
