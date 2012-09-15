package uk.co.iankent.RhUnit.QUnit;

import uk.co.iankent.RhUnit.RhUnit;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public interface QUnitFactory {
    public QUnit getQUnit(RhUnit rhUnit);
}
