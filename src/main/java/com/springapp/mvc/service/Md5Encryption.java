package com.springapp.mvc.service;

import org.springframework.security.core.codec.Hex;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 2/8/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Md5Encryption {


    public static String encryptUsingMd5(String password) throws Exception {
        String hash = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes("UTF8"));
            byte[] raw = messageDigest.digest();
            hash = new String(Hex.encode(raw));
        }
        catch (Exception e){

        }
        return hash;
    }
}
