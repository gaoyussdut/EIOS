package top.toptimus.formula.util.code.coder;

import org.apache.commons.codec.binary.Base64;
import top.toptimus.formula.util.KeyGen;
import top.toptimus.formula.util.code.Coder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * SHA1 加密
 *
 * @author gaoyu
 * @since 1.0.13
 */
public class SHA1 implements Coder {
    public String getAlgorithm() {
        return "sha-1";
    }

    public String encode(String data, String key) {
        try {
            MessageDigest m = MessageDigest.getInstance(getAlgorithm());
            String content = data + key;
            m.update(content.getBytes());
            byte result[] = m.digest();
            return new String(Base64.encodeBase64(result));
        } catch (NoSuchAlgorithmException e) {
            return data;
        }
    }


    public String decode(String data, String key) {
        return data;
    }


    public String createKey() {
        return KeyGen.getKey(8);
    }

}