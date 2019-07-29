package top.toptimus.formula.webloader;

import org.apache.commons.lang3.StringUtils;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.Settings;

import javax.servlet.ServletConfig;

/**
 * A Properties wrapper for ServletConfig.
 *
 * @author gaoyu
 * @version 1.6.8.13 [gaoyu 20170427] <br>
 * - 指定Settings为父节点 <br>
 */
public class ServletConfigProperties extends Properties {

    protected ServletConfig servletConfig = null;

    public ServletConfigProperties(ServletConfig sc) {
        super("ServletConfig", Settings.get());
        servletConfig = sc;
    }

    protected void _SetValue(String _name, String _value) {
        //do nothing
    }


    protected String _GetValue(String _name) {
        if (servletConfig == null)
            return "";
        String value = servletConfig.getInitParameter(_name);
        return StringUtils.isEmpty(value) ? servletConfig.getServletContext().getInitParameter(_name) : value;
    }


    public void Clear() {
        // do nothing

    }

}
