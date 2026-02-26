package com.company.test.pages;

import com.company.framework.keywords.WebUI;
import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    private String URL = "http://localhost:3001";

    // Locators for actions
    public By btnGenerateUsers = By.xpath("//button[contains(., 'Generate Users') or @id='generate-users-btn']");
    public By iconLoadingSpinner = By.cssSelector(".animate-spin, svg.lucide-loader");
    // Reusing the Save behavior from user prompt logic, but since it doesn't exist in UI,
    // let's click the switch to Saved Users Tab as a proxy or just save if the logic is different
    public By btnSaveToDBFirstCard = By.xpath("//button[@role='tab' and text()='Saved Users']"); 
    public By btnSaveToDBAllCards = By.xpath("//button[@role='tab' and text()='Saved Users']");

    // Toast Messages
    public By toastMessage = By.cssSelector("li[data-state='open'], div[role='status']");

    // Locators for Filters and Search
    public By inputSearch = By.id("search-input");
    public By selectGender = By.id("gender-filter");
    public By inputCountry = By.id("country-filter");
    public By emptyTableMessage = By.xpath("//*[contains(text(), 'No users to display')] | //*[contains(., 'No users to display')]");

    // Locators for Table
    public By tableRows = By.cssSelector("tbody tr");
    public By tableNameFirstRow = By.xpath("(//td[contains(@class, 'font-medium')] | //h3[contains(@class, 'font-semibold')])[1]"); 

    // Pagination
    public By btnNextPage = By.xpath("//button[contains(text(), 'Next')] | //button[contains(., 'Next')]");
    public By btnPreviousPage = By.xpath("//button[contains(text(), 'Prev')] | //button[contains(., 'Prev')]");
    public By pageIndicator = By.xpath("//button[contains(@class, 'default') and string-length(text()) < 3]"); // Current page button

    // Table Actions for a specific row index (1-based)
    public By getEditIconLocator(int rowIndex) {
        return By.xpath("(//button[starts-with(@id, 'edit-btn-')])[" + rowIndex + "]");
    }

    public By getDeleteIconLocator(int rowIndex) {
        return By.xpath("(//button[starts-with(@id, 'delete-btn-')])[" + rowIndex + "]");
    }

    public By getViewRowLocator(int rowIndex) {
        return By.xpath("(//button[starts-with(@id, 'view-btn-')])[" + rowIndex + "]");
    }
    
    // Page Actions
    public DashboardPage openDashboard() {
        navigateTo(URL);
        return this;
    }

    public DashboardPage clickGenerateUsers() {
        WebUI.clickElement(btnGenerateUsers);
        return this;
    }

    public DashboardPage waitForSpinnerToDisappear() {
        // Simple explicit wait loop to wait for spinner invisible
        try {
            while (WebUI.isElementDisplayed(iconLoadingSpinner)) {
                 Thread.sleep(500);
            }
        } catch (Exception e) {}
        return this;
    }

    public DashboardPage clickSaveFirstUser() {
        WebUI.clickElement(btnSaveToDBFirstCard);
        return this;
    }

    public DashboardPage doubleClickSaveFirstUserRapidly() {
        // Double click
        WebUI.doubleClickElement(btnSaveToDBFirstCard);
        return this;
    }

    public String getToastMessage() {
        return WebUI.getElementText(toastMessage);
    }
    
    public DashboardPage viewUserDetail(int rowIndex) {
        WebUI.clickElement(getViewRowLocator(rowIndex));
        return this;
    }

    public DashboardPage clickEditUser(int rowIndex) {
        WebUI.clickElement(getEditIconLocator(rowIndex));
        return this;
    }

    public DashboardPage clickDeleteUser(int rowIndex) {
        WebUI.clickElement(getDeleteIconLocator(rowIndex));
        return this;
    }

    public DashboardPage searchUser(String text) {
        WebUI.setText(inputSearch, text);
        return this;
    }

    public DashboardPage filterGender(String gender) {
        WebUI.clickElement(selectGender);
        WebUI.clickElement(By.xpath("//div[@role='option']/*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + gender.toLowerCase() + "'] | //div[@role='option' and translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + gender.toLowerCase() + "']"));
        return this;
    }

    public DashboardPage filterCountry(String country) {
        WebUI.setText(inputCountry, country); 
        return this;
    }

    public DashboardPage clickNextPage() {
        WebUI.clickElement(btnNextPage);
        return this;
    }

    public DashboardPage clickPreviousPage() {
        WebUI.clickElement(btnPreviousPage);
        return this;
    }

    public boolean isPreviousPageDisabled() {
        return !WebUI.isElementEnabled(btnPreviousPage) || 
               Boolean.parseBoolean(WebUI.getAttribute(btnPreviousPage, "disabled")) ||
               "true".equals(WebUI.getAttribute(btnPreviousPage, "aria-disabled"));
    }

    public boolean isNextPageDisabled() {
        return !WebUI.isElementEnabled(btnNextPage) || 
               Boolean.parseBoolean(WebUI.getAttribute(btnNextPage, "disabled")) ||
               "true".equals(WebUI.getAttribute(btnNextPage, "aria-disabled"));
    }
}
