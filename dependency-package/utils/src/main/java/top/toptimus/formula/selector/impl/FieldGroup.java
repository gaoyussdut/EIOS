package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.FieldList;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;

/**
 * 字段组选择器
 *
 * @author gaoyu
 * @since 1.5.2
 */
public class FieldGroup extends Selector {


    protected String seperator = "|";
    protected boolean noSeperator = false;
    protected FieldList fieldList = null;

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        seperator = PropertiesConstants.getString(_p, "seperator", seperator, true);
        noSeperator = PropertiesConstants.getBoolean(_p, "noSeperator", noSeperator, true);
        fieldList = new FieldList();
        fieldList.configure(_e, _p);
    }

    public String onSelect(DataProvider _dataProvider) {
        StringBuffer buffer = new StringBuffer();
        int index = 0;
        Selector[] fields = fieldList.getFields();
        for (Selector field : fields) {
            buffer.append(field.select(_dataProvider));
            index++;
            if (!noSeperator && index != fields.length) {
                buffer.append(seperator);
            }
        }
        return buffer.toString();
    }

    public Selector[] getFields() {
        return fieldList.getFields();
    }
}
