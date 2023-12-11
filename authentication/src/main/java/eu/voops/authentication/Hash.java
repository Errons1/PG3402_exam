package eu.voops.authentication;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Hash {
    public static byte[] sha256(String password) {
//        Not the strongest nor the slowest for password. 
//        Also know I need a salt in here.
//        But this exam is for microservices not safety, might come back here later!
//        TODO: Make this better if you have time
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not Hash password!");
            e.printStackTrace();
            return null;
        }
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}
