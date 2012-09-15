package uk.co.iankent.RhUnit.Rhino;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhinoTimerFactoryImpl implements RhinoTimerFactory {
    @Override
    public RhinoTimer getRhinoTimer(RhinoEnvironment rhinoEnvironment) {
        return new RhinoTimerImpl(rhinoEnvironment);
    }
}
