package org.example.jQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.iankent.RhUnit.RhUnit;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public abstract class jQueryUnitBase {

    protected static Logger logger = Logger.getLogger(jQueryUnitBase.class);

    protected RhUnit rhUnit;

    protected abstract void test();

    @Before
    public void before() {
        rhUnit = new RhUnit();
        rhUnit._load("org/example/jQuery/jquery.1.8.1.min.js");
        rhUnit._load("org/example/jQuery/jquery.testinit.js");
    }

    @After
    public void after() {
        if(rhUnit.getFailed() > 0) junit.framework.Assert.fail();
    }

}
