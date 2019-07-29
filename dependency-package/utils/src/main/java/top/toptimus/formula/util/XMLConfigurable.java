package top.toptimus.formula.util;

import org.w3c.dom.Element;

/**
 * Can read config from a XML document.
 *
 * @author gaoyu
 * @version 1.6.4.27 [20160125 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 */
public interface XMLConfigurable {
    /**
     * to read config from xml.
     *
     * @param e xml document
     * @param p variables
     */
    public void configure(Element e, Properties p);
}
