package com.griddynamics.jagger.dbapi.util;

/**
 * Created with IntelliJ IDEA.
 * User: kgribov
 * Date: 6/25/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class HTMLFormatter {

    private static String BREAK_LINE = "<br>";

    public static String format(String source) {
        return source.replaceAll(System.getProperty("line.separator"), BREAK_LINE);
    }
}
