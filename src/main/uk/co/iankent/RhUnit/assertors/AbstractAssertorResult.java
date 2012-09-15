package uk.co.iankent.RhUnit.assertors;

import org.apache.log4j.Logger;
import uk.co.iankent.RhUnit.Module;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public abstract class AbstractAssertorResult {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected String message;
    protected Module module;

    public abstract boolean getPassed();
    public abstract String getName();

    @Override
    public String toString() {
        return String.format(
                "%s %s%s - %s",
                getPassed() ? "PASS" : "FAIL",
                getModule() == null ? "" : getModule().getName() + ": ",
                getName(),
                getMessage()
                );
    }

    protected AbstractAssertorResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
