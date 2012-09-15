package uk.co.iankent.RhUnit.Rhino;

import org.mozilla.javascript.NativeFunction;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public interface RhinoTimer {
    public void join();
    public int getOutstandingTimeouts();

    public int _setTimeout(int timeout, final NativeFunction block);
    public int _setInterval(int interval, final NativeFunction block);
    public void _cancelInterval(int id);
    public void _cancelTimeout(int id);
}
