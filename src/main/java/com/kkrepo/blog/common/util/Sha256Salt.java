package com.kkrepo.blog.common.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Sha256 + 盐   加密密码
 * @author WangRuofei
 * @create 2020-03-28 10:20
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class Sha256Salt {

    public static void main(String[] args) {
        System.out.println(sha256EncryptSalt("khkjdhfakjsdf"));
//        System.out.println(checkSha256EncryptSalt("74757464DCD8E8FAF0508E8D5D585A7A757D6DADAC0CF4FD0DC5C3537170002226A62027C7F5F8E8191555090474595A","dfgdfgdfg"));
//        System.out.println(checkSha256EncryptSalt("123","487D75F5373777111D9D7478E8BABB4B060E4EFEFFAF535010DCDC4C5F5E7E0808088886C66667C7060DADA2AFAF9A96"));
    }

    /**
     * 获取随机数(盐值)
     */
    private static String getSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            secureRandom.nextBytes(salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(b);
        }
        return bytes2Hex(salt);
    }

    /**
     * 生成sha256加盐加密
     * @param encryptStr
     * @return
     */
    public static String sha256EncryptSalt(String encryptStr) {
        String encryptCode = null;
        String salt = getSalt();
        byte[] bytes = (encryptStr + salt).getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            encryptCode = bytes2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return confusionSalt(salt, encryptCode);
    }

    /**
     * 检查密码是否正确
     * @param encryptStr
     * @param encryptCode
     * @return
     */
    public static boolean checkSha256EncryptSalt(String encryptStr, String encryptCode) {
        String salt = getSaltFromEncryptCode(encryptCode);
        byte[] bytes = (encryptStr + salt).getBytes();
        String encryptCodeNew = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            encryptCodeNew = bytes2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return confusionSalt(salt,encryptCodeNew).equals(encryptCode);
    }

    /**
     * 一个byte转为2个hex字符
     */
    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

    /**
     * 将盐和密文进行混淆
     * @param salt 盐 32位
     * @param encryptCode   密文 64位
     * @return
     */
    public static String confusionSalt(String salt, String encryptCode) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < salt.length(); i++) {
            result.append(salt.charAt(i));
            result.append(encryptCode.charAt(i));
            result.append(encryptCode.charAt(i + 1));
        }
        return result.toString();
    }

    /**
     * 从加盐密文中获取盐值
     * @param encryptCode
     * @return
     */
    public static String getSaltFromEncryptCode(String encryptCode) {
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < encryptCode.length(); i += 3) {
            salt.append(encryptCode.charAt(i));
        }
        return salt.toString();
    }
}
