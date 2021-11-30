package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private static HashService hashService;
    private static  UserService userService;
    private static CredentialsMapper credentialsMapper;

    public CredentialService(CredentialsMapper credentialsMapper){
        CredentialService.credentialsMapper = credentialsMapper;
     //   CredentialService.encryptionService = encryptionService;
    }



    public int getRandomPosition(String password){
        return (int)(Math.random() * password.length()-1);
    }

    public String mixUpPassword(String password, String passSalt,  int position){
        System.out.println(position);
        String part1 = password.substring(0, position);
        String part2 = password.substring(position);
        String finalPass = part1  + passSalt + part2;
        System.out.println(finalPass);
        return finalPass;
    }

    public String generateSecureSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String encryptData(String data, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.encryptValue(data, salt);
    }

    public String decryptData(String data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.decryptValue(data, key);
    }

    public void addCredentials(Credential credential){
        credentialsMapper.insert(credential);
    }
    public void deleteCredentials(Integer credential_id){
        credentialsMapper.remove(credential_id);
    }
    public void editCredentials(Credential credential){
        credentialsMapper.update(credential);
    }

    public Credential getUserCredentialById(Integer credential_id, Integer user_id){
        return credentialsMapper.getUserCredential(credential_id, user_id);
    }

    public List<Credential> getUserCredentials(Integer user_id) {
        return credentialsMapper.getUserCredentials(user_id);
    }

}
