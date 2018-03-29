package com.sap.recruiting.assistant.common;

import org.hibernate.dialect.MySQLDialect;

/**
 * Created by Jiaye Wu on 18-3-29.
 */
public class UTF8MySQLDialect extends MySQLDialect {

    @Override
    public String getTableTypeString() {
        return " DEFAULT CHARSET=utf8";
    }
}