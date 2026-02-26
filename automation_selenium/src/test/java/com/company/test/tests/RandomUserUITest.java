package com.company.test.tests;

import com.company.framework.drivers.DriverManager;
import com.company.framework.keywords.WebUI;
import com.company.test.common.BaseTest;
import com.company.test.pages.DashboardPage;
import com.company.test.pages.DeleteConfirmationModalPage;
import com.company.test.pages.UserModalPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RandomUserUITest extends BaseTest {

    private DashboardPage dashboardPage;
    private UserModalPage userModalPage;
    private DeleteConfirmationModalPage deleteModalPage;

    @BeforeMethod
    public void setupPages() {
        dashboardPage = new DashboardPage();
        userModalPage = new UserModalPage();
        deleteModalPage = new DeleteConfirmationModalPage();
        dashboardPage.openDashboard();
    }

    @Test(priority = 1, description = "UI_01: Generate Users & Save to DB")
    public void testUI01_GenerateUsersAndSaveToDB() {
        dashboardPage.clickGenerateUsers()
                     .waitForSpinnerToDisappear();
        
        dashboardPage.viewUserDetail(1);
        WebUI.clickElement(By.xpath("//button[@id='save-user-btn']"));
                     
        String toast = dashboardPage.getToastMessage();
        Assert.assertTrue(toast.toLowerCase().contains("success") || toast.toLowerCase().contains("saved"), 
            "Success toast message should appear. Actual: " + toast);
        
        // Close modal
        try { WebUI.clickElement(By.xpath("//button[contains(text(), 'Close')]")); } catch(Exception e) {}
    }

    @Test(priority = 2, description = "UI_02: Prevent Multiple Saves (Double-click)")
    public void testUI02_PreventMultipleSaves() {
        dashboardPage.clickGenerateUsers()
                     .waitForSpinnerToDisappear();
        
        dashboardPage.viewUserDetail(1);
        WebUI.doubleClickElement(By.xpath("//button[@id='save-user-btn']"));

        String toast = dashboardPage.getToastMessage();
        Assert.assertNotNull(toast, "A toast notification should appear.");
        try { WebUI.clickElement(By.xpath("//button[contains(text(), 'Close')]")); } catch(Exception e) {}
    }

    @Test(priority = 3, description = "UI_03: View Detail Modal")
    public void testUI03_ViewDetailModal() {
        // Precondition: Ensure there is at least one user by generating them first
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        
        String expectedName = WebUI.getElementText(dashboardPage.tableNameFirstRow);
        dashboardPage.viewUserDetail(1);
        
        Assert.assertTrue(userModalPage.isModalDisplayed(), "User Detail modal should be displayed.");
        String modalName = userModalPage.getName();
        Assert.assertEquals(modalName, expectedName, "Modal name should match the table row data.");
        
        userModalPage.clickClose();
    }

    @Test(priority = 4, description = "UI_04: View Detail Modal - Responsive & Missing Image")
    public void testUI04_ViewDetailModalResponsive() {
        // Resize browser window to mobile view (e.g., 375x812)
        DriverManager.getDriver().manage().window().setSize(new Dimension(375, 812));
        
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        dashboardPage.viewUserDetail(1);
        
        Assert.assertTrue(userModalPage.isModalDisplayed(), "User detail modal should render responsively.");
        
        // Check for missing image fallback (either real profile img or default avatar)
        boolean hasImage = userModalPage.isProfileImageDisplayed();
        boolean hasFallback = userModalPage.isDefaultAvatarDisplayed();
        Assert.assertTrue(hasImage || hasFallback, "A profile image or default fallback avatar should be displayed.");
        
        userModalPage.clickClose();
    }

    @Test(priority = 5, description = "UI_05: Edit User - Happy Path")
    public void testUI05_EditUserHappyPath() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        dashboardPage.clickEditUser(1);
        
        String newName = "John Doe " + System.currentTimeMillis();
        userModalPage.updateName(newName);
        userModalPage.clickSave();
        
        String toast = dashboardPage.getToastMessage();
        Assert.assertTrue(toast.toLowerCase().contains("success") || toast.toLowerCase().contains("updated"), 
            "Success toast message should appear.");
            
        // Check table row is updated
        String updatedName = WebUI.getElementText(dashboardPage.tableNameFirstRow);
        Assert.assertTrue(updatedName.contains(newName), "Updated name should reflect immediately on the table row.");
    }

    @Test(priority = 6, description = "UI_06: Edit User - Validation Errors")
    public void testUI06_EditUserValidationErrors() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        dashboardPage.clickEditUser(1);
        
        userModalPage.clearRequiredFields();
        userModalPage.clickSave();
        
        Assert.assertTrue(userModalPage.isNameValidationErrorDisplayed(), 
            "HTML5/React validation prompts should appear under the blank fields.");
        Assert.assertTrue(userModalPage.isModalDisplayed(), "Form should not be submitted.");
        
        userModalPage.clickClose();
    }

    @Test(priority = 7, description = "UI_07: Delete User - Happy Path")
    public void testUI07_DeleteUserHappyPath() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        
        // Save first name to ensure it's removed
        String firstUserName = WebUI.getElementText(dashboardPage.tableNameFirstRow);
        
        dashboardPage.clickDeleteUser(1);
        Assert.assertTrue(deleteModalPage.isModalDisplayed(), "Delete confirmation modal should appear.");
        
        deleteModalPage.clickYesDelete();
        
        String toast = dashboardPage.getToastMessage();
        Assert.assertTrue(toast.toLowerCase().contains("success") || toast.toLowerCase().contains("deleted"), 
            "Success toast message should appear.");
            
        // Wait briefly for UI to update row count
        WebUI.executeJavaScript("return setTimeout(() => {}, 1000)"); 
        
        String newFirstUserName = WebUI.isElementDisplayed(dashboardPage.tableNameFirstRow) 
                                    ? WebUI.getElementText(dashboardPage.tableNameFirstRow) 
                                    : "";
                                    
        Assert.assertNotEquals(newFirstUserName, firstUserName, "The deleted user should be absent from the table.");
    }

    @Test(priority = 8, description = "UI_08: Delete User - Cancel")
    public void testUI08_DeleteUserCancel() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        
        String firstUserName = WebUI.getElementText(dashboardPage.tableNameFirstRow);
        dashboardPage.clickDeleteUser(1);
        
        deleteModalPage.clickCancel();
        try { Thread.sleep(1000); } catch (Exception e) {} // wait for radix out animation
        Assert.assertFalse(deleteModalPage.isModalDisplayed(), "Delete modal should close.");
        
        String currentFirstUserName = WebUI.getElementText(dashboardPage.tableNameFirstRow);
        Assert.assertEquals(currentFirstUserName, firstUserName, "The user record should still be present.");
    }

    @Test(priority = 9, description = "UI_09: Filter + Search Combination")
    public void testUI09_FilterAndSearchCombination() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear(); // Ensure data
        
        dashboardPage.searchUser("Ana")
                     .filterGender("female")
                     .filterCountry("Spain");
                     
        // Wait table updates logically based on React
        WebUI.executeJavaScript("return setTimeout(() => {}, 1000)"); 
        if(WebUI.isElementDisplayed(dashboardPage.tableNameFirstRow)) {
            String name = WebUI.getElementText(dashboardPage.tableNameFirstRow).toLowerCase();
            Assert.assertTrue(name.contains("ana"), "Rows should match 'Ana'");
        }
    }

    @Test(priority = 10, description = "UI_10: Filter/Search - No Matches")
    public void testUI10_FilterSearchNoMatches() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        dashboardPage.searchUser("XYZ123NonExistent");
        
        try { Thread.sleep(1000); } catch (Exception e) {} // wait for react
                     
        Assert.assertTrue(WebUI.isElementDisplayed(dashboardPage.emptyTableMessage), 
            "A user-friendly 'No Results Found' message should be shown.");
    }

    @Test(priority = 11, description = "UI_11: Pagination Navigation")
    public void testUI11_PaginationNavigation() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        
        if(!dashboardPage.isNextPageDisabled()) {
            dashboardPage.clickNextPage(); // Go to page 2
            
            // Depends on how your page indicator is shown
            // In a real app we might read the page indicator text: 
            String pageText = WebUI.getElementText(dashboardPage.pageIndicator);
            Assert.assertTrue(pageText.contains("2"), "Page indicator should update to 2.");
        }
    }

    @Test(priority = 12, description = "UI_12: Pagination Boundary States")
    public void testUI12_PaginationBoundaryStates() {
        dashboardPage.clickGenerateUsers().waitForSpinnerToDisappear();
        
        // On Page 1
        Assert.assertTrue(dashboardPage.isPreviousPageDisabled(), "'Previous' button should be disabled on Page 1.");
        
        // Go to last page
        while (!dashboardPage.isNextPageDisabled()) {
            dashboardPage.clickNextPage();
            WebUI.executeJavaScript("return setTimeout(() => {}, 500)");
        }
        
        Assert.assertTrue(dashboardPage.isNextPageDisabled(), "'Next' button should be disabled on the last page.");
    }
}
