package common;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    /**
     * 将字符串加密为md5码
     *
     * @param str
     * @return
     */
    public static String md5Encoder(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = ((byte) charArray[i]);
            }

            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = md5Bytes[i] & 0xFF;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * md5二次加密
     *
     * @param str
     *            要二次加密的字符串
     * @return
     */
    public static String md5EncoderSquare(String str) {
        return md5Encoder(md5Encoder(str));
    }

    /**
     * 将加密后的md5串加盐，再加密，目的是使安全性更高
     *
     * @param md5Str
     *            要加密的字符串
     * @param salt
     *            盐
     * @return
     */
    public static String md5EncoderWithSalt(String md5Str, String salt) {
        return md5Encoder(md5Str + salt);
    }
   @Test
    public void test(){
       System.out.println(md5Encoder("盛重阳"));
   }
}
