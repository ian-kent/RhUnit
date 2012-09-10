package uk.co.iankent.RhUnit.assertors;

import org.apache.log4j.Logger;
import uk.co.iankent.RhUnit.RhUnit;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public abstract class AbstractAssertor {
    protected Logger logger = Logger.getLogger(this.getClass());

    public abstract String getJavascript();

    protected RhUnit rhUnit;

    public void setRhUnit(RhUnit rhUnit) {
        this.rhUnit = rhUnit;
    }

    protected void result(AbstractAssertorResult result) {
        rhUnit.result(result);
    }
}
