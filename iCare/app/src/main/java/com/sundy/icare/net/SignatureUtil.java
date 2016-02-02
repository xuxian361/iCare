package com.sundy.icare.net;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sundy on 16/1/24.
 */
public class SignatureUtil {

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String encryptionAlgorithm = "SHA-1";

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 使用指定算法生成消息摘要，默认是md5
     *
     * @param strSrc  , a string will be encrypted; <br/>
     * @param encName , the algorithm name will be used, dafault to "MD5"; <br/>
     * @return
     */
    public static String digest(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytesToHexString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 根据device_info、token、lol以及时间戳来生成签名
     *
     * @param device_info
     * @param token
     * @param lol
     * @param millis
     * @return
     */
    public static String generateSignature(String device_info, String token, String lol,
                                           long millis) {
        String timestamp = String.valueOf(millis);
        String signature = null;
        if (!"".equals(token)) {
            List<String> srcList = new ArrayList<String>();
            srcList.add(timestamp);
            srcList.add(device_info);
            srcList.add(token);
            srcList.add(lol);
            Collections.sort(srcList);
            Collections.reverse(srcList);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < srcList.size(); i++) {
                sb.append(srcList.get(i));
            }
            signature = digest(sb.toString(), encryptionAlgorithm);
            srcList.clear();
            srcList = null;
        } else {
            List<String> srcList = new ArrayList<String>();
            srcList.add(timestamp);
            srcList.add(device_info);
            srcList.add(lol);
            Collections.sort(srcList);
            Collections.reverse(srcList);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < srcList.size(); i++) {
                sb.append(srcList.get(i));
            }
            signature = digest(sb.toString(), encryptionAlgorithm);
            srcList.clear();
            srcList = null;
        }
        return signature;
    }


}
