package com.company.framework.constants;

import com.company.framework.helpers.PropertiesHelpers;
import com.company.framework.helpers.SystemHelpers;

public class ConfigData {

    private ConfigData() {
        // Ngăn chặn khởi tạo class
    }

    // Load all properties files
    static {
        PropertiesHelpers.loadAllFiles();
    }

    public static final String PROJECT_PATH = SystemHelpers.getCurrentDir();
    public static final String EXCEL_DATA_FILE_PATH = PropertiesHelpers.getValue("EXCEL_DATA_FILE_PATH");
    public static final String JSON_DATA_FILE_PATH = PropertiesHelpers.getValue("JSON_DATA_FILE_PATH");
    public static final String TEST_DATA_FOLDER_PATH = PropertiesHelpers.getValue("TEST_DATA_FOLDER_PATH");
    public static final String LOCATE = PropertiesHelpers.getValue("LOCATE");
    public static final String TIMEOUT_EXPLICIT_DEFAULT = PropertiesHelpers.getValue("TIMEOUT_EXPLICIT_DEFAULT");
    public static final String STEP_ACTION_TIMEOUT = PropertiesHelpers.getValue("STEP_ACTION_TIMEOUT");
    public static final String SCREENSHOT_FAIL = PropertiesHelpers.getValue("SCREENSHOT_FAIL");
    public static final String SCREENSHOT_PASS = PropertiesHelpers.getValue("SCREENSHOT_PASS");
    public static final String SCREENSHOT_ALL_STEP = PropertiesHelpers.getValue("SCREENSHOT_ALL_STEP");
    public static final String SCREENSHOT_PATH = PropertiesHelpers.getValue("SCREENSHOT_PATH");
    public static final String BROWSER_TYPE = PropertiesHelpers.getValue("BROWSER_TYPE");
    public static final String HEADLESS = PropertiesHelpers.getValue("HEADLESS");
    public static final String AUTO_SETUP_DRIVER = PropertiesHelpers.getValue("AUTO_SETUP_DRIVER");
    public static final String DRIVER_VERSION = PropertiesHelpers.getValue("DRIVER_VERSION");
    public static final String BROWSER_VERSION = PropertiesHelpers.getValue("BROWSER_VERSION");
}

