package top.toptimus.formula.selector.impl;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.formula.DefaultFunctionHelper;
import top.toptimus.formula.formula.Expression;
import top.toptimus.formula.formula.Parser;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;

/**
 * 公式选择器
 *
 * @author gaoyu
 * @since 1.5.2
 */
public class Formula extends Selector {


    protected String formula = "0";
    protected Expression expr = null;

    public void onConfigure(Element _e, Properties _p) throws BaseException {
        formula = PropertiesConstants.getString(_p, "formula", formula, true);
        Parser parser = new Parser(new DefaultFunctionHelper(null));
        expr = parser.parse(formula);
    }

    public String onSelect(DataProvider _dataProvider) {
        try {
            return expr.getValue(_dataProvider).toString();
        } catch (Exception ex) {
            return getDefaultValue();
        }
    }
}
