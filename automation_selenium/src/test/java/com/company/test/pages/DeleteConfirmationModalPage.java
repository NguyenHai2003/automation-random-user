package com.company.test.pages;

import com.company.framework.keywords.WebUI;
import org.openqa.selenium.By;

public class DeleteConfirmationModalPage extends BasePage {

    public By modalContainer = By.xpath("//div[@role='alertdialog'] | //div[@role='dialog']");
    public By btnConfirmDelete = By.xpath("//button[@id='confirm-delete-btn'] | //button[contains(text(), 'Yes, Delete') or contains(text(), 'Confirm')]");
    public By btnCancel = By.xpath("//button[contains(text(), 'Cancel') and not(contains(@class, 'hidden'))]");

    public boolean isModalDisplayed() {
        return WebUI.isElementDisplayed(modalContainer);
    }

    public void clickYesDelete() {
        WebUI.clickElement(btnConfirmDelete);
    }

    public void clickCancel() {
        WebUI.clickElement(btnCancel);
    }
}
