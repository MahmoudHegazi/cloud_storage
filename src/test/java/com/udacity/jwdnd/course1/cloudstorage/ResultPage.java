package com.udacity.jwdnd.course1.cloudstorage;

import net.bytebuddy.implementation.bytecode.Throw;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class ResultPage {

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

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

    public String getSavedMsg(){
        return savedMessage.getText();
    }
    public String getErrorMsg() throws NullPointerException, NoSuchElementException {

        try {
            if (!Objects.equals(errorMessage.getText(), "")) {
                return errorMessage.getText();
            } else if (!Objects.equals(this.notSavedMessage.getText(), "")) {
                return notSavedMessage.getText();
            } else {
                return null;
            }
        } catch(NoSuchElementException ex){
            Throw NoSuchElementException;
        }
        return null;
    }

}
