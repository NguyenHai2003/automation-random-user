package com.company.framework.drivers.factory;

import com.company.framework.enums.BrowserType;
import com.company.framework.utils.LogUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Factory class để tạo WebDriver cho các browser khác nhau
 */
public class DriverFactory {

    /**
     * Tạo WebDriver dựa trên BrowserType
     */
    public static WebDriver createDriver(BrowserType browserType, DriverConfig config) {
        LogUtils.info("🔧 Đang tạo WebDriver cho browser: " + browserType.getValue());

        switch (browserType) {
            case CHROME:
                return createChromeDriver(config);
            case FIREFOX:
                return createFirefoxDriver(config);
            case EDGE:
                return createEdgeDriver(config);
            case SAFARI:
                return createSafariDriver(config);
            default:
                throw new IllegalArgumentException("BrowserType không được hỗ trợ: " + browserType);
        }
    }

    /**
     * Tạo ChromeDriver
     */
    private static WebDriver createChromeDriver(DriverConfig config) {
        // Auto setup ChromeDriver nếu được bật
        if (config.autoSetupDriver) {
            if (config.driverVersion != null && !config.driverVersion.isEmpty()) {
                WebDriverManager.chromedriver().driverVersion(config.driverVersion).setup();
            } else if (config.browserVersion != null && !config.browserVersion.isEmpty()) {
                WebDriverManager.chromedriver().browserVersion(config.browserVersion).setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }
        }

        ChromeOptions options = DriverOptionsFactory.createChromeOptions(config);
        WebDriver driver = new ChromeDriver(options);
        LogUtils.info("✅ ChromeDriver đã được tạo thành công");
        return driver;
    }

    /**
     * Tạo FirefoxDriver
     */
    private static WebDriver createFirefoxDriver(DriverConfig config) {
        // Auto setup GeckoDriver nếu được bật
        if (config.autoSetupDriver) {
            if (config.driverVersion != null && !config.driverVersion.isEmpty()) {
                WebDriverManager.firefoxdriver().driverVersion(config.driverVersion).setup();
            } else {
                WebDriverManager.firefoxdriver().setup();
            }
        }

        FirefoxOptions options = DriverOptionsFactory.createFirefoxOptions(config);
        WebDriver driver = new FirefoxDriver(options);
        LogUtils.info("✅ FirefoxDriver đã được tạo thành công");
        return driver;
    }

    /**
     * Tạo EdgeDriver
     */
    private static WebDriver createEdgeDriver(DriverConfig config) {
        // Auto setup EdgeDriver nếu được bật
        if (config.autoSetupDriver) {
            if (config.driverVersion != null && !config.driverVersion.isEmpty()) {
                WebDriverManager.edgedriver().driverVersion(config.driverVersion).setup();
            } else {
                WebDriverManager.edgedriver().setup();
            }
        }

        EdgeOptions options = DriverOptionsFactory.createEdgeOptions(config);
        WebDriver driver = new EdgeDriver(options);
        LogUtils.info("✅ EdgeDriver đã được tạo thành công");
        return driver;
    }

    /**
     * Tạo SafariDriver
     */
    private static WebDriver createSafariDriver(DriverConfig config) {
        SafariOptions options = DriverOptionsFactory.createSafariOptions(config);
        WebDriver driver = new SafariDriver(options);
        LogUtils.info("✅ SafariDriver đã được tạo thành công");
        return driver;
    }

    /**
     * Inner class để chứa driver configuration
     */
    public static class DriverConfig {
        public boolean headless = false;
        public boolean autoSetupDriver = true;
        public String driverVersion;
        public String browserVersion;
        public int windowWidth = 1920;
        public int windowHeight = 1080;
        public String[] arguments;
        public String downloadPath;
        public boolean incognito = false;
    }
}

