package com.example.guru.pa;

/**
 * Created by Crystal on 2016/7/21.
 */

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES3Utils {
    //定义加密算法
    private static final String Algorithm = "Desede";
    // 加密密钥
    private static final String PASSWORD_CRYPT_KEY = "qwertyuiop";
    // 加密 src为源数据字符串
    public static String encryptMode(String src) {
        try {// 生成密钥
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return byte2Hex(c1.doFinal(src.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //解密函数,src为密文字符串
    public static String decryptMode(String src) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return new String(c1.doFinal(hex2Byte(src)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据字符串生成密钥24位的字节数组
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    public static String byte2Hex(byte[] b){
        String hs = "";
        String stmp = "";
        for (int n = 0;n < b.length; n++){
            stmp = (java.lang.Integer.toHexString(b[n]&0xFF));
            if(stmp.length() == 1){
                hs = hs + "0" +stmp;
            }else {
                hs = hs +stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2Byte(String str){
        if(str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if(len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len/2];
        try{
            for (int i=0; i<str.length();i+=2){
                b[i/2]=(byte)Integer.decode("0x"+str.substring(i,i+2)).intValue();
            }
            return b;
        }catch (Exception e){
            return null;
        }
    }
}