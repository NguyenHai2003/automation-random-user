package com.company.framework.drivers.factory;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class để tạo Driver Options cho các browser khác nhau
 */
public class DriverOptionsFactory {

    /**
     * Tạo ChromeOptions
     */
    public static ChromeOptions createChromeOptions(DriverFactory.DriverConfig config) {
        ChromeOptions options = new ChromeOptions();

        if (config.headless) {
            options.addArguments("--headless=new");
        }

        if (config.incognito) {
            options.addArguments("--incognito");
        }

        // Window size
        options.addArguments("--window-size=" + config.windowWidth + "," + config.windowHeight);

        // Common arguments
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Custom arguments
        if (config.arguments != null && config.arguments.length > 0) {
            options.addArguments(Arrays.asList(config.arguments));
        }

        // Download path
        if (config.downloadPath != null && !config.downloadPath.isEmpty()) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", config.downloadPath);
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            prefs.put("safebrowsing.enabled", true);
            options.setExperimentalOption("prefs", prefs);
        }

        return options;
    }

    /**
     * Tạo FirefoxOptions
     */
    public static FirefoxOptions createFirefoxOptions(DriverFactory.DriverConfig config) {
        FirefoxOptions options = new FirefoxOptions();

        if (config.headless) {
            options.addArguments("--headless");
        }

        // Window size
        options.addArguments("--width=" + config.windowWidth);
        options.addArguments("--height=" + config.windowHeight);

        // Custom arguments
        if (config.arguments != null && config.arguments.length > 0) {
            options.addArguments(Arrays.asList(config.arguments));
        }

        // Download path
        if (config.downloadPath != null && !config.downloadPath.isEmpty()) {
            options.addPreference("browser.download.folderList", 2);
            options.addPreference("browser.download.dir", config.downloadPath);
            options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,text/csv");
        }

        return options;
    }

    /**
     * Tạo EdgeOptions
     */
    public static EdgeOptions createEdgeOptions(DriverFactory.DriverConfig config) {
        EdgeOptions options = new EdgeOptions();

        if (config.headless) {
            options.addArguments("--headless=new");
        }

        if (config.incognito) {
            options.addArguments("--inprivate");
        }

        // Window size
        options.addArguments("--window-size=" + config.windowWidth + "," + config.windowHeight);

        // Common arguments
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Custom arguments
        if (config.arguments != null && config.arguments.length > 0) {
            options.addArguments(Arrays.asList(config.arguments));
        }

        // Download path
        if (config.downloadPath != null && !config.downloadPath.isEmpty()) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", config.downloadPath);
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            prefs.put("safebrowsing.enabled", true);
            options.setExperimentalOption("prefs", prefs);
        }

        return options;
    }

    /**
     * Tạo SafariOptions
     */
    public static SafariOptions createSafariOptions(DriverFactory.DriverConfig config) {
        SafariOptions options = new SafariOptions();
        // Safari has limited options compared to other browsers
        return options;
    }
}

