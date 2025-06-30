package ex03_rsa;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;

public class CipherRSA {

    static Cipher cipher; // 암호 객체
    static PrivateKey privateKey; // 개인키
    static PublicKey publicKey; // 공개키

    static {
        try {
            /*
                RSA : 암호화 알고리즘
                ECB : 블럭 암호화
                PKCS1Padding : Padding 방식
             */
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // KeyPairGenerator : 2개의 키를 생성 
            KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = key.generateKeyPair(); // 쌍인 키 객체 생성
            privateKey = keyPair.getPrivate(); // 개인키 
            publicKey = keyPair.getPublic(); // 공개키
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String plain1) {
        byte[] cipherMsg = new byte[1024];

        try {
//          cipher.init(Cipher.ENCRYPT_MODE, privateKey); // 암호화 모드. 개인키로 암호화
            cipher.init(Cipher.ENCRYPT_MODE, publicKey); // 암호화 모드. 공개키로 암호화
            cipherMsg = cipher.doFinal(plain1.getBytes()); // 암호화
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteToHex(cipherMsg); // byte[] => 16진수코드값의 문자열
    }

    private static String byteToHex(byte[] cipherMsg) {
        if  (cipherMsg == null) {
            return null;
        }
        String str = "";
        for (byte b : cipherMsg) {
            str += String.format("%02x", b);
        }
        return str;
    }

    public static String decrypt(String cipher1) { // 복호화
        byte[] plainMsg = new byte[1024];
        try {
//          cipher.init(Cipher.DECRYPT_MODE, publicKey); // 복호화 모드. 공개키로 복호화
            cipher.init(Cipher.DECRYPT_MODE, privateKey); // 복호화 모드. 개인키로 복호화
            // hexToByte : 16진수 코드 값의 문자열 => byte[]
            plainMsg = cipher.doFinal(hexToByte(cipher1.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(plainMsg).trim();
    }

    private static byte[] hexToByte(String str) {
        if (str == null || str.length() < 2) {
            return null;
        }
        int len = str.length() / 2;
        byte[] buf = new byte[len];
        for (int i = 0; i < len; i++) {
            buf[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return buf;
    }

    private static byte[] makeKey(String key) {
        int len = key.length();
        char ch = 'A';
        for(int i=len; i< 16; i ++) {
            key += ch++;
        }
        return key.substring(0,16).getBytes();
    }

    private static void getKey(String key) throws Exception{
        Key genKey = new SecretKeySpec(makeKey(key),"AES");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("key.ser"));
        out.writeObject(genKey); // key
        out.flush();
        out.close();
    }
    
    public static String makehash(String userid) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] plain = userid.getBytes();
        byte[] hash = md.digest(plain);
        return byteToHex(hash);
    }
    public static  void getKey() { // 키생성. 파일에 저장
        try {
            KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
            // 1024 에서 2048 비트 키가 커지면 속도는 2 1024승 만큼 보안 강화됨.
            key.initialize(2048); // 키크기를 2048비트로 생성
            // 키의 크기가 큰 경우 : 보안에 좋다. 해독이 어렵다
            //                     암호/복호화에 속도가 느려짐
            KeyPair keyPair = key.generateKeyPair();
            PrivateKey priKey = keyPair.getPrivate();
            PublicKey pubKey = keyPair.getPublic();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privatekey.ser"));
            out.writeObject(priKey);
            out.flush();
            out.close();
            out = new ObjectOutputStream(new FileOutputStream("publickey.ser"));
            out.writeObject(pubKey);
            out.flush();
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicKey() { // 파일에서 공개키 읽어 오기
        ObjectInputStream ois = null;
        PublicKey pubKey = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("publickey.ser"));
            pubKey = (PublicKey) ois.readObject();
            ois.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return pubKey;
    }
    public static PrivateKey getPrivateKey() { // 개인키 
        ObjectInputStream ois = null;
        PrivateKey priKey = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("privatekey.ser"));
            priKey = (PrivateKey) ois.readObject();
            ois.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return priKey;
    }
    public static String encrypt(String org,int menu1) {
        byte[] cipherMsg = new byte[1024];
        try{
            // menu1 = 1=> 기밀문서. 공개키로 암호화
            if (menu1 == 1) {
                cipher.init(Cipher.ENCRYPT_MODE,getPublicKey());
            }else {
                // menu1 = 2=> 본인작성 문서. 개인키로 암호화
                cipher.init(Cipher.ENCRYPT_MODE,getPrivateKey());
            }
            cipherMsg = cipher.doFinal(org.getBytes());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return byteToHex(cipherMsg);
    }
    public static String decrypt(String cipherMsg,int menu1) {
        byte[] plainMsg = new byte[1024];
        try{
            if (menu1 == 1) {
                // menu1 = 1=> 기밀문서. 개인키로 복호화
                cipher.init(Cipher.DECRYPT_MODE,getPrivateKey());
            }else {
                // // menu1 = 2=> 본인작성 문서. 공개키로 복호화
                cipher.init(Cipher.DECRYPT_MODE,getPublicKey());
            }
            plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(plainMsg).trim(); //복호화된 문자열
    }
}