package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;

/**
 * 当前时间
 *
 * @author gaoyu
 * @since 1.5.2
 */
public class Now extends Selector {


    public void onConfigure(Element _e, Properties _p) throws BaseException {
    }


    public String onSelect(DataProvider _dataProvider) {
        return String.valueOf(System.currentTimeMillis());
    }

}
