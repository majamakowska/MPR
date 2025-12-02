package org.example.testdoubles;

/** Dummy: nie ma być wywołany w testach, rzuca AssertionError w przypadku wywołania.*/

public class ConfigDummy {
    public ConfigDummy() {}

    public void doNothing() {
        throw new AssertionError("ConfigDummy should not be called");
    }
}