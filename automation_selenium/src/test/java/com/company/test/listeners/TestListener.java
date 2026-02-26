package com.company.test.listeners;

import com.company.framework.constants.ConfigData;
import com.company.framework.helpers.CaptureHelpers;
import com.company.framework.helpers.SystemHelpers;
import com.company.framework.reports.AllureManager;
import com.company.framework.utils.DateUtils;
import com.company.framework.utils.LogUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    // ThreadLocal để lưu suite name cho mỗi thread (hỗ trợ parallel execution)
    private static final ThreadLocal<String> suiteName = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext result) {
        //Delete folder screenshots/videos
        LogUtils.info("♻️ Setup môi trường: " + result.getStartDate());

        // Lưu suite name để dùng cho screenshots/videos path
        String suite = result.getSuite().getName();
        suiteName.set(suite);
        LogUtils.info("📁 Suite name: " + suite + " - Screenshots sẽ lưu vào thư mục: " + SystemHelpers.makeSlug(suite));
    }

    @Override
    public void onFinish(ITestContext result) {
        LogUtils.info("🔚 Kết thúc chạy test: " + result.getEndDate());

        // Cleanup ThreadLocal
        suiteName.remove();
    }

    @Override
    public void onTestStart(ITestResult result) {
        LogUtils.info("➡️ Bắt đầu chạy test case: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("✅ Test case " + result.getName() + " is passed.");

        LocalDateTime now = LocalDateTime.now(); // lấy ngày giờ hiện tại
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        LogUtils.info("Thời gian: " + formattedDate);

        if (ConfigData.SCREENSHOT_PASS.equalsIgnoreCase("true")) {
            CaptureHelpers.captureScreenshot(result.getName(), getSuiteName());
        }

        // AllureListener sẽ tự động thêm screenshot vào Allure report HTML
        // (được load qua ServiceLoader - META-INF/services)
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.error("❌ Test case " + result.getName() + " is failed.");

        LocalDateTime now = LocalDateTime.now(); // lấy ngày giờ hiện tại
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        LogUtils.info("Thời gian lỗi: " + formattedDate);
        LogUtils.info("Nguyên nhân lỗi: " + result.getThrowable());

        if (ConfigData.SCREENSHOT_FAIL.equalsIgnoreCase("true")) {
            CaptureHelpers.captureScreenshot(result.getName(), getSuiteName());
        }

        //Add screenshot to Allure report (qua AllureManager)
        AllureManager.saveScreenshotPNG();

        // AllureListener sẽ tự động thêm screenshot vào Allure report HTML
        // (được load qua ServiceLoader - META-INF/services)
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.info("⛔️ Test case " + result.getName() + " is skipped.");
    }

    /**
     * Lấy suite name từ ThreadLocal, nếu không có thì dùng "DefaultSuite"
     */
    private String getSuiteName() {
        String suite = suiteName.get();
        if (suite == null || suite.isEmpty()) {
            suite = "DefaultSuite";
        }
        // Sanitize suite name để dùng làm folder name (loại bỏ ký tự đặc biệt)
        return SystemHelpers.makeSlug(suite);
    }

}

