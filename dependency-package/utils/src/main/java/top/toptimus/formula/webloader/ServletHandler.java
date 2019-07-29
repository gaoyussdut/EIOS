package top.toptimus.formula.webloader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet处理器
 *
 * @author gaoyu
 */
public interface ServletHandler {

    /**
     * 初始化
     *
     * @param servletConfig servlet配置
     * @throws ServletException
     */
    public void init(ServletConfig servletConfig) throws ServletException;

    /**
     * 服务
     *
     * @param request  Http请求
     * @param response Http响应
     * @param method   请求方式
     * @throws ServletException
     * @throws IOException
     */
    public void doService(HttpServletRequest request, HttpServletResponse response, String method)
            throws ServletException, IOException;

    /**
     * 销毁
     */
    public void destroy();
}
