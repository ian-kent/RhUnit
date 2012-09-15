package uk.co.iankent.RhUnit.QUnit;

import org.mozilla.javascript.NativeFunction;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public interface QUnit {
    public void _reset();
    public void _init();

    public void _registerQUnitBegin(NativeFunction callback);
    public void callQUnitBegin();
    public void _registerQUnitDone(NativeFunction callback);
    public void callQUnitDone(long runtime, int total, int failed, int passed);
    public void _registerQUnitLog(NativeFunction callback);
    public void callQUnitLog(AbstractAssertorResult result);
    public void _registerQUnitModuleDone(NativeFunction callback);
    public void callQUnitModuleDone();
    public void _registerQUnitModuleStart(NativeFunction callback);
    public void callQUnitModuleStart();
    public void _registerQUnitTestDone(NativeFunction callback);
    public void callQUnitTestDone();
    public void _registerQUnitTestStart(NativeFunction callback);
    public void callQUnitTestStart();
}
