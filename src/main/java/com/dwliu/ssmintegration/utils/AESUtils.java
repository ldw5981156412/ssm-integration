package com.dwliu.ssmintegration.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES-128-CBC加密
 *
 * @author dwliu
 * @create-time 2018年5月24日 下午2:35:43
 */
public class AESUtils {

    /**
     * AES-128-CBC对称加密
     *
     * @param content
     *            加密的明文
     * @param secretKey
     *            密钥
     * @param iv
     *            CBC向量，长度为16的byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String secretKey, byte[] iv) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(secretKey.getBytes("UTF-8"));
        SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 算法参数
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, skc, paramSpec);
        byte[] input = content.getBytes("UTF-8");
        int len = input.length;
        byte[] cipherText = new byte[cipher.getOutputSize(len)];
        int ctLength = cipher.update(input, 0, len, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return byte2hex(cipherText);
    }

    /**
     * AES-128-CBC对称解密
     *
     * @param encrypted
     *            解密的密文(16进制，hex)
     * @param secretKey
     *            密钥
     * @param iv
     *            CBC向量，长度为16的byte数组
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String encrypted, String secretKey, byte[] iv) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(secretKey.getBytes("UTF-8"));
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 算法参数
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
        byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
        return new String(clearbyte);
    }

    /**
     * 将十六进制字符串转换成字节数组
     *
     * @param hex
     *            十六进制字符串
     * @return byte数组
     */
    public static byte[] toByte(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0, l = binary.length; i < l; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * 字节数组转换为二行制表示
     *
     * @param inStr
     *            需要转换字节数组
     * @return 字节数组的二进制表示
     */
    public static String byte2hex(byte[] inStr) {
        StringBuilder out = new StringBuilder(inStr.length * 2);
        for (int i = 0, l = inStr.length; i < l; i++) {
            // 字节做"与"运算，去除高位置字节 11111111
            if (((int) inStr[i] & 0xff) < 0x10) {
                // 小于十前面补零
                out.append("0");
            }
            out.append(Long.toString((int) inStr[i] & 0xff, 16));
        }
        return out.toString();
    }

    private static final byte[] handleData(byte[] password, byte[] iv, byte[] content, int mode) {
        try {
            SecretKeySpec key = new SecretKeySpec(password, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] secretkey, byte[] ivParameter, byte[] data) {
        return handleData(secretkey, ivParameter, data, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt(byte[] secretkey, byte[] ivParameter, byte[] data) {
        return handleData(secretkey, ivParameter, data, Cipher.DECRYPT_MODE);
    }
}
