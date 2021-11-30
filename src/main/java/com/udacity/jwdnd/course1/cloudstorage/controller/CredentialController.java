package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsEditForm;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.udacity.jwdnd.course1.cloudstorage.CloudStorageApplication.viewPasswordApiKeys;

@Controller
public class CredentialController {
    public final UserService userService;
    public final EncryptionService encryptionService;
    private static CredentialService credentialService;

    public CredentialController(UserService userService,EncryptionService encryptionService, CredentialService credentialService){
        this.encryptionService = encryptionService;
        this.userService = userService;
        CredentialController.credentialService = credentialService;
    }

    public static class SecureAJAXCredentials{
        private String id;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        //Getters and Setters
    }

    public static class SecureAJAXViewCredentials{
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
        //Getters and Setters
    }


    // this route for ajax toggle password view mode secured Note Implemented CSRF and token

    @PostMapping("/credentials/view_pass")
    public String ViewPassCredentials(Model model,@RequestBody SecureAJAXViewCredentials reqData, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        assert reqData.getId() != null;
        assert reqData.getType() != null;
        // ajax req cred id


        Integer credentials_id = Integer.valueOf(reqData.getId());

        String encodedSalt = credentialService.generateSecureSalt();
        User loggedUser = userService.getUser(authentication.getName());
        assert loggedUser.getUserId() != null;

        Credential credential = credentialService.getUserCredentialById(credentials_id, loggedUser.getUserId());


        if (!Objects.equals(loggedUser.getUserId(), credential.getUser_id())){
            AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
            mapper.get().put("status", "false");
            mapper.get().put("message", "Can not access the data");
            return "jsonTemplate";
        }
        /* create json response to sent */
        HashMap<String, String> mapper = new HashMap<>();
        mapper.put("status", "success");
        mapper.put("message", "Here is the password.");
        mapper.put("id", String.valueOf(credential.getCredentials_id()));


        // next-type make js and java connected first it ask to show then it give it the type to not forget and async type but async java js


        // toggle between encrypted and unencrypted with js and with java
        mapper.put("about", "java js html feature async");
        if (Objects.equals(reqData.getType(), "decrypt")){
            mapper.put("next_type", "encrypt");
            mapper.put("cred_pass", credentialService.decryptData(credential.getPassword(), credential.getSecret_key()));
        } else {
            mapper.put("next_type", "decrypt");
            mapper.put("cred_pass", credential.getPassword());
        }

        System.out.println(mapper);
        ///mapper.put("hashpass", credentialService.encryptData(credential.getPassword(), credential.getSecret_key()));
        model.addAttribute("data", mapper);
        return "jsonTemplate";
    }


    // get credentials data using post and aax
    @PostMapping("/credentials/edit_view")
    public String ViewPassCredentials(Model model,@RequestBody SecureAJAXCredentials reqData, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        assert reqData.getId() != null;
        // ajax req cred id

        Integer credentials_id = Integer.valueOf(reqData.getId());

        String encodedSalt = credentialService.generateSecureSalt();
        User loggedUser = userService.getUser(authentication.getName());

        Credential credential = credentialService.getUserCredentialById(credentials_id, loggedUser.getUserId());


        if (!Objects.equals(loggedUser.getUserId(), credential.getUser_id())){
            AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
            mapper.get().put("status", "false");
            mapper.get().put("message", "Can not access the data");
            return "jsonTemplate";
        }
        /* create json response to sent */
        HashMap<String, String> mapper = new HashMap<>();
        mapper.put("status", "success");
        mapper.put("id", String.valueOf(credential.getCredentials_id()));
        mapper.put("uname", credential.getUname());
        mapper.put("password", credential.getPassword());
        mapper.put("url", credential.getUrl());

        System.out.println(mapper);
        ///mapper.put("hashpass", credentialService.encryptData(credential.getPassword(), credential.getSecret_key()));
        model.addAttribute("data", mapper);
        return "jsonTemplate";
    }


    @PostMapping("/encrypt/string")
    public String hidePasswordSecurely(Model model,@RequestParam Integer id, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String encodedSalt = credentialService.generateSecureSalt();
        User loggedUser = userService.getUser(authentication.getName());

        Credential credential = credentialService.getUserCredentialById(id, loggedUser.getUserId());
        if (!Objects.equals(loggedUser.getUserId(), credential.getUser_id())){
            AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
            mapper.get().put("status", "false");
            mapper.get().put("message", "Nothing Is here");
            return "jsonTemplate";
        }
        System.out.println("Request Data is" + id.toString());
        HashMap<String, String> mapper = new HashMap<>();
        String put = mapper.put("status", "success");
        mapper.put("hashpass", credentialService.encryptData(credential.getPassword(), credential.getSecret_key()));
        model.addAttribute("data", mapper);
        return "jsonTemplate";
    }

