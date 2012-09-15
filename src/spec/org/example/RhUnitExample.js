/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */

/* An example RhUnit (and almost qUnit compatible) test script */

console.log("You can log things to the console");

module("Example module");

test( "Tests that pass", function() {
	expect(8);

    ok(1 == 1, "1 should equal 1");
    ok(2 == 2, "2 should equal 2");

    equal(1, 1, "1 should equal 1");
    equal("a", "a", "a should equal a");
    equal(1, "1.0", "1 should equal 1"); // TODO java toStrings 1 as 1.0

    strictEqual("a", "a", "1 should not equal 1");

    throws(function() { throw "Exception"; }, "Exception", "Throws an exception");
    throws(function() { throw "Another exception"; }, "Another exception", "Throws an exception");
});

/*
test( "Tests that fail", function() {
	expect(5);

    ok(1 == 2, "1 should equal 2");

    equal("a", "b", "a should equal b");
    equal(1, 5, "1 should equal 5");

    strictEqual(1, "1.0", "1 should not equal 1");

    throws(function() { return "Not an exception"; }, "Not an exception", "Throws an exception");
});
*/

test( "Async test", function() {
    expect(1);

    stop();
    setTimeout(function() {
        ok(true, "Delayed async test runs");
        start();
    }, 500);
});

test( "Test following async", function() {
    expect(1);
    ok(true, "This test runs");
});

test( "Repeated async test", function() {
    expect(1);

    stop();
    var counter = 0;
    var id = setInterval(function() {
        counter++;
        if(counter >= 5) {
            cancelInterval(id);
            ok(true, "Interval is repeated");
            start();
        }
    }, 1000);
});

test( "Test following repeated async", function() {
    expect(1);
    ok(true, "This test runs");
});

asyncTest( "asyncTest test", function() {
    expect(1);

    stop();
    setTimeout(function() {
        ok(true, "asyncTest test runs");
        start();
    }, 500);
});

test( "Test following asyncTest test", function() {
    expect(1);
    ok(true, "This test runs");
});