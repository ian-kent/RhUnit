package uk.co.iankent.RhUnit.Rhino;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhinoEnvironmentFactoryImpl implements RhinoEnvironmentFactory {
    @Override
    public RhinoEnvironment getRhinoEnvironment() {
        return new RhinoEnvironmentImpl();
    }
}
