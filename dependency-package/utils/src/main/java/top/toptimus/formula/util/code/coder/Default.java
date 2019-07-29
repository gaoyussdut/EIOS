package top.toptimus.formula.util.code.coder;

import top.toptimus.formula.util.code.Coder;


/**
 * 缺省的编码/解码器
 * <p>
 * <br>
 * <p>
 * 缺省状态下
 *
 * @author gaoyu
 */
public class Default implements Coder {


    public String encode(String data, String key) {
        return data;
    }


    public String decode(String data, String key) {
        return data;
    }


    public String createKey() {
        return "";
    }
}
