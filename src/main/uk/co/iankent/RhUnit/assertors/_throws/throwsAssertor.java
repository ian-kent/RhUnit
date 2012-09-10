package uk.co.iankent.RhUnit.assertors._throws;

import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.RhinoException;
import uk.co.iankent.RhUnit.assertors.AbstractAssertor;
import uk.co.iankent.RhUnit.assertors._equal.equalAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class throwsAssertor extends AbstractAssertor {

    @Override
    public String getJavascript() {
        return "function throws(block, expected, message) { throwsAssertor._throws(block, expected, message); };";
    }

    public void _throws(Object block, String expected, String message) {
        if(message == null) {
            // expected is optional in qUnit!
            message = expected;
            expected = null;
        }

        if(NativeFunction.class.isInstance(block)) {
            try {
                logger.trace("Given native function");
                ((NativeFunction)block).call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});

                // No exception thrown so its a failure
                result(new throwsAssertorResult(message, false, null));
            } catch (RhinoException e) {
                logger.trace("Exception thrown by native function");

                // Exception thrown so its a pass

                String actual = e.getLocalizedMessage().substring(0, e.getLocalizedMessage().lastIndexOf("("));

                if(expected != null)
                    result(new throwsAssertorResult(message, actual.trim().equals(expected), e));
                else
                    result(new throwsAssertorResult(message, true, e));
            }
        } else {
            logger.trace("Not given native function, got " + block.getClass() + ": " + block);

            // We weren't given a block...
            result(new throwsAssertorResult(message, false, null));
        }
    }
}