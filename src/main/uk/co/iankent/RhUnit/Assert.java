package uk.co.iankent.RhUnit;

/**
 * Copyright (c) Ian Kent, 2012
 */
public class Assert {

    protected String expected;
    protected String actual;
    protected boolean result;
    protected String message;

    public Assert(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public Assert(String expected, String actual, boolean result, String message) {
        this.expected = expected;
        this.actual = actual;
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Assert{" +
                "message='" + message + '\'' +
                ", expected='" + expected + '\'' +
                ", actual='" + actual + '\'' +
                ", result=" + result +
                '}';
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
