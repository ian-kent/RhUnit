/**
 * RhUnit - A qUnit compatible Javascript unit testing framework for Rhino
 * Copyright (c) Ian Kent, 2012
 */

/* Checks callbacks */
// TODO need to assert these
QUnit.begin(function() {
    console.log("QUnit.begin callback");
});
QUnit.done(function(result) {
    console.log("QUnit.done callback");
    console.log(result);
});
QUnit.log(function(result) {
    console.log("QUnit.log callback");
    console.log(result);
});
QUnit.moduleDone(function() {
    console.log("QUnit.moduleDone callback");
});
QUnit.moduleStart(function() {
    console.log("QUnit.moduleStart callback");
});
QUnit.testDone(function() {
    console.log("QUnit.testDone callback");
});
QUnit.testStart(function() {
    console.log("QUnit.testStart callback");
});

module("RhUnit");

/* Tests test() */
test( "Test with no expected", function() {
    expect(1);
    ok(true, "Test was called");
});
test( "Test with expected", 1, function() {
    ok(true, "Test was called");
});

/* Tests assertion methods */
test( "Assertion methods", function() {
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

/* Tests assertion methods via assert object */
test( "Assertion methods using assert object", function(assert) {
	expect(8);

    assert.ok(1 == 1, "1 should equal 1");
    assert.ok(2 == 2, "2 should equal 2");

    assert.equal(1, 1, "1 should equal 1");
    assert.equal("a", "a", "a should equal a");
    assert.equal(1, "1.0", "1 should equal 1"); // TODO java toStrings 1 as 1.0

    assert.strictEqual("a", "a", "1 should not equal 1");

    assert.throws(function() { throw "Exception"; }, "Exception", "Throws an exception");
    assert.throws(function() { throw "Another exception"; }, "Another exception", "Throws an exception");
});

/* Tests assertion methods via assert object with different name */
test( "Assertion methods using assert object with different name", function(foo) {
	expect(8);

    foo.ok(1 == 1, "1 should equal 1");
    foo.ok(2 == 2, "2 should equal 2");

    foo.equal(1, 1, "1 should equal 1");
    foo.equal("a", "a", "a should equal a");
    foo.equal(1, "1.0", "1 should equal 1"); // TODO java toStrings 1 as 1.0

    foo.strictEqual("a", "a", "1 should not equal 1");

    foo.throws(function() { throw "Exception"; }, "Exception", "Throws an exception");
    foo.throws(function() { throw "Another exception"; }, "Another exception", "Throws an exception");
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

/* Tests asyncTest */
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

test( "equal test", function() {
    expect(6);

    equal(1, 1, "1 should equal 1");
    equal(2, 2, "2 should equal 2");
    equal("a", "a", "a should equal a");
    equal("b", "b", "b should equal b");
    equal(1, "1.0", "numeric 1 should equal string 1");
    equal(2.5, "2.5", "numeric 2.5 should equal string 2.5");
});

test( "notEqual test", function() {
    expect(4);

    notEqual(1, 2, "1 should not equal 2");
    notEqual(3, 4, "3 should not equal 4");
    notEqual("a", "b", "a should not equal b");
    notEqual(1, "2.0", "numeric 1 should not equal string 2");
});

test( "deepEqual test", function() {
    expect(1);
    var a = {
        foo: "bar",
        baz: "qux"
    };
    var b = {
        foo: "bar",
        baz: "qux"
    };
    deepEqual(a, b, "a should equal b");
});