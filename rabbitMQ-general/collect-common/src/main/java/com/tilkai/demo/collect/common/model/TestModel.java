package com.tilkai.demo.collect.common.model;

/**
 * Created by tilkai on 2017/2/7.
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
