package com.company.framework.helpers;

import com.company.framework.constants.ConfigData;
import com.company.framework.drivers.DriverManager;
import com.company.framework.utils.DateUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CaptureHelpers {
    /**
     * Hàm static để chụp ảnh màn hình và lưu vào đường dẫn file được chỉ định.
     *
     * @param fileName Đường dẫn file nơi muốn lưu ảnh chụp màn hình (ví dụ: "screenshots/image.png").
     */
    public static void captureScreenshot(String fileName) {
        captureScreenshot(fileName, "DefaultSuite");
    }

    /**
     * Hàm static để chụp ảnh màn hình và lưu vào đường dẫn file được chỉ định với suite name.
     *
     * @param fileName Đường dẫn file nơi muốn lưu ảnh chụp màn hình (ví dụ: "screenshots/image.png").
     * @param suiteName Tên suite để tạo thư mục con (ví dụ: "SuiteWeb").
     */
    public static void captureScreenshot(String fileName, String suiteName) {
        try {
            // Ép kiểu driver thành TakesScreenshot để lấy ảnh màn hình
            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);

            // Sanitize suite name để dùng làm folder name
            String sanitizedSuiteName = SystemHelpers.makeSlug(suiteName);
            String screenshotPath = ConfigData.SCREENSHOT_PATH + sanitizedSuiteName + File.separator;
            SystemHelpers.createFolder(SystemHelpers.getCurrentDir() + screenshotPath);
            String filePath = SystemHelpers.getCurrentDir() + screenshotPath + fileName + "_" + Thread.currentThread().getId() + "_" + SystemHelpers.makeSlug(DateUtils.getCurrentDateTime()) + ".png";

            // Tạo đối tượng Path cho file đích
            Path targetPath = new File(filePath).toPath();

            // Sao chép file từ nguồn sang đích, thay thế file nếu đã tồn tại
            Files.copy(srcFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Chụp ảnh màn hình thành công, lưu tại: " + targetPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Lỗi trong quá trình lưu file ảnh: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình chụp ảnh màn hình: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

