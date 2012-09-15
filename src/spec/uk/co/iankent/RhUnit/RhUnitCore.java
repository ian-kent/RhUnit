package uk.co.iankent.RhUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.iankent.RhUnit.Rhino.RhinoEnvironmentImpl;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnitCore {

    protected static Logger logger = Logger.getLogger(RhUnitCore.class);

    protected RhUnit rhUnit;

    public static void main(String[] args) {
        RhUnitCore example = new RhUnitCore();
        example.before();
        example.test();
        example.after();
    }

    @Before
    public void before() {
        rhUnit = new RhUnit();
    }

    @After
    public void after() {
        if(rhUnit.getFailed() > 0) junit.framework.Assert.fail();
    }

    @Test
    public void test() {
        String specName = "uk/co/iankent/RhUnit/RhUnitCore.js";
        rhUnit.test(specName);
    }

}
