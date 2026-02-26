# 🚀 HƯỚNG DẪN ÁP DỤNG FRAMEWORK VÀO DỰ ÁN MỚI

## 📋 Tổng Quan

Framework hiện tại là **Base Framework** có thể tái sử dụng cho nhiều dự án web khác nhau. Khi tham gia vào một dự án cụ thể, bạn **KHÔNG CẦN** thay đổi core framework, chỉ cần **customize** các phần liên quan đến dự án.

---

## ✅ KHÔNG CẦN THAY ĐỔI (Core Framework)

### 1. Framework Core Classes

### 2. Base Test Classes

### 3. Base Page Classes

## 🔧 CUSTOMIZE (Project-Specific)

### 1. Configuration Files ⚙️

#### `src/test/resources/configs/config.properties`

```properties
# Cần thay đổi theo dự án
BROWSER_TYPE = Chrome
HEADLESS = false
AUTO_SETUP_DRIVER = true

# Timeouts
TIMEOUT_EXPLICIT_DEFAULT = 10
STEP_ACTION_TIMEOUT = 1

# Screenshots
SCREENSHOT_FAIL = true
SCREENSHOT_PASS = true
SCREENSHOT_ALL_STEP = true
SCREENSHOT_PATH = exports/screenshots/

# Test Data Paths
JSON_DATA_FILE_PATH = src/test/resources/test_data/data.json
EXCEL_DATA_FILE_PATH = src/test/resources/test_data/data.xlsx
```

---

### 2. Test Data 📊

#### `src/test/resources/test_data/data.json`

```json
{
  "login": {
    "validUser": {
      "username": "your_test_user",
      "password": "your_test_password"
    },
    "invalidUser": {
      "username": "invalid_user",
      "password": "wrong_password"
    }
  },
  "testData": {
    "productName": "Your Product",
    "price": "100000",
    "baseUrl": "https://yourproject.com"
  }
}
```

#### `src/test/resources/test_data/data.xlsx`

- Tạo sheets và data theo dự án
- Cấu trúc columns theo test cases
- Ví dụ: Login data, Product data, User data, etc.

---

### 3. Page Objects 📄

#### Tạo Pages mới cho dự án

**Ví dụ: LoginPage**

```java
package com.company.test.pages;

import com.company.test.pages.BasePage;
import com.company.framework.helpers.LocatorHelper;
import com.company.framework.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // Locators cho dự án cụ thể
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    // Methods cho dự án cụ thể
    public void enterUsername(String username) {
        WebUI.setText(LocatorHelper.byId("username"), username);
    }

    public void enterPassword(String password) {
        WebUI.setText(LocatorHelper.byCss("input[type='password']"), password);
    }

    public void clickLogin() {
        WebUI.clickElement(LocatorHelper.byId("loginButton"));
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void navigateToLoginPage() {
        WebUI.navigateToUrl("https://yourproject.com/login");
    }
}
```

**Ví dụ: HomePage với Hover**

```java
package com.company.test.pages;

import com.company.test.pages.BasePage;
import com.company.framework.helpers.LocatorHelper;
import com.company.framework.keywords.WebUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(id = "menu")
    private WebElement menuButton;

    @FindBy(css = ".dropdown-item")
    private WebElement dropdownItem;

    public void hoverMenu() {
        WebUI.hoverElement(LocatorHelper.byId("menu"));
    }

    public void clickDropdownItem() {
        WebUI.clickElement(LocatorHelper.byCss(".dropdown-item"));
    }

    public void selectMenuItem() {
        hoverMenu();
        clickDropdownItem();
    }
}
```

**Ví dụ: ProductPage với Scroll**

```java
package com.company.test.pages;

import com.company.test.pages.BasePage;
import com.company.framework.helpers.LocatorHelper;
import com.company.framework.keywords.WebUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {

    @FindBy(id = "addToCart")
    private WebElement addToCartButton;

    @FindBy(id = "footer")
    private WebElement footer;

    public void scrollToFooter() {
        WebUI.scrollToElement(LocatorHelper.byId("footer"));
    }

    public void addToCart() {
        WebUI.scrollToElement(LocatorHelper.byId("addToCart"));
        WebUI.clickElement(LocatorHelper.byId("addToCart"));
    }
}
```

---

### 4. Test Cases 🧪

#### Tạo Test Cases mới cho dự án

**Ví dụ: LoginTest**

