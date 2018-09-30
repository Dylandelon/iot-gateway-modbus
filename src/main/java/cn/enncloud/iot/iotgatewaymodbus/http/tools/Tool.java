package cn.enncloud.iot.iotgatewaymodbus.http.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thinkpad on 2018/9/25.
 */
public class Tool {
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }
    public static String getMobileNO(String mobiles) {
        Pattern p = Pattern.compile("1\\d{10}");
        Matcher m = p.matcher(mobiles);
        if(m.find()) {
            return m.group(0);
            }
        return "";
    }
    public static byte[] SC_Tea_Encryption(byte[] plainText, byte[] key)
    {
        int i;
        byte j = 13;                           // 秘钥不从零开始使用
        byte[] cipherText = new byte[plainText.length];
        for (i = 0; i < plainText.length; i++, j++)               // 单次循环加秘密数据
        {
            cipherText[i] = (byte)(plainText[i] ^ key[j&0x0f]);
            cipherText[i] = (byte)(cipherText[i] ^ j);
        }
        return cipherText;
    }
    public static byte[] SC_Tea_Decryption(byte[] cipherText, byte[] key)
    {
        int i;
        byte j = 13;
        byte[] plainText = new byte[cipherText.length];
        for (i = 0; i < cipherText.length; i++, j++)
        {
            plainText[i] = (byte)(cipherText[i] ^ j);
            plainText[i] = (byte)(plainText[i] ^ key[j & 0x0f]);
        }
        return plainText;
    }
    public static String SC_Tea_Encryption_Str(String plainText, String key) {
        byte[] plainbytes=toBytes(plainText);
        byte[] keybytes=key.getBytes();
        byte[] cipherbytes=SC_Tea_Encryption(plainbytes,keybytes);
        String  cipherText=bytesToHex(cipherbytes);
        return cipherText;
    }
    public static String SC_Tea_Decryption_Str(String cipherText, String key) {
        byte[] plainbytes=toBytes(cipherText);
        byte[] keybytes=key.getBytes();
        byte[] cipherbytes=SC_Tea_Decryption(plainbytes,keybytes);
        String  plainText=bytesToHex(cipherbytes);
        return plainText;
    }
    public static void main(String args[]) {
        String phonenum=getMobileNO("asfsa15712456891");
        System.out.println("phonenum:" + phonenum);
        String cipherText=SC_Tea_Encryption_Str("747365743137363130313335333833000A5714B300","2018091200000000");
//        String cipherText=SC_Tea_Encryption_Str("010300000001840A","2018091200000000");
        System.out.println("cipherText:" + cipherText);
        String plainText=SC_Tea_Encryption_Str(cipherText,"2018091200000000");
//        String plainText=SC_Tea_Encryption_Str(cipherText,"2018091200000000");
        System.out.println("plainText:" + plainText);
    }


}
