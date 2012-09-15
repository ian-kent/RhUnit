package uk.co.iankent.RhUnit;

import java.util.Date;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhUnitTimer {
    protected Date start;
    protected boolean hasStarted = false;
    protected Date end;
    protected long runtime = 0;

    @Override
    public String toString() {
        return "RhUnitTimer{" +
                "start=" + start +
                ", runtime=" + runtime +
                ", end=" + end +
                '}';
    }

    public boolean start() {
        if(hasStarted) return false;

        hasStarted = true;
        this.start = new Date();
        return true;
    }

    public void stop() {
        this.end = new Date();
        this.runtime = end.getTime() - start.getTime();
    }

    public long getRuntime() {
        return runtime;
    }
}
