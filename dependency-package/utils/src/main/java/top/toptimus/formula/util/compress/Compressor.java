package top.toptimus.formula.util.compress;

import top.toptimus.formula.util.Factory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 压缩/解压器
 *
 * @author gaoyu
 * @version 1.6.4.17 [20151216 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 * @since 1.0.11
 */
public interface Compressor {
    /**
     * 压缩bytes
     *
     * @param data 数据
     * @return 压缩后的数据
     * @throws Exception
     */
    public byte[] compress(byte[] data) throws Exception; // NOSONAR

    /**
     * 解压bytes
     *
     * @param data 压缩数据
     * @return 原始数据
     * @throws Exception
     */
    public byte[] decompress(byte[] data) throws Exception; // NOSONAR

    /**
     * 压缩
     *
     * @param in  输入流
     * @param out 输出流
     * @throws Exception
     */
    public void compress(InputStream in, OutputStream out) throws Exception;// NOSONAR

    /**
     * 解压
     *
     * @param in  输入流
     * @param out 输出流
     * @throws Exception
     */
    public void decompress(InputStream in, OutputStream out) throws Exception;// NOSONAR


    public static class TheFatory extends Factory<Compressor> {
        public TheFatory(ClassLoader cl) {
            super(cl);
        }

        @Override
        public String getClassName(String module) {
            if (module.indexOf('.') >= 0) {
                return module;
            }
            return "com.anysoft.util.compress.compressor." + module;
        }
    }
}
