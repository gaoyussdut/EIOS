package top.toptimus.formula.selector.impl;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.DataProviderProperties;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;

/**
 * Template
 *
 * @author yyduan
 * @since 1.6.6.13
 */
public class Template extends Selector {
    protected String template = "";

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        template = PropertiesConstants.getString(_p, "selector-template", template, true);
    }

    @Override
    public String onSelect(DataProvider provider) {
        DataProviderProperties p = new DataProviderProperties(provider);
        String value = p.transform(template);
        return StringUtils.isEmpty(value) ? getDefaultValue() : value;
    }


}
