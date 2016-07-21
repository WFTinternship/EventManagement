package com.workfront.internship.event_management.business;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public class HashGeneratorUtil {

    public static String generateHashString(String message) {

        String encryptedPassword = null;

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            encryptedPassword = convertByteArrayToHexString(hashedBytes);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}