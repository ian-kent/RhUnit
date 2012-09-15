package org.example.jQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.iankent.RhUnit.RhUnit;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class jQueryUnitAjax extends jQueryUnitBase {
    @Test
    public void test() {
        rhUnit.test("org/example/jQuery/unit/ajax.js");
    }

}
