package com.company.framework.keywords;

import com.company.framework.constants.ConfigData;
import com.company.framework.drivers.DriverManager;
import com.company.framework.helpers.browser.BrowserHelper;
import com.company.framework.reports.AllureManager;
import com.company.framework.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

/**
 * WebUI - Class chứa các keywords cho Web Browser testing
 * Sử dụng cho Web automation
 */
public class WebUI {

    private static final int DEFAULT_TIMEOUT = Integer.parseInt(ConfigData.TIMEOUT_EXPLICIT_DEFAULT);
    private static final double STEP_ACTION_TIMEOUT = Double.parseDouble(ConfigData.STEP_ACTION_TIMEOUT);

    /**
     * Điều hướng đến URL
     */
    @Step("Navigate to URL: {0}")
    public static void navigateToUrl(String url) {
        BrowserHelper.navigateToUrl(url);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
    }

    /**
     * Lấy URL hiện tại
     */
    @Step("Get current URL")
    public static String getCurrentUrl() {
        return BrowserHelper.getCurrentUrl();
    }

    /**
     * Lấy title của trang
     */
    @Step("Get page title")
    public static String getPageTitle() {
        return BrowserHelper.getPageTitle();
    }

    /**
     * Quay lại trang trước
     */
    @Step("Go back")
    public static void goBack() {
        BrowserHelper.goBack();
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
    }

    /**
     * Đi tới trang tiếp theo
     */
    @Step("Go forward")
    public static void goForward() {
        BrowserHelper.goForward();
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
    }

    /**
     * Refresh trang
     */
    @Step("Refresh page")
    public static void refresh() {
        BrowserHelper.refresh();
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
    }

    /**
     * Click element trong browser
     */
    @Step("Click element {0}")
    public static void clickElement(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Clicking element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        waitForElementToBeClickable(locator).click();
    }

    /**
     * Set text vào element trong browser
     */
    @Step("Set text '{1}' on element {0}")
    public static void setText(By locator, String text) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Setting text '" + text + "' on element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        WebElement element = waitForElementVisible(locator);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Lấy text từ element trong browser
     */
    @Step("Get text from element {0}")
    public static String getElementText(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Getting text from element: " + locator);
        WebElement element = waitForElementVisible(locator);
        String text = element.getText();
        AllureManager.saveTextLog("➡️ TEXT: " + text);
        return text;
    }

