package com.sminedata.eric.demo.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.zaxxer.hikari.HikariConfig;

import org.apache.commons.codec.binary.Base64;

public class SmineDESUtil {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DESede";
    /**
     * 加密算法/解密算法/工作模式/填充方式
     */
    private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";

    /**  
     * 生成随机密钥
     * 
     * @return
     * @throws Exception
     */
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(new SecureRandom());
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    /**
     * 生成固定密钥
     * 
     * @param seed
     * @return
     * @throws Exception
     */
    public static SecretKey generateKey(byte[] seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(new SecureRandom(seed));
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    /**
     * 生成固定密钥
     * 
     * @param password
     * @return
     * @throws Exception
     */
    public static SecretKey generateKey(String password) throws Exception {
        return generateKey(password.getBytes());
    }

    /**
     * 执行加密
     * key长度必须为8位,即64bit
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String str, byte[] key) throws Exception {
        byte[] content = str.getBytes();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行加密
     * key长度必须为8位,即64bit
     * @param content
     * @param  key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行加密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] content, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(password));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行解密
     * key长度必须为8位,即64bit
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行解密
     * key长度必须为8位,即64bit
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(String str, byte[] key) throws Exception {
        byte[] content = str.getBytes();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行解密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] content, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(password));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行解密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(String str, String password) throws Exception {
        byte[] content = str.getBytes();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(password));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * To String
     * 
     * @param data
     * @return
     */
    private static String toString(byte[] data) {
        return new String(data);
    }
    /**
     * get Properties File By FilePath
     * @param filePath
     * @return
     * @throws IOException
     */
    private static Properties getPropertiesFileByFilePath(String filePath) throws IOException {
        Properties prop = new Properties();
        InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
        prop.load(InputStream);
        return prop;
    }
    /**
     * 解密Properties
     * @param filePath
     * @param password
     * @return
     * @throws Exception
     */
    public static HikariConfig decodeProperties(String filePath, String password) throws Exception {
        Properties config = getPropertiesFileByFilePath(filePath);
        SecretKey encodekey = generateKey(password);
        Set<Object> keys = config.keySet();
        for (Object propkey : keys) {
            // System.out.println("解密后: " + propkey.toString() + "="+ new String(decrypt(Base64.decodeBase64(config.get(propkey).toString()), encodekey.getEncoded())));
            config.setProperty(propkey.toString(),new String(decrypt(Base64.decodeBase64(config.get(propkey).toString()), encodekey.getEncoded())));
        }
        // TODO Logger
        return new HikariConfig(config);
    }
}