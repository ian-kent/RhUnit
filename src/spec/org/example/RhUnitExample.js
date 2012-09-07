/* An example RhUnit (and almost qUnit compatible) test script */

test( "An example test", function() {
	expect(6);
    ok(1 == 1, "1 should equal 1");
    not_ok(1 == 2, "1 should not equal 2");
    equal(1, 1, "1 should equal 1");
    not_equal(1, 2, "1 should not equal 2");
    throws(function() { throw "Exception"; }, "Exception", "Throws an exception");
    not_throws(function() { return 1; }, "Does not throw an exception");
});