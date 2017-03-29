package com.hyperchain.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具包
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/13 下午11:23
 */
public class SHA1Util {
    /**
     * 获取字符串的sha1表示
     *
     * @param convertStr 需要转换的值
     * @return sha1编码后的字符串，小写
     */
    public static String getSHA1String(String convertStr) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            // digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(convertStr.toString().getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }
}
