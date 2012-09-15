package uk.co.iankent.RhUnit.Rhino;

import org.apache.log4j.Logger;
import org.mozilla.javascript.NativeFunction;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhinoTimerImpl implements RhinoTimer {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected RhinoEnvironment rhinoEnvironment;

    protected Timer timer = new Timer();
    protected int outstandingTimeouts = 0;
    protected int timerId = 0;
    protected HashMap<Integer, TimerTask> timerTasks = new HashMap<Integer, TimerTask>();

    public RhinoTimerImpl(RhinoEnvironment rhinoEnvironment) {
        this.rhinoEnvironment = rhinoEnvironment;
    }

    public void join() {
        while(outstandingTimeouts > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e, e);
            }
        }
    }

    public int getOutstandingTimeouts() {
        return outstandingTimeouts;
    }

    public int _setTimeout(int timeout, final NativeFunction block) {
        outstandingTimeouts++;
        timerId++;
        final int thisTimerId = timerId;

        logger.trace("Setting timeout of " + timeout + "ms with ID " + thisTimerId);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!timerTasks.containsKey(thisTimerId)) return;

                logger.trace("Executing timer with ID " + thisTimerId);

                timerTasks.remove(thisTimerId);
                block.call(rhinoEnvironment.getContext(), rhinoEnvironment.getScope(), rhinoEnvironment.getScope(), new Object[] {});
                outstandingTimeouts--;
            }
        };
        timerTasks.put(thisTimerId, task);
        timer.schedule(task, timeout);

        return thisTimerId;
    }

    public int _setInterval(int interval, final NativeFunction block) {
        outstandingTimeouts++;
        timerId++;
        final int thisTimerId = timerId;

        logger.trace("Setting interval of " + interval + "ms with ID " + thisTimerId);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!timerTasks.containsKey(thisTimerId)) return;

                logger.trace("Executing timer with ID " + thisTimerId);

                block.call(rhinoEnvironment.getContext(), rhinoEnvironment.getScope(), rhinoEnvironment.getScope(), new Object[] {});
            }
        };
        timerTasks.put(thisTimerId, task);
        timer.scheduleAtFixedRate(task, 0, interval);

        return thisTimerId;
    }

    public void _cancelInterval(int id) {
        logger.trace("Cancelling interval with ID " + id);
        timerTasks.get(id).cancel();
        timerTasks.remove(id);
        outstandingTimeouts--;
    }

    public void _cancelTimeout(int id) {
        logger.trace("Cancelling timeout with ID " + id);
        timerTasks.get(id).cancel();
        timerTasks.remove(id);
    }
}
