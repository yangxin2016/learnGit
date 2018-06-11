package com.security.wrr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String getMd5CodeByStr(String str){
        byte[] keywordByte = str.getBytes();
        String md5code="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(keywordByte);
            BigInteger bigInt = new BigInteger(1, md5.digest());
            md5code = bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5code;
    }

    public static String getMd5CodeByBuffer(byte[] buffer){
        String md5code="";
        MessageDigest md5 = null;
			try{
                md5 = MessageDigest.getInstance("MD5");
                md5.update(buffer);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("生成md5失败！");
			}
	        BigInteger bigInt = new BigInteger(1, md5.digest());
			md5code = bigInt.toString(16);
        return md5code;
    }
}
