package uk.co.iankent.RhUnit.assertors._deepEqual;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.NativeObject;
import uk.co.iankent.RhUnit.assertors.AbstractAssertorResult;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class deepEqualAssertorResult extends AbstractAssertorResult {

    protected Object actual;
    protected Object expected;

    public deepEqualAssertorResult(String message, Object actual, Object expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getName() {
        return "deepEqual";
    }

    @Override
    public boolean getPassed() {
        if(actual == null || expected == null) return actual == expected ? true : false;

        if(!actual.getClass().equals(expected.getClass())) return false;

        return isEqual(actual, expected);
    }

    protected boolean isEqual(Object obj1, Object obj2) {
        if(obj1.getClass().equals(NativeArray.class)) {
            NativeArray n1 = (NativeArray)obj1;
            NativeArray n2 = (NativeArray)obj2;

            Object[] n1Ids = n1.getAllIds();
            Object[] n2Ids = n2.getAllIds();

            for(Object on1 : n1Ids) {
                if(!n2.contains(on1)) return false;
            }
            for(Object on2 : n2Ids) {
                if(!n1.contains(on2)) return false;
            }

            for(Object on1 : n1Ids) {
                Object o1 = n1.get(on1);
                Object o2 = n2.get(on1);
                if(!isEqual(o1, o2)) return false;
            }

            return true;
        }
        if(obj1.getClass().equals(String.class)) {
            return obj1.equals(obj2);
        }
        if(obj1.getClass().equals(Integer.class) ||
            obj1.getClass().equals(Short.class) ||
            obj1.getClass().equals(Long.class) ||
            obj1.getClass().equals(Double.class) ||
            obj1.getClass().equals(Float.class) ||
            obj1.getClass().equals(Byte.class) ||
            obj1.getClass().equals(Character.class) ||
            obj1.getClass().equals(Boolean.class)) {
            return obj1 == obj2;
        }
        if(NativeFunction.class.isAssignableFrom(obj1.getClass())) {
            NativeFunction f1 = (NativeFunction)obj1;
            NativeFunction f2 = (NativeFunction)obj2;
            if(!f1.getFunctionName().equals(f2.getFunctionName())) return false;
            if(!f1.getPrototype().equals(f2.getPrototype())) return false;
            logger.warn("Need better implementation of NativeFunction");
            return true;
        }
        if(obj1.getClass().equals(NativeObject.class)) {
            NativeObject n1 = (NativeObject)obj1;
            NativeObject n2 = (NativeObject)obj2;

            Object[] n1Ids = n1.getAllIds();
            Object[] n2Ids = n2.getAllIds();

            for(Object on1 : n1Ids) {
                if(!n2.containsKey(on1)) return false;
            }
            for(Object on2 : n2Ids) {
                if(!n1.containsKey(on2)) return false;
            }

            for(Object on1 : n1Ids) {
                Object o1 = n1.get(on1);
                Object o2 = n2.get(on1);
                if(!isEqual(o1, o2)) return false;
            }

            return true;
        }
        logger.warn("Deep equal not implemented for class " + obj1.getClass().getName());
        return true;
    }
}
