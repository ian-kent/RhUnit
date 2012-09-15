package uk.co.iankent.RhUnit;

import org.apache.log4j.Logger;
import org.mozilla.javascript.NativeFunction;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class Module {

    protected Logger logger = Logger.getLogger(this.getClass());
    protected RhUnit rhUnit;

    protected String name;
    protected NativeFunction setup;
    protected NativeFunction teardown;

    protected Queue<Test> queuedTests = new LinkedList<Test>();
    protected Queue<Test> completedTests = new LinkedList<Test>();
    protected Test currentTest = null;

    public Module(RhUnit rhUnit, String name, NativeFunction setup, NativeFunction teardown) {
        this.rhUnit = rhUnit;

        this.name = name;
        this.setup = setup;
        this.teardown = teardown;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setup() {
        if(setup != null)
            setup.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[] {});
    }

    public void teardown() {
        if(teardown != null)
            teardown.call(rhUnit.getContext(), rhUnit.getScope(), rhUnit.getScope(), new Object[]{});
    }

    public void addTest(Test test) {
        test.setModule(this);
        queuedTests.add(test);
        currentTest = test;
    }

    public int getTotal() {
        int total = 0;
        for(Test test : completedTests) {
            total += test.getTotal();
        }
        return total;
    }
    public int getFailed() {
        int failed = 0;
        for(Test test : completedTests) {
            failed += test.getFailed();
        }
        return failed;
    }
    public int getPassed() {
        int passed = 0;
        for(Test test : completedTests) {
            passed += test.getPassed();
        }
        return passed;
    }

    public void execute() {
        rhUnit.getQUnit().callQUnitModuleStart();

        setup();

        while(queuedTests.size() > 0) {
            Test test = queuedTests.remove();
            currentTest = test;
            test.execute();
            currentTest = null;
            completedTests.add(test);
        }

        teardown();

        rhUnit.getQUnit().callQUnitModuleDone();
    }

    public void outputTestResults() {
        logger.info(this);

        for(Test test : completedTests) {
            logger.info(test.toString());
            for(AbstractAssertorResult result : test.getResults()) {
                logger.info(result.toString());
            }
        }
    }

    public String getName() {
        return name;
    }
}
