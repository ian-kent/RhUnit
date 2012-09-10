/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */

/* An example test script using jQuery */

load("org/example/jquery.1.8.1.min.js");

test( "jQuery exists", function() {
	expect(1);

    ok(jQuery != null && typeof jQuery != "undefined", "jQuery should exist");
});

// Taken from jquery/test/unit/core.js
test("Basic requirements", function() {
	expect(7);
	ok( Array.prototype.push, "Array.push()" );
	ok( Function.prototype.apply, "Function.apply()" );
	ok( document.getElementById, "getElementById" );
	ok( document.getElementsByTagName, "getElementsByTagName" );
	ok( RegExp, "RegExp" );
	ok( jQuery, "jQuery" );
	ok( $, "$" );
});