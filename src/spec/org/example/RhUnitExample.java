package org.example;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.iankent.RhUnit.RhUnit;
import uk.co.iankent.RhUnit.RhinoEnvironment;

/**
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnitExample {

    protected static Logger logger = Logger.getLogger(RhUnitExample.class);

    protected RhUnit rhUnit;
    protected RhinoEnvironment rhinoEnvironment;

    public static void main(String[] args) {
        RhUnitExample example = new RhUnitExample();
        example.before();
        example.test();
        example.after();
    }

    @Before
    public void before() {
        rhinoEnvironment = new RhinoEnvironment();
        rhUnit = new RhUnit(rhinoEnvironment.getContext(), rhinoEnvironment.getScope());
    }

    @After
    public void after() {
        rhUnit.afterRhUnit();
        if(rhUnit.getFailed() > 0) junit.framework.Assert.fail();
    }

    @Test
    public void test() {
        String specName = "org/example/RhUnitExample.js";
        rhinoEnvironment.requireResource(specName);
    }

}
