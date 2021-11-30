package com.udacity.jwdnd.course1.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService {
    private final Logger logger = LoggerFactory.getLogger(EncryptionService.class);


    // I fixed this function and make it accept direct the secret key I generated in above function
    public String encryptValue(String data, String salt) {

        byte[] encryptedValue = null;
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(salt.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }
        assert encryptedValue != null;
        System.out.println("The encrypted Password is : " + Base64.getEncoder().encodeToString(encryptedValue) + " the salt " + salt);
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public String decryptValue(String data, String salt) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        byte[] decryptedValue = null;
        try{
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                SecretKey secretKey = new SecretKeySpec(salt.getBytes(), "AES");

                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                decryptedValue = cipher.doFinal(Base64.getDecoder().decode(data));

            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                logger.error(e.getMessage());
            }


        assert decryptedValue != null;
        System.out.println("The decryptedValue Password is : " + new String(decryptedValue) + " the salt " + salt);
        return new String(decryptedValue);
    }
}