    // Add  Credentials
    @PostMapping("/credential/add")
    public String addCredential(@ModelAttribute("CredentialsForm") CredentialsForm credentialsForm, Authentication authentication, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        User loggedUser = userService.getUser(authentication.getName());

        String encodedSalt = credentialService.generateSecureSalt();
        String encryptedPassword = credentialService.encryptData(credentialsForm.getPassword(), encodedSalt);

        Credential credential = new Credential(null,credentialsForm.getUrl(), credentialsForm.getUsername(), encodedSalt, encryptedPassword, loggedUser.getUserId());
        System.out.println(credentialService.decryptData(encryptedPassword,encodedSalt));

        credentialService.addCredentials(credential);
        System.out.println(credential.getCredentials_id());
        System.out.println(credential.getUname());
        model.addAttribute("success_message", "New Credentials Added With id:" + credential.getCredentials_id());
        //System.out.println(encryptionService.encryptValue("hi","&&#Y^T&FF#&^F&f76f7&767671625rfsafasfsacfc_-=21=-2-`/../1.1.xd"));
        //model.addAttribute("error_message", "hi");
        return "result";
    }

    // delete Credentials
    @PostMapping("/credentials/{id}/delete")
    public String deleteCredentials(Model model, @RequestBody SecureAJAXCredentials reqData, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NullPointerException {
        assert reqData.getId() != null;
        // ajax req cred id
        Integer credentials_id = Integer.valueOf(reqData.getId());

        User loggedUser = userService.getUser(authentication.getName());
        Credential credential = credentialService.getUserCredentialById(credentials_id, loggedUser.getUserId());

        if (credential == null){
            AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
            mapper.get().put("status", "false");
            mapper.get().put("message", "credential Not Found");
            return "jsonTemplate";
        }

        if (!Objects.equals(loggedUser.getUserId(), credential.getUser_id())){
            AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
            mapper.get().put("status", "false");
            mapper.get().put("message", "Can not access the data" + loggedUser.getUserId() + " , "  + credential.getUser_id());
            return "jsonTemplate";
        }
        /* create json response to sent */
        String credential_url = credential.getUrl();
        credentialService.deleteCredentials(credential.getCredentials_id());
        AtomicReference<HashMap<String, String>> mapper = new AtomicReference<>(new HashMap<>());
        mapper.get().put("status", "success");
        mapper.get().put("message", "Deleted Credentials With for url: " + credential_url + "Successfully.");
        //add the data to json template model
        model.addAttribute("data", mapper);
        return "jsonTemplate";
    }

    // edit Credentials
    @PostMapping("/credentials/{id}/update")
    public String updateCredentials(Model model, @ModelAttribute("editCredentialModal") CredentialsEditForm credentialsEditForm, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NullPointerException {
        assert credentialsEditForm.getEdit_credential_id() != null;
        assert credentialsEditForm.getEdit_credential_password() != null;
        assert credentialsEditForm.getEdit_credential_url() != null;
        assert credentialsEditForm.getEdit_credential_username() != null;
        // get cred data

        User loggedUser = userService.getUser(authentication.getName());
        Credential credential = credentialService.getUserCredentialById(credentialsEditForm.getEdit_credential_id(), loggedUser.getUserId());

        if (credential == null){
            model.addAttribute("error_message", "did not find the credential with id " + credentialsEditForm.getEdit_credential_id());
            return "result";
        }
        if (!Objects.equals(loggedUser.getUserId(), credential.getUser_id())){
            model.addAttribute("error_message", "can not update the data not belong to you");
            return "result";
        }

        if (!Objects.equals(credentialsEditForm.getEdit_credential_url(), credential.getUrl())){
            credential.setUrl(credentialsEditForm.getEdit_credential_url());
        }
        if (!Objects.equals(credentialsEditForm.getEdit_credential_username(), credential.getUname())){
            credential.setUname(credentialsEditForm.getEdit_credential_username());
        }
        if (!Objects.equals(credentialsEditForm.getEdit_credential_password(), credential.getPassword())) {

            // encrypt the password
            String encodedSalt = credentialService.generateSecureSalt();
            String encryptedPassword = credentialService.encryptData(credentialsEditForm.getEdit_credential_password(), encodedSalt);
            credential.setPassword(encryptedPassword);
            credential.setSecret_key(encodedSalt);
        }

        /* create json response to sent */

        credentialService.editCredentials(credential);
        model.addAttribute("success_message", "Edited Credentials successfully with id "+ credential.getCredentials_id());
        return "result";
    }

}
