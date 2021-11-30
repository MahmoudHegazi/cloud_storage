package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "login_button")
    private WebElement loginButton;

    @FindBy(id = "invalid_credentials")
    private WebElement invalidCredentials;

    @FindBy(id = "signup_btn")
    private WebElement signupBtn;

    @FindBy(id = "logout_message")
    private WebElement logoutMessage;

    @FindBy(id = "success-signup-message")
    private WebElement successSignupMessage;



    public void logIn(String un, String ps){
        inputUsername.clear();
        inputPassword.clear();
        inputUsername.sendKeys(un);
        inputPassword.sendKeys(ps);
        loginButton.submit();
        System.out.println("Error Message: " + getErrorMsg());
    }

    public String getErrorMsg(){
        try{
            return invalidCredentials.getText();
        } catch(NoSuchElementException er) {
            return null;
        }
    }

    public String getSuccessSignupMessage(){
        return successSignupMessage.getText();
    }
}
