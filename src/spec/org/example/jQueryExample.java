package org.example;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.iankent.RhUnit.RhUnit;
import uk.co.iankent.RhUnit.RhinoEnvironment;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class jQueryExample {

    protected static Logger logger = Logger.getLogger(jQueryExample.class);

    protected RhUnit rhUnit;
    protected RhinoEnvironment rhinoEnvironment;

    public static void main(String[] args) {
        jQueryExample example = new jQueryExample();
        example.before();
        example.test();
        example.after();
    }

    @Before
    public void before() {
        rhinoEnvironment = new RhinoEnvironment();
        rhUnit = new RhUnit(rhinoEnvironment);
    }

    @After
    public void after() {
        rhUnit.afterRhUnit();
        if(rhUnit.getFailed() > 0) junit.framework.Assert.fail();
    }

    @Test
    public void test() {
        String specName = "org/example/jQueryExample.js";
        rhinoEnvironment.requireResource(specName);
    }

}
