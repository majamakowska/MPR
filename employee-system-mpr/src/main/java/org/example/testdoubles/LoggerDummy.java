package org.example.testdoubles;

/** Dummy: nie ma być wywołany w testach, rzuca AssertionError w przypadku wywołania.*/

public class LoggerDummy {
    public LoggerDummy() {}

    public void doNothing() {
        throw new AssertionError("LoggerDummy should not be called");
    }
}
