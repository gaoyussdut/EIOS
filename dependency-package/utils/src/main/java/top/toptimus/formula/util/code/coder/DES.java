package top.toptimus.formula.util.code.coder;

import org.apache.commons.codec.binary.Base64;
import top.toptimus.formula.util.KeyGen;
import top.toptimus.formula.util.code.Coder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 基于DES算法编码/解码器
 *
 * @author gaoyu
 */
public class DES implements Coder {

    public String getAlgorithm() {
        return "DES";
    }


    public String encode(String data, String key) {
        try {
            String algorithm = getAlgorithm();

            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey securekey = keyFactory.generateSecret(desKey);

            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.ENCRYPT_MODE, securekey, random);

            byte[] result = c.doFinal(data.getBytes());
            return new String(Base64.encodeBase64(result));
        } catch (Exception ex) {
            return data;
        }
    }


    public String decode(String data, String key) {
        try {
            String algorithm = getAlgorithm();

            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey securekey = keyFactory.generateSecret(desKey);

            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.DECRYPT_MODE, securekey, random);

            byte[] result = Base64.decodeBase64(data.getBytes());
            return new String(c.doFinal(result));
        } catch (Exception ex) {
            return data;
        }
    }


    public String createKey() {
        return KeyGen.getKey(8);
    }
}
