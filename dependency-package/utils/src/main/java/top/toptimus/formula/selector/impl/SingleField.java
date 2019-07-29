package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;


/**
 * 单字段选择器
 *
 * @author gaoyu
 * @version 1.6.0.5 [20141114 gaoyu] <br>
 * - 如果没有定义selector-field属性，则取selector-id属性.<br>
 * @since 1.5.2
 */
public class SingleField extends Selector {


    protected Object context = null;
    protected String fieldName;

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        fieldName = PropertiesConstants.getString(_p, "selector-field", id, true);
    }

    public String onSelect(DataProvider _dataProvider) {
        if (context == null) {
            context = _dataProvider.getContext(fieldName);
        }

        if (context != null) {
            return _dataProvider.getValue(fieldName, context, getDefaultValue()).trim();
        }

        return getDefaultValue();
    }
}
