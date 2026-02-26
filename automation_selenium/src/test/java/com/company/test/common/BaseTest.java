package com.company.test.common;

import com.company.framework.constants.ConfigData;
import com.company.framework.drivers.DriverManager;
import com.company.framework.drivers.factory.DriverFactory;
import com.company.framework.enums.BrowserType;
import com.company.framework.utils.LogUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

/**
 * BaseTest cho Web Browser testing
 * Sử dụng cho Web automation
 */
public class BaseTest {

    /**
     * Setup driver cho Web Browser
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browserType", "headless", "autoSetupDriver", "driverVersion", "browserVersion"})
    public void setUpDriver(
            @Optional String browserType,
            @Optional String headless,
            @Optional String autoSetupDriver,
            @Optional String driverVersion,
            @Optional String browserVersion) {

        LogUtils.info("🚀 Setting up Web Browser driver...");

        // Parse browser type
        BrowserType browser = BrowserType.CHROME; // Default
        if (browserType != null && !browserType.isEmpty()) {
            try {
                browser = BrowserType.fromString(browserType);
            } catch (IllegalArgumentException e) {
                LogUtils.warn("⚠️ Invalid browser type: " + browserType + ", using default: Chrome");
            }
        } else if (ConfigData.BROWSER_TYPE != null && !ConfigData.BROWSER_TYPE.isEmpty()) {
            try {
                browser = BrowserType.fromString(ConfigData.BROWSER_TYPE);
            } catch (IllegalArgumentException e) {
                LogUtils.warn("⚠️ Invalid browser type in config: " + ConfigData.BROWSER_TYPE + ", using default: Chrome");
            }
        }

        // Tạo driver config
        DriverFactory.DriverConfig config = new DriverFactory.DriverConfig();
        config.headless = (headless != null && !headless.isEmpty()) ? Boolean.parseBoolean(headless) :
                (ConfigData.HEADLESS != null && ConfigData.HEADLESS.equalsIgnoreCase("true"));
        config.autoSetupDriver = (autoSetupDriver != null && !autoSetupDriver.isEmpty()) ? Boolean.parseBoolean(autoSetupDriver) :
                (ConfigData.AUTO_SETUP_DRIVER != null && ConfigData.AUTO_SETUP_DRIVER.equalsIgnoreCase("true"));
        config.driverVersion = (driverVersion != null && !driverVersion.isEmpty()) ? driverVersion : ConfigData.DRIVER_VERSION;
        config.browserVersion = (browserVersion != null && !browserVersion.isEmpty()) ? browserVersion : ConfigData.BROWSER_VERSION;

        // Tạo driver
        var driver = DriverFactory.createDriver(browser, config);

        // Set driver vào DriverManager
        DriverManager.setDriver(driver);

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Maximize window
        driver.manage().window().maximize();

        LogUtils.info("✅ Web Browser driver setup completed");
        LogUtils.info("🌐 Browser: " + browser.getValue());
    }

    /**
     * Tear down driver
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        if (DriverManager.getDriver() != null) {
            DriverManager.quitDriver();
            LogUtils.info("✅ Driver quit successfully");
        }
    }
}

