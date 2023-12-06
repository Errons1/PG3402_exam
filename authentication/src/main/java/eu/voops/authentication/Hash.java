package eu.voops.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static byte[] sha256(String password) throws NoSuchAlgorithmException {
//        Not the strongest nor the slowest for password. 
//        Also know I need a salt in here.
//        But this exam is for microservices not safety, might come back here later!
//        TODO: Make this better if you have time
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
//        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
//        return new String(hash, StandardCharsets.UTF_8);
    }
}
