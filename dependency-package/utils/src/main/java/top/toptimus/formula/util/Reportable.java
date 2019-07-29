package top.toptimus.formula.util;

import org.w3c.dom.Element;

import java.util.Map;


/**
 * interface <code>Reportable</code>
 *
 * @author gaoyu
 * @since 1.3.0
 */
public interface Reportable {
    /**
     * 报告输出到XML
     *
     * @param xml XML节点
     */
    public void report(Element xml);

    /**
     * 报告输出到JSON
     *
     * @param json JSON节点
     */
    public void report(Map<String, Object> json);
}
