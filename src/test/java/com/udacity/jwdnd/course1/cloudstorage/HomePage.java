package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import java.util.List;

public class HomePage {
    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    @FindBy(id="logoutbtn")
    private WebElement logoutBtn;

    // nav menu links
    @FindBy(id="nav-files-tab")
    private WebElement filesTab;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    /* --------- credentials end ----- */

    // files Inputs
    @FindBy(id="fileUpload")
    private WebElement fileUploadInput;

    @FindBy(id="upload-file-btn")
    private WebElement uploadFileBtn;


    /* --------- files end ----- */




    public void logout(){
        logoutBtn.submit();
    }

    public void signOut(){
        logoutBtn.submit();
    }
    public void gotToTab(String tabName){
        switch (tabName) {
            case "notes" -> notesTab.click();
            case "credentials" -> credentialsTab.click();
            default -> filesTab.click();
        }
    }




}
