package com.wgcisotto.blockchain.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


/**
 * The class convert String into SHA256 (Secure Hash Algorithm)
 *
 * The SHA-256 algorithm generates an almost unique, fixed-size 256-bit (32-byte) hash.
 * This is a one-way function, so the result cannot be decrypted back to the original value.
 *
 * https://www.baeldung.com/sha-256-hashing-java
 *
 * @author williamcisotto
 */
//@Slf4j //TODO: add logger
public class SHA256Helper {


    /**
     * Returns a SHA256 in hexadecimal
     * Here we have to use a custom byte to hex converter to get the hashed value in hexadecimal
     */
    public static String hash(String data){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8)); // UTF-8 Unicode Transformation Format - 8-bit
            StringBuilder hexadecimalString = new StringBuilder();
            for (int i = 0; i < hash.length; ++i) {
                String hexadecimal = Integer.toHexString(0xff & hash[i]);
                //padding
                if(hexadecimal.length() == 1) {
                    hexadecimalString.append("0");
                }
                hexadecimalString.append(hexadecimal);
            }
            return hexadecimalString.toString();
        } catch (NoSuchAlgorithmException e) {
            //TODO: throw custom exception
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
