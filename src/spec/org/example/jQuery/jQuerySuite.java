package org.example.jQuery;

import org.example.RhUnitExample;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
@Suite.SuiteClasses({
        jQueryUnitAjax.class,
        jQueryUnitAttributes.class,
        jQueryUnitCallbacks.class,
        jQueryUnitCore.class,
        jQueryUnitCss.class,
        jQueryUnitData.class,
        jQueryUnitDeferred.class,
        jQueryUnitDeprecated.class,
        jQueryUnitDimensions.class,
        jQueryUnitEffects.class,
        jQueryUnitEvent.class,
        jQueryUnitExports.class,
        jQueryUnitManipulation.class,
        jQueryUnitOffset.class,
        jQueryUnitQueue.class,
        jQueryUnitSelector.class,
        jQueryUnitSerialize.class,
        jQueryUnitSupport.class,
        jQueryUnitTraversing.class
})
@RunWith(Suite.class)
public class jQuerySuite {

}
