package top.toptimus.formula.util.code.coder;

import top.toptimus.formula.util.KeyGen;
import top.toptimus.formula.util.code.Coder;
import top.toptimus.formula.util.code.util.RSAUtil;

/**
 * 基于RSA采用私钥加密/解密
 *
 * @author gaoyu
 */
public class PrivateRSA implements Coder {
    public String encode(String data, String key) {
        return RSAUtil.encryptWithPrivateKey(data, key);
    }

    public String decode(String data, String key) {
        return RSAUtil.decryptWithPrivateKey(data, key);
    }

    public String sign(String data, String key) {
        return RSAUtil.sign(data, key);
    }

    public String createKey() {
        return KeyGen.getKey(8);
    }
}