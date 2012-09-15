package uk.co.iankent.RhUnit.QUnit;

import uk.co.iankent.RhUnit.RhUnit;

/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */
public class QUnitFactoryImpl implements QUnitFactory {
    @Override
    public QUnit getQUnit(RhUnit rhUnit) {
        return new QUnitImpl(rhUnit);
    }
}
