package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;

/**
 * 常量选择器
 *
 * @author gaoyu
 * @since 1.5.2
 */
public class Constants extends Selector {


    protected String value = "0";

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        value = PropertiesConstants.getString(_p, "selector-value", value, true);
    }

    public String onSelect(DataProvider _dataProvider) {
        return value;
    }
}
