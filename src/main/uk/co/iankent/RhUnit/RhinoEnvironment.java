package uk.co.iankent.RhUnit;

import junit.framework.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Copyright (c) Ian Kent, 2012
 */
public class RhinoEnvironment {

    protected Logger logger = Logger.getLogger(RhinoEnvironment.class);

    protected Context context;
    protected Scriptable scope;
    protected RhUnit RhUnit;

    public RhinoEnvironment() {
        prepareRhino();
        loadJSResource("env.rhino.js");
    }

    private void prepareRhino() {
        logger.trace("Preparing Rhino environment");
        context = ContextFactory.getGlobal().enterContext();
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_7);
        Global global = Main.getGlobal();
        global.init(context);
        scope = context.initStandardObjects(global);
    }

    public void requireResource(String file) {
        loadJSResource(file);
    }

    protected void loadJSResource(String res) {
        logger.trace("Loading resource " + res);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(res);
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
        return context;
    }

    public Scriptable getScope() {
        return scope;
    }
}
