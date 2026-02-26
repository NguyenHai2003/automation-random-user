package com.company.test.pages;

import com.company.framework.keywords.WebUI;
import org.openqa.selenium.By;

public class UserModalPage extends BasePage {

    // Locators
    public By modalContainer = By.xpath("//div[@role='dialog'] | //div[contains(@class, 'modal-content')]");
    public By inputName = By.xpath("//input[@id='firstName' or @name='firstName']"); // Using firstName instead
    public By inputEmail = By.xpath("//input[@id='email' or @name='email']");
    public By imgProfile = By.xpath("//img[contains(@class, 'rounded-full') or @alt='Profile'] | //span[contains(@class, 'relative flex')]//img");
    public By defaultAvatarIcon = By.xpath("//span[contains(@class, 'flex h-full w-full')]"); // fallback
    public By btnSave = By.xpath("//button[@id='save-user-btn' or @id='update-user-btn']");
    public By btnClose = By.xpath("//button[contains(text(), 'Close') or contains(text(), 'Cancel')] | //button[@aria-label='Close']");
    
    // Validation Errors
    public By validationErrorName = By.xpath("//p[contains(@class, 'text-red')] | //span[contains(@class, 'text-red')]");

    public boolean isModalDisplayed() {
        return WebUI.isElementDisplayed(modalContainer);
    }

    public String getName() {
        if(WebUI.isElementDisplayed(inputName)) {
           return WebUI.getAttribute(inputName, "value");
        }
        return WebUI.getElementText(By.xpath("//h2[contains(@class, 'text-2xl')]")); 
    }

    public String getEmail() {
        return WebUI.getAttribute(inputEmail, "value");
    }

    public boolean isProfileImageDisplayed() {
        return WebUI.isElementDisplayed(imgProfile);
    }

    public boolean isDefaultAvatarDisplayed() {
        return WebUI.isElementDisplayed(defaultAvatarIcon);
    }

    public void updateName(String newName) {
        WebUI.setText(inputName, newName);
    }

    public void clearRequiredFields() {
        // Send backspaces to trigger React's onChange event for validation
        WebUI.clickElement(inputName);
        WebUI.setText(inputName, " "); // A single space fails the `.trim()` check
        WebUI.clickElement(inputEmail);
        WebUI.setText(inputEmail, " ");
    }

    public void clickSave() {
        WebUI.clickElement(btnSave);
    }

    public void clickClose() {
        WebUI.clickElement(btnClose);
    }

    public boolean isNameValidationErrorDisplayed() {
        return WebUI.isElementDisplayed(validationErrorName);
    }
}
