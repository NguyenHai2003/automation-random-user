package com.automation.helpers;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.LogUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.LinkedList;

public class PropertiesHelpers {

    private static Properties properties;
    private static String linkFile;
    private static FileInputStream file;
    private static String activeEnv = "dev";

    public static Properties loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();
        files.add(FrameworkConstants.COMMON_CONFIG_FILE_PATH);

        try {
            properties = new Properties();

            for (String f : files) {
                linkFile = f;
                file = new FileInputStream(linkFile);
                properties.load(file);
                file.close();
            }

            String env = resolveEnv();
            activeEnv = env;
            String envFile = FrameworkConstants.CONFIG_DIR_PATH + java.io.File.separator + "configs-" + env + ".properties";
            linkFile = envFile;
            file = new FileInputStream(linkFile);
            properties.load(file);
            file.close();

            LogUtils.info("Loaded environment config: " + envFile);
            return properties;
        } catch (IOException e) {
            LogUtils.error("Error loading config file: " + linkFile);
            e.printStackTrace();
            return new Properties();
        }
    }

    private static String resolveEnv() {
        String envFromSystemProperty = System.getProperty("env");
        if (envFromSystemProperty != null && !envFromSystemProperty.isBlank()) {
            return envFromSystemProperty.trim().toLowerCase();
        }

        String envFromCommonConfig = properties.getProperty("ENV");
        if (envFromCommonConfig != null && !envFromCommonConfig.isBlank()) {
            return envFromCommonConfig.trim().toLowerCase();
        }

        return "dev";
    }

    public static String getValue(String key) {
        if (properties == null) {
            properties = loadAllFiles();
        }
        String value = properties.getProperty(key);
        return value != null ? value : "";
    }

    public static String getActiveEnv() {
        if (properties == null) {
            loadAllFiles();
        }
        return activeEnv;
    }
}
