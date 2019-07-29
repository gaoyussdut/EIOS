package top.toptimus.formula.util.code.coder;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import top.toptimus.formula.util.KeyGen;
import top.toptimus.formula.util.code.Coder;


/**
 * 基于AES的编码/解码器
 *
 * @author gaoyu
 */
public class AES implements Coder {

    public String getAlgorithm() {
        return "AES";
    }


    public String encode(String data, String key) {
        try {
            String algorithm = getAlgorithm();
            KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
            keygen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = keygen.generateKey();

            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] result = c.doFinal(data.getBytes());
            return new String(Base64.encodeBase64(result));
        } catch (Exception ex) {
            return data;
        }
    }


    public String decode(String data, String key) {
        try {
            byte[] result = Base64.decodeBase64(data.getBytes());

            String algorithm = getAlgorithm();
            KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
            keygen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = keygen.generateKey();

            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(c.doFinal(result));
        } catch (Exception ex) {
            return data;
        }
    }


    public String createKey() {
        return KeyGen.getKey(8);
    }
}