    /**
     * Gửi phím (keys) vào element trong browser
     * Có thể dùng để gửi Keys.ENTER, Keys.TAB, v.v.
     */
    @Step("Send keys '{1}' to element {0}")
    public static void sendKeys(By locator, CharSequence... keys) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Sending keys to element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        WebElement element = waitForElementVisible(locator);
        element.sendKeys(keys);
        LogUtils.info("[WebUI] Keys sent successfully");
    }

    /**
     * Chờ page load
     */
    public static void waitForPageLoad(int timeoutSeconds) {
        LogUtils.info("[WebUI] Waiting for page to load");
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        LogUtils.info("[WebUI] Page loaded successfully");
    }

    /**
     * Hover (di chuột) vào element
     * Thường dùng để hiển thị dropdown menu hoặc tooltip
     */
    @Step("Hover over element {0}")
    public static void hoverElement(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Hovering over element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        WebDriver driver = DriverManager.getDriver();
        WebElement element = waitForElementVisible(locator);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        LogUtils.info("[WebUI] Hovered successfully");
    }

    /**
     * Double click vào element
     */
    @Step("Double click element {0}")
    public static void doubleClickElement(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Double clicking element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        WebDriver driver = DriverManager.getDriver();
        WebElement element = waitForElementVisible(locator);
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
        LogUtils.info("[WebUI] Double clicked successfully");
    }

    /**
     * Right click (context click) vào element
     */
    @Step("Right click element {0}")
    public static void rightClickElement(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Right clicking element: " + locator);
        if (ConfigData.SCREENSHOT_ALL_STEP.equalsIgnoreCase("true")) {
            AllureManager.saveScreenshotPNG();
        }
        WebDriver driver = DriverManager.getDriver();
        WebElement element = waitForElementVisible(locator);
        Actions actions = new Actions(driver);
        actions.contextClick(element).perform();
        LogUtils.info("[WebUI] Right clicked successfully");
    }

    /**
     * Scroll đến element
     */
    @Step("Scroll to element {0}")
    public static void scrollToElement(By locator) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Scrolling to element: " + locator);
        WebDriver driver = DriverManager.getDriver();
        WebElement element = waitForElementVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        LogUtils.info("[WebUI] Scrolled to element successfully");
    }

    /**
     * Scroll theo pixel
     */
    @Step("Scroll by {1} pixels")
    public static void scrollBy(int x, int y) {
        LogUtils.info("[WebUI] Scrolling by (" + x + ", " + y + ") pixels");
        WebDriver driver = DriverManager.getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(" + x + "," + y + ");");
        LogUtils.info("[WebUI] Scrolled successfully");
    }

    /**
     * Lấy attribute của element
     */
    @Step("Get attribute '{1}' from element {0}")
    public static String getAttribute(By locator, String attributeName) {
        sleep(STEP_ACTION_TIMEOUT);
        LogUtils.info("[WebUI] Getting attribute '" + attributeName + "' from element: " + locator);
        WebElement element = waitForElementVisible(locator);
        String value = element.getAttribute(attributeName);
        AllureManager.saveTextLog("➡️ ATTRIBUTE '" + attributeName + "': " + value);
        return value;
    }

    /**
     * Kiểm tra element có hiển thị không
     */
    @Step("Check if element {0} is displayed")
    public static boolean isElementDisplayed(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            boolean displayed = element.isDisplayed();
            LogUtils.info("[WebUI] Element is displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            LogUtils.info("[WebUI] Element is not displayed");
            return false;
        }
    }

    /**
     * Kiểm tra element có enabled không
     */
    @Step("Check if element {0} is enabled")
    public static boolean isElementEnabled(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            boolean enabled = element.isEnabled();
            LogUtils.info("[WebUI] Element is enabled: " + enabled);
            return enabled;
        } catch (Exception e) {
            LogUtils.info("[WebUI] Element is not enabled");
            return false;
        }
    }

    /**
     * Kiểm tra element có selected không (checkbox, radio button)
     */
    @Step("Check if element {0} is selected")
    public static boolean isElementSelected(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            boolean selected = element.isSelected();
            LogUtils.info("[WebUI] Element is selected: " + selected);
            return selected;
        } catch (Exception e) {
            LogUtils.info("[WebUI] Element is not selected");
            return false;
        }
    }

    /**
     * Switch to frame bằng index
     */
    @Step("Switch to frame {0}")
    public static void switchToFrame(int frameIndex) {
        LogUtils.info("[WebUI] Switching to frame index: " + frameIndex);
        WebDriver driver = DriverManager.getDriver();
        driver.switchTo().frame(frameIndex);
        LogUtils.info("[WebUI] Switched to frame successfully");
    }

    /**
     * Switch to frame bằng locator
     */
    @Step("Switch to frame {0}")
    public static void switchToFrame(By locator) {
        LogUtils.info("[WebUI] Switching to frame: " + locator);
        WebDriver driver = DriverManager.getDriver();
        WebElement frameElement = waitForElementVisible(locator);
        driver.switchTo().frame(frameElement);
        LogUtils.info("[WebUI] Switched to frame successfully");
    }

    /**
     * Switch về default content (ra khỏi frame)
     */
    @Step("Switch to default content")
    public static void switchToDefaultContent() {
        LogUtils.info("[WebUI] Switching to default content");
        WebDriver driver = DriverManager.getDriver();
        driver.switchTo().defaultContent();
        LogUtils.info("[WebUI] Switched to default content successfully");
    }

    /**
     * Switch to window bằng title
     */
    @Step("Switch to window with title: {0}")
    public static void switchToWindow(String windowTitle) {
        LogUtils.info("[WebUI] Switching to window with title: " + windowTitle);
        WebDriver driver = DriverManager.getDriver();
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(windowTitle)) {
                LogUtils.info("[WebUI] Switched to window successfully");
                return;
            }
        }
        LogUtils.warn("[WebUI] Window with title '" + windowTitle + "' not found");
    }

    /**
     * Switch to window bằng handle
     */
    @Step("Switch to window")
    public static void switchToWindow() {
        LogUtils.info("[WebUI] Switching to new window");
        WebDriver driver = DriverManager.getDriver();
        Set<String> windowHandles = driver.getWindowHandles();
        String currentHandle = driver.getWindowHandle();
        for (String handle : windowHandles) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                LogUtils.info("[WebUI] Switched to window successfully");
                return;
            }
        }
    }

    /**
     * Đóng window hiện tại
     */
    @Step("Close current window")
    public static void closeWindow() {
        LogUtils.info("[WebUI] Closing current window");
        WebDriver driver = DriverManager.getDriver();
        driver.close();
        LogUtils.info("[WebUI] Window closed successfully");
    }

    /**
     * Thực thi JavaScript
     */
    @Step("Execute JavaScript: {0}")
    public static Object executeJavaScript(String script, Object... args) {
        LogUtils.info("[WebUI] Executing JavaScript: " + script);
        WebDriver driver = DriverManager.getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object result = js.executeScript(script, args);
        LogUtils.info("[WebUI] JavaScript executed successfully");
        return result;
    }

    /**
     * Chờ URL chứa text
     */
    public static boolean waitForUrlContains(String text, int timeoutSeconds) {
        return BrowserHelper.waitForUrlContains(text, timeoutSeconds);
    }

    // Wait methods
    public static WebElement waitForElementToBeClickable(By locator) {
        LogUtils.info("[WebUI] Waiting for element to be clickable: " + locator);
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementVisible(By locator) {
        LogUtils.info("[WebUI] Waiting for element to be visible: " + locator);
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static void sleep(double second) {
        LogUtils.info("[WebUI] Sleeping for " + second + " seconds.");
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

