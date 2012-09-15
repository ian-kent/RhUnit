package uk.co.iankent.RhUnit.assertors._strictEqual;

import uk.co.iankent.RhUnit.assertors.AbstractAssertor;
import uk.co.iankent.RhUnit.assertors.Assertor;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class strictEqualAssertor extends AbstractAssertor {

    @Assertor("strictEqual")
    public void strictEqual(Object actual, Object expected, String message) {
        logger.trace(String.format("Actual[%s], Expected[%s], Message[%s]", actual, expected, message));
        result(new strictEqualAssertorResult(message, actual, expected));
    }
}