```java
package com.company.test.testcases;

import com.company.test.common.BaseTest;
import com.company.test.pages.LoginPage;
import com.company.framework.helpers.JsonHelpers;
import com.company.framework.keywords.WebUI;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage();

        // Navigate to login page
        loginPage.navigateToLoginPage();

        // Get test data from JSON
        String username = JsonHelpers.getValueJsonObject("login", "validUser", "username");
        String password = JsonHelpers.getValueJsonObject("login", "validUser", "password");

        // Perform login
        loginPage.login(username, password);

        // Assertions
        String currentUrl = WebUI.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard"), "Login should redirect to dashboard");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();

        String username = JsonHelpers.getValueJsonObject("login", "invalidUser", "username");
        String password = JsonHelpers.getValueJsonObject("login", "invalidUser", "password");

        loginPage.login(username, password);

        // Assert error message appears
        String errorText = WebUI.getElementText(com.company.framework.helpers.LocatorHelper.byCss(".error-message"));
        Assert.assertNotNull(errorText, "Error message should be displayed");
    }
}
```

**Ví dụ: HomePageTest với Hover**

```java
package com.company.test.testcases;

import com.company.test.common.BaseTest;
import com.company.test.pages.HomePage;
import com.company.framework.keywords.WebUI;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test
    public void testMenuHover() {
        WebUI.navigateToUrl("https://yourproject.com");

        HomePage homePage = new HomePage();
        homePage.selectMenuItem();

        // Assert navigation happened
        String currentUrl = WebUI.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("menu-item"), "Should navigate to menu item");
    }
}
```

**Ví dụ: ProductTest với Scroll và Frame**

```java
package com.company.test.testcases;

import com.company.test.common.BaseTest;
import com.company.test.pages.ProductPage;
import com.company.framework.helpers.LocatorHelper;
import com.company.framework.keywords.WebUI;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {

    @Test
    public void testAddToCart() {
        WebUI.navigateToUrl("https://yourproject.com/products");

        ProductPage productPage = new ProductPage();
        productPage.addToCart();

        // Verify cart updated
        String cartCount = WebUI.getElementText(LocatorHelper.byId("cartCount"));
        Assert.assertEquals(cartCount, "1", "Cart count should be 1");
    }

    @Test
    public void testScrollToFooter() {
        WebUI.navigateToUrl("https://yourproject.com/products");

        ProductPage productPage = new ProductPage();
        productPage.scrollToFooter();

        // Verify footer is visible
        boolean footerVisible = WebUI.isElementDisplayed(LocatorHelper.byId("footer"));
        Assert.assertTrue(footerVisible, "Footer should be visible after scroll");
    }
}
```

---

### 5. TestNG XML Suites 📋

#### Tạo TestNG XML cho dự án

**Ví dụ: `src/test/resources/suites/YourProject_Chrome.xml`**

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Your Project - Chrome Suite" verbose="1">
    <listeners>
        <listener class-name="com.company.test.listeners.TestListener"/>
        <listener class-name="com.company.test.listeners.AllureListener"/>
    </listeners>

    <test name="Chrome Tests">
        <parameter name="browserType" value="Chrome"/>
        <parameter name="headless" value="false"/>
        <parameter name="autoSetupDriver" value="true"/>

        <classes>
            <class name="com.company.test.testcases.LoginTest"/>
            <class name="com.company.test.testcases.HomePageTest"/>
            <class name="com.company.test.testcases.ProductTest"/>
        </classes>
    </test>
</suite>
```

**Ví dụ: `src/test/resources/suites/YourProject_Firefox.xml`**

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Your Project - Firefox Suite" verbose="1">
    <listeners>
        <listener class-name="com.company.test.listeners.TestListener"/>
        <listener class-name="com.company.test.listeners.AllureListener"/>
    </listeners>

    <test name="Firefox Tests">
        <parameter name="browserType" value="Firefox"/>
        <parameter name="headless" value="false"/>
        <parameter name="autoSetupDriver" value="true"/>

        <classes>
            <class name="com.company.test.testcases.LoginTest"/>
            <class name="com.company.test.testcases.HomePageTest"/>
        </classes>
    </test>
</suite>
```

**Ví dụ: `src/test/resources/suites/YourProject_AllBrowsers.xml` - Parallel Execution**

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Your Project - All Browsers Suite" parallel="tests" thread-count="3" verbose="1">
    <listeners>
        <listener class-name="com.company.test.listeners.TestListener"/>
        <listener class-name="com.company.test.listeners.AllureListener"/>
    </listeners>

    <test name="Chrome Tests">
        <parameter name="browserType" value="Chrome"/>
        <parameter name="headless" value="false"/>
        <classes>
            <class name="com.company.test.testcases.LoginTest"/>
        </classes>
    </test>

    <test name="Firefox Tests">
        <parameter name="browserType" value="Firefox"/>
        <parameter name="headless" value="false"/>
        <classes>
            <class name="com.company.test.testcases.LoginTest"/>
        </classes>
    </test>

    <test name="Edge Tests">
        <parameter name="browserType" value="Edge"/>
        <parameter name="headless" value="false"/>
        <classes>
            <class name="com.company.test.testcases.LoginTest"/>
        </classes>
    </test>
