package com.griddynamics.jagger.engine.e1.services;

/**
 * Created with IntelliJ IDEA.
 * User: kgribov
 * Date: 12/10/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public enum JaggerPlace {
    TEST_LISTENER("TestListener"),
    TEST_GROUP_LISTENER("TestGroupListener"),
    TEST_SUITE_LISTENER("TestSuiteListener");

    private String name;

    JaggerPlace(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
