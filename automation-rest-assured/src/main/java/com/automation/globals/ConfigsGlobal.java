package com.automation.globals;

import com.automation.helpers.PropertiesHelpers;

public class ConfigsGlobal {
    public static final String ENV = PropertiesHelpers.getActiveEnv();
    public static final String BASE_URI = PropertiesHelpers.getValue("BASE_URI");
    public static final String BASE_PATH = PropertiesHelpers.getValue("BASE_PATH");
    public static final String USERNAME = PropertiesHelpers.getValue("USERNAME");
    public static final String PASSWORD = PropertiesHelpers.getValue("PASSWORD");
    public static final String DB_HOST = PropertiesHelpers.getValue("DB_HOST");
    public static final String DB_PORT = PropertiesHelpers.getValue("DB_PORT");
    public static final String DB_NAME = PropertiesHelpers.getValue("DB_NAME");
    public static final String DB_USERNAME = PropertiesHelpers.getValue("DB_USERNAME");
    public static final String DB_PASSWORD = PropertiesHelpers.getValue("DB_PASSWORD");
    public static final String DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    public static final String AUTHOR = PropertiesHelpers.getValue("AUTHOR");
}
