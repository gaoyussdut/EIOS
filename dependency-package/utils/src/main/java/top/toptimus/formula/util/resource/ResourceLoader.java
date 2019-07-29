package top.toptimus.formula.util.resource;

import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.URLocation;

import java.io.InputStream;
import java.net.URL;


/**
 * 资源装入接口
 *
 * @author gaoyu
 */
public interface ResourceLoader {
    /**
     * 装入资源
     *
     * @param _url     资源路径
     * @param _context 上下文
     * @return 输入数据流
     * @throws BaseException
     */
    public InputStream load(URLocation _url, Object _context) throws BaseException;

    /**
     * 生成资源的标准URL
     *
     * @param _url     URL
     * @param _context 上下文
     * @throws BaseException
     * @return　资源对应的URL
     */
    public URL createURL(URLocation _url, Object _context) throws BaseException;
}