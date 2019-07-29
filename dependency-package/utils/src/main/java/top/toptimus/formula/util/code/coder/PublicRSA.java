package top.toptimus.formula.util.code.coder;

import top.toptimus.formula.util.KeyGen;
import top.toptimus.formula.util.code.Coder;
import top.toptimus.formula.util.code.util.RSAUtil;

/**
 * 基于RSA采用公钥加密/解密
 *
 * @author gaoyu
 */
public class PublicRSA implements Coder {
    public String encode(String data, String key) {
        return RSAUtil.encryptWithPublicKey(data, key);
    }

    public String decode(String data, String key) {
        return RSAUtil.decryptWithPublicKey(data, key);
    }

    public boolean verify(String data, String key, String signData) {
        return RSAUtil.verify(data, key, signData);
    }

    public String createKey() {
        return KeyGen.getKey(8);
    }
}