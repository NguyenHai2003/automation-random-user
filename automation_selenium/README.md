# 🌐 Selenium Web Automation Framework

## 📝 Giới Thiệu

Web Test Automation Framework với Selenium Java - Hỗ trợ đầy đủ cho **Web Browser Testing** trên các trình duyệt phổ biến.

Framework được thiết kế với cấu trúc rõ ràng, dễ hiểu cho người mới, với sự tối ưu hóa cho web automation testing.

## 🚀 Công Nghệ Chính

- **Selenium WebDriver 4.38.0**: Automation cho web browsers
- **TestNG 7.11.0**: Test framework
- **Allure Report 2.31.0**: Báo cáo test
- **WebDriverManager 5.9.2**: Tự động quản lý browser drivers
- **Maven**: Build và dependency management
- **Java >= 17**: Programming language
- **Apache POI 5.5.0**: Đọc/ghi Excel files
- **Jackson & Gson**: Xử lý JSON
- **Log4j2**: Logging
- **Lombok**: Giảm boilerplate code
- **Multi-Threading, Parallel Testing**: Hỗ trợ chạy song song
- **Keyword Driven Testing**: Pattern cho test automation
- **Data Driven Testing**: Test với nhiều bộ dữ liệu

---

## 🎯 Hỗ Trợ Các Trình Duyệt

### ✅ Chrome

- Chrome browser automation
- Headless mode support
- Chrome options customization

### ✅ Firefox

- Firefox browser automation
- Headless mode support
- Firefox options customization

### ✅ Edge

- Microsoft Edge browser automation
- Edge options customization

### ✅ Safari

- Safari browser automation (macOS only)
- Safari options customization

---

## 🏗️ Cấu Trúc Dự Án

### 1. 📂 Framework Core (`src/main/java/com/company/framework/`)

| Thư mục            | Mục đích                                             |
| :----------------- | :--------------------------------------------------- |
| `enums/`           | Định nghĩa **BrowserType**                           |
| `drivers/`         | Quản lý Driver với Factory Pattern                   |
| `drivers/factory/` | Factory để tạo Driver và Options                     |
| `helpers/`         | Helper classes chung                                 |
| `helpers/browser/` | **BrowserHelper** - Navigation và browser operations |
| `keywords/`        | Keywords cho web automation                          |
| `keywords/`        | **WebUI** - Keywords cho Web Browser                 |
| `reports/`         | Allure Report integration                            |
| `utils/`           | Utilities (Logging, Date, etc.)                      |
| `constants/`       | ConfigData - Load và quản lý config                  |

### 2. 🧪 Test Code (`src/test/java/com/company/test/`)

| Thư mục      | Mục đích                                      |
| :----------- | :-------------------------------------------- |
| `common/`    | **BaseTest** - Base test cho Web automation   |
| `pages/`     | **BasePage** - Base page cho Web pages        |
| `testcases/` | Test cases (tổ chức theo feature hoặc module) |
| `listeners/` | TestNG Listeners                              |

### 3. 📁 Resources (`src/test/resources/`)

| Thư mục     | Mục đích                       |
| :---------- | :----------------------------- |
| `configs/`  | Cấu hình browser và môi trường |
| `suites/`   | TestNG XML suites              |
| `testdata/` | Test data (Excel, JSON)        |

---

## 🎯 Tính Năng Chính

### 🌐 Web Actions

- ✅ **Click, SendKeys, Hover** - Các thao tác cơ bản
- ✅ **Double Click, Right Click** - Context actions
- ✅ **Scroll** - Scroll to element hoặc theo pixel
- ✅ **Frame/Window Management** - Switch giữa frames và windows
- ✅ **JavaScript Execution** - Thực thi JavaScript
- ✅ **Get Text/Attribute** - Lấy thông tin từ elements

### 🔍 Locators

- ✅ **ID, CSS Selector, XPath** - Các loại locator phổ biến
- ✅ **Name, ClassName, LinkText** - Locators bổ sung
- ✅ **LocatorHelper** - Helper class để tạo locators dễ dàng

### 📊 Reporting & Logging

- ✅ **Allure Report** - Báo cáo test đẹp mắt với screenshots
- ✅ **Log4j2** - Logging chi tiết
- ✅ **Screenshot tự động** - Khi test fail/pass hoặc mỗi step

### 🧪 Testing Features

- ✅ **Page Object Model** - Pattern cho maintainability
- ✅ **Data Driven Testing** - Test với JSON và Excel
- ✅ **Parallel Execution** - Chạy test song song
- ✅ **Thread-safe** - ThreadLocal cho parallel testing

---

## 📚 Tài Liệu Tham Khảo

- [Selenium WebDriver](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Allure Report](https://docs.qameta.io/allure/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
