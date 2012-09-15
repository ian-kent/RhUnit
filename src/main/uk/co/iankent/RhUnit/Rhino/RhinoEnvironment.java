package uk.co.iankent.RhUnit.Rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.InputStream;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public interface RhinoEnvironment {
    public void requireResource(InputStream is);
    public Context getContext();
    public Scriptable getScope();
    public RhinoTimer getRhinoTimer();
}
