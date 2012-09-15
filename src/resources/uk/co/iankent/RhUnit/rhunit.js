console.log("Injecting RhUnit methods");

var RhUnitAssert = {};

function load(file) {
    RhUnit._load(file);
}
function test(message, expected, block) {
    if(typeof block === "undefined") {
        block = expected;
        RhUnit._test(message, block);
    } else {
        RhUnit._test(message, expected, block);
    }
}
function expect(tests) {
    RhUnit._expect(tests);
}
function asyncTest(message, expected, block) {
    if(typeof block === "undefined") {
        block = expected;
        RhUnit._asyncTest(message, block);
    } else {
        RhUnit._asyncTest(message, expected, block);
    }
}
function start() {
    RhUnit._start();
}
function stop() {
    RhUnit._stop();
}
function module(name, lifecycle) {
    var setup = function() {};
    var teardown = function() {};
    if(typeof lifecycle != "undefined") {
        if(typeof lifecycle.setup != "undefined") {
            setup = lifecycle.setup;
        }
        if(typeof lifecycle.teardown != "undefined") {
            teardown = lifecycle.teardown;
        }
    }
    RhUnit._module(name, setup, teardown);
}
function setTimeout(block, timeout) {
    return RhUnit.getRhinoEnvironment().getRhinoTimer()._setTimeout(timeout, block);
}
function cancelTimeout(id) {
    RhUnit.getRhinoEnvironment().getRhinoTimer()._cancelTimeout(id);
}
function setInterval(block, timeout) {
    return RhUnit.getRhinoEnvironment().getRhinoTimer()._setInterval(timeout, block);
}
function cancelInterval(id) {
    RhUnit.getRhinoEnvironment().getRhinoTimer()._cancelInterval(id);
}

/* Emulate QUnit features */
var QUnit = {
    begin: function(callback) { RhUnit.getQUnit()._registerQUnitBegin(callback); },
    done: function(callback) { RhUnit.getQUnit()._registerQUnitDone(callback); },
    log: function(callback) { RhUnit.getQUnit()._registerQUnitLog(callback); },
    moduleDone: function(callback) { RhUnit.getQUnit()._registerQUnitModuleDone(callback); },
    moduleStart: function(callback) { RhUnit.getQUnit()._registerQUnitModuleStart(callback); },
    testDone: function(callback) { RhUnit.getQUnit()._registerQUnitTestDone(callback); },
    testStart: function(callback) { RhUnit.getQUnit()._registerQUnitTestStart(callback); },
    init: function() { RhUnit.getQUnit()._init(); },
    reset: function() { RhUnit.getQUnit()._reset(); }
};