package top.toptimus.formula.util.compress.compressor;

import top.toptimus.formula.util.compress.AbstractCompressor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 基于GZIP的压缩/解压器
 * <p>
 * <br>采用Java内置GZIP实现。
 *
 * @author gaoyu
 * @version 1.6.4.17 [20151216 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 * @since 1.0.11
 */
public class GZIP extends AbstractCompressor {

    @Override
    protected InputStream getInputStream(InputStream in) throws Exception {
        return new GZIPInputStream(in);
    }

    @Override
    protected OutputStream getOutputStream(OutputStream out) throws Exception {
        return new GZIPOutputStream(out);
    }
}