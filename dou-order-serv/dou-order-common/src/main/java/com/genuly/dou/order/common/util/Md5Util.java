package com.genuly.dou.order.common.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String stringToMd5(String plainText) {
        byte[] secretBytes;
        try {
            byte[] byteText = plainText.getBytes(StandardCharsets.UTF_8);
            secretBytes = MessageDigest.getInstance("md5").digest(byteText);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        while (md5code.length() < 32) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }
}
