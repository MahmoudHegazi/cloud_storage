package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    private static String nextUrl = "";

    @FindBy(id = "initial_login_link")
    private WebElement initialLoginLink;

    @FindBy(id = "success_login_link")
    private WebElement successLoginLink;

    @FindBy(id = "success_message")
    private WebElement successMessage;

    @FindBy(id = "inputFirstName")
    private WebElement firstNameInput;

    @FindBy(id = "inputLastName")
    private WebElement lastNameInput;

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(id = "signup_btn")
    private WebElement signupBtn;

    @FindBy(id = "error_message")
    private WebElement errorMessage;


    public SignupPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void createUser(String fn, String ln, String un, String ps) throws InterruptedException {
        firstNameInput.clear();
        lastNameInput.clear();
        usernameInput.clear();
        passwordInput.clear();

        firstNameInput.sendKeys(fn);
        lastNameInput.sendKeys(ln);
        usernameInput.sendKeys(un);
        passwordInput.sendKeys(ps);
        Thread.sleep(1000);
        signupBtn.submit();
        System.out.println("Created User with Name: " + fn);
        System.out.println("Success: " + getSuccessMessage());
        System.out.println("Errors: " + getErrorMessage());
    }

    // check if any error messages
    public String getErrorMessage(){
        try{
            return errorMessage.getText();
        } catch(NoSuchElementException ee){
            return "";
        }
    }


    // if success message return it else return empty use try and catch to avoid errors
    public String getSuccessMessage(){
        try{
            return successMessage.getText();
        } catch(NoSuchElementException ee){
            return "";
        }
    }

    // click on login URL after login success (correct redirect after success)
    public void goToLoginAfterSuccess(){
        try{
            successLoginLink.click();
        } catch(NoSuchElementException ee){
            System.out.println("Could not find the Success Login link");
        }
    }
}
