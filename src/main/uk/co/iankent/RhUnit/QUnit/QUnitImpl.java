package uk.co.iankent.RhUnit.QUnit;

import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.Scriptable;
import uk.co.iankent.RhUnit.RhUnit;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

import java.util.LinkedList;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class QUnitImpl implements QUnit {

    protected RhUnit rhUnit;

    protected LinkedList<NativeFunction> qUnitBeginCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitDoneCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitLogCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitModuleDoneCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitModuleStartCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitTestDoneCallbacks = new LinkedList<NativeFunction>();
    protected LinkedList<NativeFunction> qUnitTestStartCallbacks = new LinkedList<NativeFunction>();

    public QUnitImpl(RhUnit rhUnit) {
        this.rhUnit = rhUnit;
    }

    public void _reset() {

    }
    public void _init() {

    }

    public void _registerQUnitBegin(NativeFunction callback) {
        qUnitBeginCallbacks.add(callback);
    }
    public void callQUnitBegin() {
        for(NativeFunction callback : qUnitBeginCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
        }
    }
    public void _registerQUnitDone(NativeFunction callback) {
        qUnitDoneCallbacks.add(callback);
    }
    public void callQUnitDone(long runtime, int total, int failed, int passed) {
        Scriptable obj = rhUnit.getContext().newObject(rhUnit.getScope());
        obj.put("failed", obj, failed);
        obj.put("total", obj, total);
        obj.put("passed", obj, passed);
        obj.put("runtime", obj, runtime);

        for(NativeFunction callback : qUnitDoneCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] { obj });
        }
    }
    public void _registerQUnitLog(NativeFunction callback) {
        qUnitLogCallbacks.add(callback);
    }
    public void callQUnitLog(AbstractAssertorResult result) {
        Scriptable obj = rhUnit.getContext().newObject(rhUnit.getScope());
        obj.put("result", obj, result.getPassed());
        obj.put("message", obj, result.getMessage());
        for(NativeFunction callback : qUnitLogCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] { obj });
        }
    }
    public void _registerQUnitModuleDone(NativeFunction callback) {
        qUnitModuleDoneCallbacks.add(callback);
    }
    public void callQUnitModuleDone() {
        for(NativeFunction callback : qUnitModuleDoneCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
        }
    }
    public void _registerQUnitModuleStart(NativeFunction callback) {
        qUnitModuleStartCallbacks.add(callback);
    }
    public void callQUnitModuleStart() {
        for(NativeFunction callback : qUnitModuleStartCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
        }
    }
    public void _registerQUnitTestDone(NativeFunction callback) {
        qUnitTestDoneCallbacks.add(callback);
    }
    public void callQUnitTestDone() {
        for(NativeFunction callback : qUnitTestDoneCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
        }
    }
    public void _registerQUnitTestStart(NativeFunction callback) {
        qUnitTestStartCallbacks.add(callback);
    }
    public void callQUnitTestStart() {
        for(NativeFunction callback : qUnitTestStartCallbacks) {
            callback.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
        }
    }

}
