package com.tilkai.demo.collect.common.model;

/**
 * @author tilkai
 */
public class TestModel {
    protected String name;
    protected String value;

    public TestModel() {}

    public TestModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("name:%s, value:%s", name, value);
    }
}
