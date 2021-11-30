package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CredentialsPage {

    public CredentialsPage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    // credentials Elements
    @FindBy(id="add_credential_modal")
    private WebElement add_credential_modal;



    @FindBy(id="credentialModal")
    private WebElement credentialModal;


    @FindBy(id="toggle_cred_pass")
    private WebElement togglePasswordAJAXButton;

    /* ######### Add modal ########### */
    @FindBy(id="new_credential_url")
    private WebElement new_credential_url;

    @FindBy(id="new_credential_username")
    private WebElement new_credential_username;

    @FindBy(id="new_credential_password")
    private WebElement new_credential_password;


    @FindBy(id="new_credential_save")
    private WebElement new_credential_save;


    /* ######### edit modal ########### */
    @FindBy(id="editCredentialModal")
    private WebElement editCredentialModal;

    @FindBy(id="edit_credential_id")
    private WebElement edit_credential_id;

    @FindBy(id="edit_credential_url")
    private WebElement edit_credential_url;

    @FindBy(id="edit_credential_username")
    private WebElement edit_credential_username;

    @FindBy(id="edit_credential_password")
    private WebElement edit_credential_password;

    @FindBy(id="edit_credential_save")
    private WebElement edit_credential_save;

    @FindBy(className="edit_credentials")
    private WebElement notesTitles;

    // professional way to get all elements instead of driver.findall outside the page
    @FindAll({
            @FindBy(className = "edit_credentials"),
            @FindBy(className = "btn")
    })
    private List<WebElement> edit_credentials;

    @FindAll({
            @FindBy(className = "delete_credentials"),
            @FindBy(className = "btn")
    })
    private List<WebElement> delete_credentials;


    @FindAll({
            @FindBy(className = "credentials_username")
    })
    private List<WebElement> credentials_usernames;

    @FindAll({
            @FindBy(className = "credentials_password")
    })
    private List<WebElement> credentials_passwords;


    @FindAll({
            @FindBy(className = "credentials_url")
    })
    private List<WebElement> credentials_urls;


    public List<String> getAllUserNamesStrings(){
        List<String> usernames = new ArrayList<>();
        for (WebElement credentials_username : credentials_usernames) {
            usernames.add(credentials_username.getText());
        }
        return usernames;
    }

    public List<String> getAllUserPasswordsStrings(){
        List<String> passwords = new ArrayList<>();
        for (WebElement credentials_password : credentials_passwords) {
            passwords.add(credentials_password.getText());
        }
        return passwords;
    }

    public List<String> getAllUserUrlsStrings(){
        List<String> urls = new ArrayList<>();
        for (WebElement credentials_url : credentials_urls) {
            urls.add(credentials_url.getText());
        }
        return urls;
    }

    public String toggleTheAJAXAdvancedBtn() throws InterruptedException {
        togglePasswordAJAXButton.click();
        Thread.sleep(1000);
        return edit_credential_password.getText();
    }
    public void openAddCredentialsModal(){
        add_credential_modal.click();
    }

    // save function to open edit by index or open any edit last one
    public void openEditCredentialsModal(int byIndex){
        WebElement firstEdit = null;
        int i=0;
        while (i<edit_credentials.size()) {
            firstEdit = edit_credentials.get(i);
            if (byIndex == i){
                firstEdit = edit_credentials.get(i);
                break;
            }
            i++;
        }
        System.out.println("Cuuuuuyuyuuuuyuyuurent zft edit" + edit_credentials.size());
        assert firstEdit != null;
        firstEdit.click();
    }

    // save function to delete by index or delete last one
    public void deleteElementByIndex(int byIndex){
        WebElement lastDelete = null;
        int i=0;
        while (i<delete_credentials.size()) {
            lastDelete = delete_credentials.get(i);
            if (byIndex == i){
                lastDelete = delete_credentials.get(i);
                break;
            }
            i++;
        }
        assert lastDelete != null;
        lastDelete.click();
    }

    public void fillAddCredentialsForm(String url, String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        new_credential_url.clear();
        new_credential_username.clear();
        new_credential_password.clear();
        new_credential_url.sendKeys(url);

        new_credential_username.sendKeys(username);
        new_credential_password.sendKeys(password);
        new_credential_save.click();
    }

    public void fillEditCredentialsForm(String url, String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        if (url != null) {
            edit_credential_url.clear();
            edit_credential_url.sendKeys(url);
        }
        if (username != null) {
            edit_credential_username.clear();
            edit_credential_username.sendKeys(username);
        }
        if (password != null) {
            edit_credential_password.clear();
            edit_credential_password.sendKeys(password);
        }
        edit_credential_save.click();
    }





    public void setCredentials_usernames(List<WebElement> credentials_usernames) {
        this.credentials_usernames = credentials_usernames;
    }

    public void setCredentials_passwords(List<WebElement> credentials_passwords) {
        this.credentials_passwords = credentials_passwords;
    }

    public void setCredentials_urls(List<WebElement> credentials_urls) {
        this.credentials_urls = credentials_urls;
    }

    public void setEdit_credentials(List<WebElement> edit_credentials) {
        this.edit_credentials = edit_credentials;
    }

    public void setDelete_credentials(List<WebElement> delete_credentials) {
        this.delete_credentials = delete_credentials;
    }

    /*encrypt functions */
    public String generateNewSecureSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String encryptThisValue(String data, String salt) {

        byte[] encryptedValue = null;
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(salt.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        }
        assert encryptedValue != null;
        System.out.println("The encrypted Password is : " + Base64.getEncoder().encodeToString(encryptedValue) + " the salt " + salt);
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

}
