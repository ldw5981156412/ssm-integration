package com.dwliu.ssmintegration.utils;

import java.util.Random;

/**
 * @author dwliu
 */
public class BytesUtil {
    /**
     * 字节数组转十六进制字符串
     *
     * @param bArray 字节数组
     * @return 十六进制内容
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将十六进制字符串转换成字节数组
     *
     * @param hex 十六进制字符串
     * @return byte数组
     */
    public static byte[] hexString2Bytes(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0, l = binary.length; i < l; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * 字符串转16进制字符串
     *
     * @param strPart 字符串
     * @return 16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * 16进制字符串转字符串
     *
     * @param src 16进制字符串
     * @return 字节数组
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return temp;
    }

    /**
     * 字符转成字节数据char-->integer-->byte
     *
     * @param src
     * @return
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    /**
     * 10进制数字转成16进制
     *
     * @param a   转化数据
     * @param len 占用字节数
     * @return
     */
    public static String intToHexString(int a, int len) {
        len <<= 1;
        StringBuffer hexString = new StringBuffer(Integer.toHexString(a));
        int b = len - hexString.length();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                hexString = hexString.append("0").append(hexString);
            }
        }
        return hexString.toString();
    }

    /**
     * 字节数组转换为二行制表示
     *
     * @param inStr 需要转换字节数组
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

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static byte[] generateRandomBytes() {
        Random random = new Random();
        byte[] result = new byte[16];
        random.nextBytes(result);
        return result;
    }

    /**
     * 字节数组异或
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static byte[] xor(byte[] arr1, byte[] arr2) {
        if (arr1 == null) {
            throw new RuntimeException("数组不能为空");
        }
        if (arr2 == null) {
            throw new RuntimeException("数组不能为空");
        }
        if (arr1.length != arr2.length) {
            throw new RuntimeException("两个字节数组长度不一致");
        }
        byte[] result = new byte[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = (byte) ((arr1[i] ^ arr2[i]) & 0xFF);
        }
        return result;
    }

    /**
     * @param arr1
     * @param arr2
     * @return
     */
    public static byte[] xor2(byte[] arr1, byte[] arr2) {
        if (arr1 == null){
            throw new RuntimeException("数组不能为空");
        }
        if (arr2 == null){
            throw new RuntimeException("数组不能为空");
        }
        byte[] result = new byte[arr1.length > arr2.length ? arr1.length : arr2.length];
        if (arr1.length > arr2.length) {
            for (int i = 0; i < arr2.length; i++) {
                result[i] = (byte) (arr1[i] ^ arr2[i]);
            }
        } else {
            for (int i = 0; i < arr1.length; i++) {
                result[i] = (byte) (arr1[i] ^ arr2[i]);
            }
        }
        return result;
    }

}
