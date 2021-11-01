package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResultPage {
    // messages
    @FindBy(id = "saved_message")
    private WebElement savedMessage;

    @FindBy(id = "not_saved_message")
    private WebElement notSavedMessage;

    @FindBy(id = "error_message")
    private WebElement errorMessage;

    // links
    @FindBy(id = "backHome_saved")
    private WebElement backHomeSavedLink;

    @FindBy(id = "backHome_notSaved")
    private WebElement backHomeNotSavedLink;

    @FindBy(id = "backHome_message")
    private WebElement backHomeMessageLink;
}
