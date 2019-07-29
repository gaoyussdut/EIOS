package top.toptimus.formula.batch;

import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.selector.Selector;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;
import top.toptimus.formula.util.XMLConfigurable;
import top.toptimus.formula.util.XmlElementProperties;

import java.io.PrintStream;

/**
 * 指令参数
 *
 * @author gaoyu
 * @version 1.6.4.20 [20151222 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 */
public class Argument implements XMLConfigurable, CommandHelper {
    protected String id;

    protected String name;

    protected String note;

    /**
     * the selector
     */
    protected Selector selector = null;

    protected boolean nullable = false;

    /**
     * to get the argument id
     *
     * @return the argument id
     */
    public String getId() {
        return id;
    }

    /**
     * to get the argument name
     *
     * @return the argument name
     */
    public String getName() {
        return name;
    }

    /**
     * to get the note
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * whether or not the value can be null
     *
     * @return whether or not
     */
    public boolean isNullable() {
        return nullable;
    }

    public boolean isOK() {
        return selector != null && selector.isOk();
    }

    @Override
    public void configure(Element e, Properties props) {
        Properties p = new XmlElementProperties(e, props);

        id = PropertiesConstants.getString(p, "selector-id", "");
        name = PropertiesConstants.getString(p, "name", "");
        note = PropertiesConstants.getString(p, "note", "");
        nullable = PropertiesConstants.getBoolean(p, "nullable", nullable);

        selector = Selector.newInstanceWithDefault(e, p, "SingleField");
    }

    /**
     * 计算参数值
     *
     * @param dp data provider
     * @return 计算之后的value
     */
    public String getValue(DataProvider dp) {
        return selector != null ? selector.select(dp) : "";
    }

    @Override
    public void printHelp(PrintStream ps) {
        ps.println("\t|" + getId() + "\t-" + getName());
        if (note != null && note.length() > 0) {
            ps.println("\t\t|" + getNote());
        }
        // nullable
        ps.println("\t\t|Nullable = " + Boolean.toString(nullable));
        // default value
        ps.println("\t\t|Default Value = "
                + (selector == null ? "" : selector.getDefaultValue()));
        // selector
        ps.println("\t\t|Selector = "
                + (selector == null ? "null" : selector.getClass().getName()));
    }
}
