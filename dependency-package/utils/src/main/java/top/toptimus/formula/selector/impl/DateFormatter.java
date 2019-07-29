package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;
import top.toptimus.formula.util.XmlTools;

/**
 * 日期格式化器
 *
 * @author gaoyu
 * @since 1.5.2
 */
public class DateFormatter extends Selector {


    protected Selector selector = null;
    protected String pattern = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS";

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        pattern = PropertiesConstants.getString(_p, "pattern", pattern, true);

        Element _selector = XmlTools.getFirstElementByPath(_e, "selector");
        if (_selector == null) {
            selector = Selector.newInstance(_e, _p, SingleField.class.getName());
        } else {
            selector = Selector.newInstance(_selector, _p);
        }
    }

    public String onSelect(DataProvider _dataProvider) {
        String value = selector.select(_dataProvider);
        long t = Long.parseLong(value);
        return String.format(pattern, t);
    }
}
