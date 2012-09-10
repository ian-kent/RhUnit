package uk.co.iankent.RhUnit.assertors._strictEqual;

import uk.co.iankent.RhUnit.assertors.AbstractAssertor;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class strictEqualAssertor extends AbstractAssertor {

    @Override
    public String getJavascript() {
        return "function strictEqual(actual, expected, message) { strictEqualAssertor.strictEqual(actual, expected, message); };";
    }

    public void strictEqual(Object actual, Object expected, String message) {
        logger.trace(String.format("Actual[%s], Expected[%s], Message[%s]", actual, expected, message));
        result(new strictEqualAssertorResult(message, actual, expected));
    }
}