</suite>
```

---

## 📁 Cấu Trúc Thư Mục Cho Dự Án Mới

```
YourProject/
├── src/main/java/com/company/framework/    ✅ GIỮ NGUYÊN (Core Framework)
│
├── src/test/
│   ├── java/com/company/test/
│   │   ├── common/                         ✅ GIỮ NGUYÊN (Base Test)
│   │   │   └── BaseTest.java
│   │   ├── pages/
│   │   │   ├── BasePage.java               ✅ GIỮ NGUYÊN
│   │   │   ├── LoginPage.java              🔧 TẠO MỚI (Project Pages)
│   │   │   ├── HomePage.java               🔧 TẠO MỚI
│   │   │   ├── ProductPage.java            🔧 TẠO MỚI
│   │   │   └── ...                         🔧 TẠO MỚI
│   │   └── testcases/
│   │       ├── LoginTest.java              🔧 TẠO MỚI
│   │       ├── HomePageTest.java           🔧 TẠO MỚI
│   │       ├── ProductTest.java            🔧 TẠO MỚI
│   │       └── ...                         🔧 TẠO MỚI
│   │
│   └── resources/
│       ├── configs/
│       │   └── config.properties           🔧 CUSTOMIZE
│       ├── suites/
│       │   ├── YourProject_Chrome.xml       🔧 TẠO MỚI
│       │   ├── YourProject_Firefox.xml     🔧 TẠO MỚI
│       │   └── YourProject_AllBrowsers.xml 🔧 TẠO MỚI
│       └── test_data/
│           ├── data.json                    🔧 CUSTOMIZE
│           └── data.xlsx                    🔧 CUSTOMIZE
```

---

## 🎯 Quy Trình Áp Dụng Framework Vào Dự Án Mới

### Bước 1: Copy Framework Base

```bash
# Copy toàn bộ framework vào project mới
# Hoặc clone từ repository
```

### Bước 2: Cấu Hình Dự Án

1. ✅ Update `config.properties` với config của dự án
   - Browser type mặc định
   - Timeouts
   - Screenshot paths
   - Test data paths

### Bước 3: Tạo Test Data

1. ✅ Tạo `data.json` với test data của dự án
   - Login credentials
   - Test data cho các features
   - URLs và configuration data
2. ✅ Tạo `data.xlsx` với test data (nếu cần)
   - Data sheets cho từng module
   - Test data cho data-driven testing

### Bước 4: Tạo Page Objects

1. ✅ Tạo các Page classes extend từ BasePage
2. ✅ Define locators cho từng page
   - Sử dụng `@FindBy` annotations
   - Hoặc sử dụng `LocatorHelper` trong methods
3. ✅ Implement methods cho từng page
   - Actions: click, setText, hover, scroll, etc.
   - Business logic methods

### Bước 5: Tạo Test Cases

1. ✅ Tạo test classes extend từ BaseTest
2. ✅ Implement test methods
3. ✅ Sử dụng Page Objects và WebUI Keywords
4. ✅ Sử dụng test data từ JSON/Excel
5. ✅ Thêm assertions

### Bước 6: Tạo TestNG Suites

1. ✅ Tạo XML files cho từng suite
   - Suite cho từng browser
   - Suite cho parallel execution
2. ✅ Configure parameters
   - Browser type
   - Headless mode
   - Other configurations
3. ✅ Add test classes

---

## 📝 Tóm Tắt

| Component      | Action            | Lý Do                            |
| -------------- | ----------------- | -------------------------------- |
| Core Framework | ✅ **GIỮ NGUYÊN** | Tái sử dụng cho mọi dự án web    |
| Base Test      | ✅ **GIỮ NGUYÊN** | Generic, dùng được cho mọi dự án |
| Base Page      | ✅ **GIỮ NGUYÊN** | Base class, extend cho dự án     |
| Config Files   | 🔧 **CUSTOMIZE**  | Dự án cụ thể                     |
| Test Data      | 🔧 **CUSTOMIZE**  | Dữ liệu test của dự án           |
| Page Objects   | 🔧 **TẠO MỚI**    | Pages của dự án                  |
| Test Cases     | 🔧 **TẠO MỚI**    | Test cases của dự án             |
| TestNG Suites  | 🔧 **TẠO MỚI**    | Cấu hình test cho dự án          |

---
