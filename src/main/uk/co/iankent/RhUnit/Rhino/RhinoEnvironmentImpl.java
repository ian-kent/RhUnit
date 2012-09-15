package uk.co.iankent.RhUnit.Rhino;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Main;

import java.io.*;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class RhinoEnvironmentImpl implements RhinoEnvironment {

    protected Logger logger = Logger.getLogger(RhinoEnvironmentImpl.class);

    protected Context context;
    protected Scriptable scope;
    protected uk.co.iankent.RhUnit.RhUnit RhUnit;
    protected RhinoTimer rhinoTimer;

    protected static RhinoTimerFactory rhinoTimerFactory = new RhinoTimerFactoryImpl();
    protected static void setRhinoTimerFactory(RhinoTimerFactory factory) {
        rhinoTimerFactory = factory;
    }

    public RhinoEnvironmentImpl() {
        prepareRhino();
        loadJSResource("env.rhino.js");
    }

    public RhinoTimer getRhinoTimer() {
        return rhinoTimer;
    }

    private void prepareRhino() {
        logger.trace("Preparing Rhino rhinoEnvironment");
        context = ContextFactory.getGlobal().enterContext();
        try {
            context.setOptimizationLevel(-1);
            context.setLanguageVersion(Context.VERSION_1_7);
            Main.getGlobal().init(context);
        } catch (IllegalStateException e) {
            // This is expected if we're running a series of tests, e.g. with a Suite
            logger.trace(e, e);
        }
        scope = context.initStandardObjects(Main.getGlobal());

        rhinoTimer = rhinoTimerFactory.getRhinoTimer(this);
    }

    public void requireResource(String file) {
        loadJSResource(file);
    }

    protected void loadJSResource(String res) {
        logger.trace("Loading resource " + res);
        InputStream is = ClassLoader.getSystemResourceAsStream(res);
        File f = null;
        try {
            f = File.createTempFile("res.rhino.", ".js");
            FileOutputStream fos = new FileOutputStream(f);
            IOUtils.copy(is, fos);

            logger.trace("Copied resource to " + f.getAbsolutePath());

            Main.processFile(context, scope, f.getAbsolutePath());
        } catch (IOException e) {
            logger.error(e, e);
        } finally {
            if(f != null) f.deleteOnExit();
        }
    }

    public Context getContext() {
        context = ContextFactory.getGlobal().enterContext();
        return context;
    }

    public Scriptable getScope() {
        return scope;
    }
}
