package common;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author: scyang
 * @program: ssm_super
 * @package: com.itheima.commom
 * @date: 2019-10-26 19:48:45
 * @describe:
 */
public class Md5Utils {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    private static final String RSA = "RSA";
    private static final String CHARSET_CODING = "UTF-8";
    private static final String SHA256withRSA = "SHA256withRSA";
    /***************************************************************************/
    private static final String MD5 = "MD5";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密明文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static String byteToHexString(byte b) {
        /**
         * @Description: 字节转换成字符串
         * @methodName: byteToHexString
         * @Param: [b]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/26 21:48
         */
        int n = b;
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    @Test
    public void test01() {
        System.out.println(byteToHexString((byte) 19));

    }

    private static String byteArrayToHexString(byte[] byteArray) {
        /**
         * @Description: 字节数组转换成字符串
         * @methodName: byteArrayToHexString
         * @Param: [b]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/26 22:28
         */
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            sb.append(byteToHexString(byteArray[i]));
        }
        return sb.toString();
    }

    @Test
    public void test02() {
        System.out.println(byteArrayToHexString(new byte[]{2, 5, 8}));
    }

    public static String MD5Encode(String origin) throws Exception {
        /**
         * @Description: 签名对象
         * @methodName: MD5Encode
         * @Param: [origin]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 21:25
         */
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        // md.update(origin.getBytes("UTF-8"));
        byte[] digest = md.digest(origin.getBytes(CHARSET_CODING));
        origin = byteArrayToHexString(digest);
        return origin;
    }

    @Test
    public void test03() throws Exception {
        System.out.println(MD5Encode("盛重阳"));
    }

    private static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        /**
         * @Description: 获取私钥
         * @methodName: getPrivateKey
         * @Param: [privateKeyStr]
         * @return: java.security.PrivateKey
         * @Author: scyang
         * @Date: 2019/10/28 22:30
         */
        byte[] keyBytes = Base64.decodeBase64(privateKeyStr.getBytes(CHARSET_CODING));

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    @Test
    public void test04() throws Exception {
        System.out.println(getPublicKey("-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmUWNmE0ksuRDcx5OscRn/ghCndouXqaxNFZM9lDPnmNzFSO2da4qsQEP/TH1TjGzPsrWL9roDhKkX4FfPP8pS9Gz72GBcVyExVvJ3CHFN3lBjRf2EzePL4iC6WpQzAZn/IQiFwLsJc8MjWgglb1PKva5tagBJ+lPUHAiWF8w/wLiT4exEiXZk0h59U1VvgsT8O155Z582R4J6GLkbwJ6Se2rNknFE1Q6BM7LGCWCpGjp/l73RzIPKjJPmAgpOTedAeZ7HKRTD/00b+C6qpWItJWG2Sme4vJGNOMJl4iIa8hRg6hewK1T9a/fIgZ/3Ln+1/k/E6a6C/LcVpR8G6RxNQIDAQAB-----END PUBLIC KEY-----"));
    }

    private static PublicKey getPublicKey(String publicKeyStr) throws Exception {
        /**
         * @Description: 获取公钥
         * @methodName: getPublicKey
         * @Param: [publicKeyStr]
         * @return: java.security.PublicKey
         * @Author: scyang
         * @Date: 2019/10/28 22:41
         */
        byte[] keyBytes = Base64.decodeBase64(publicKeyStr.getBytes(CHARSET_CODING));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    public static String getSHA256withRSA(String text, String privateKeyStr) throws Exception {
        /**
         * @Description: 获取私钥签名 text待签名的字符串  privateKeyStr私钥字符串
         * @methodName: getSHA256withRSA
         * @Param: [text, privateKeyStr]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2020/1/3 20:17
         */
        /** 获取SHA256withRSA签名 */
        Signature signature = Signature.getInstance(SHA256withRSA);
        /** 获取私钥 */
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        signature.initSign(privateKey);
        signature.update(text.getBytes(CHARSET_CODING));
        byte[] signedData = signature.sign();
        byte[] bytes = Base64.decodeBase64(signedData);
        return new String(bytes, CHARSET_CODING);
    }

    public static boolean getgetSHA256withRSA(String text,String signedData,String publicKeyStr) throws Exception {
        /**
         * @Description: 判断公钥签名 text待签名的字符串 signseData  privateKeyStr私钥字符串
         * @methodName: getgetSHA256withRSA
         * @Param: [text, signseData, publicKeyStr]
         * @return: boolean
         * @Author: scyang
         * @Date: 2020/1/3 20:33
         */
        /** 获取SHA256withRSA签名 */
        Signature signature = Signature.getInstance(SHA256withRSA);
        PublicKey publicKey = getPublicKey(publicKeyStr);
        signature.initVerify(publicKey);
        signature.update(text.getBytes(CHARSET_CODING));
       return signature.verify(signedData.getBytes(CHARSET_CODING));
    }
    private static String doSecure(byte[] plainTestByte, Cipher cipher, int length) throws Exception {
        /**
         * @Description: 对数据分段加解密
         * @methodName: doSecure
         * @Param: [plainTestByte, cipher, length]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 22:48
         */
        String secureDate;
        int inputLen = plainTestByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > length) {
                cache = cipher.doFinal(plainTestByte, offSet, length);
            } else {
                cache = cipher.doFinal(plainTestByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            offSet = offSet + length;
        }
        secureDate = out.toString(CHARSET_CODING);
        out.close();
        return secureDate;
    }

    public static String encryptBySHA256Private(String plainTest, String privateKeyStr) throws Exception {
        /**
         * @Description: 私钥加密
         * @methodName: encryptBySHA256Private
         * @Param: [plainTest, privateKeyStr]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 23:02
         */
        if (plainTest == null || privateKeyStr == null) {
            return null;
        }
        String encryptedData;
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        encryptedData = doSecure(plainTest.getBytes(), cipher, MAX_ENCRYPT_BLOCK);
        return encryptedData;
    }

    public static String encryptBySHA256Public(String plainTest, String publicKeyStr) throws Exception {
        /**
         * @Description: 公钥加密
         * @methodName: encryptBySHA256Public
         * @Param: [plainTest, privateKeyStr]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 23:13
         */
        if (plainTest == null || publicKeyStr == null) {
            return null;
        }
        String encryptedData;
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedData = doSecure(plainTest.getBytes(), cipher, MAX_ENCRYPT_BLOCK);
        return encryptedData;
    }

    public static String decryptBySHA256Private(String plainTest, String privateKeyStr) throws Exception {
        /**
         * @Description: 私钥解密
         * @methodName: decryptBySHA256Private
         * @Param: [plainTest, privateKeyStr]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 23:17
         */
        if (plainTest == null || privateKeyStr == null) {
            return null;
        }
        String decryptedData;
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedData = doSecure(plainTest.getBytes(), cipher, MAX_DECRYPT_BLOCK);
        return decryptedData;
    }

    public static String decryptBySHA256Public(String plainTest, String publicKeyStr) throws Exception {
        /**
         * @Description: 公钥解密
         * @methodName: decryptBySHA256Public
         * @Param: [plainTest, publicKeyStr]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2019/10/28 23:25
         */
        if (plainTest == null || publicKeyStr == null) {
            return null;
        }
        String decryptedData;
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        decryptedData = doSecure(plainTest.getBytes(), cipher, MAX_DECRYPT_BLOCK);
        return decryptedData;
    }
}

