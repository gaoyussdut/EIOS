package top.toptimus.formula.webloader;

import top.toptimus.formula.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * A Properties Wrapper for HttpServletRequest.
 *
 * @author Administrator
 * @version 1.0.1
 */
public class ServletRequestProperties extends Properties {

    protected HttpServletRequest request = null;

    /**
     * Constructor
     *
     * @param req instance of {@code HttpServletRequest}
     */
    public ServletRequestProperties(HttpServletRequest req) {
        request = req;
    }


    protected void _SetValue(String _name, String _value) {
        //do nothing
    }


    protected String _GetValue(String _name) {
        return request != null ? request.getParameter(_name) : "";
    }


    public void Clear() {
        //do nothing
    }

}
