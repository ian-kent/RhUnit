package org.example.jQuery;

import org.junit.Test;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class jQueryUnitAttributes extends jQueryUnitBase {
    @Test
    public void test() {
        rhUnit.test("org/example/jQuery/unit/attributes.js");
    }
}
