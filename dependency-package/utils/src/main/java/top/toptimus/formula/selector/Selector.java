package top.toptimus.formula.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import top.toptimus.formula.formula.DataProvider;
import top.toptimus.formula.util.*;

import java.util.HashMap;

/**
 * 字段选择器
 * <p>
 * <br>
 * 字段选择器，负责从{@link top.toptimus.formula.formula.DataProvider DataProvider}中获取数据值.
 *
 * @author gaoyu
 * @version 1.6.7.9 [20170201 gaoyu] <br>
 * - 采用SLF4j日志框架输出日志 <br>
 * @since 1.5.2
 */
abstract public class Selector implements XMLConfigurable {

    /**
     * 统计数据
     */
    static public HashMap<String, Stat> stats = new HashMap<String, Stat>();
    /**
     * 工厂类实例
     */
    protected static TheFactory theFactory = new TheFactory();
    /**
     * a logger of log4j
     */
    protected Logger logger = LoggerFactory.getLogger(FieldList.class);
    /**
     * id
     */
    protected String id;
    /**
     * 是否忽略异常
     */
    protected boolean ignoreException = false;
    /**
     * default value
     */
    private String defaultValue = "";

    /**
     * 构造函数
     */
    protected Selector() {
    }

    /**
     * 创建实例
     *
     * @param e 配置XML节点
     * @param p 环境变量
     * @return Selector实例
     */
    public static Selector newInstance(Element e, Properties p) {
        return theFactory.newInstance(e, p, "selector");
    }

    /**
     * 创建实例
     * <p>
     * <p>
     * 通过XML配置节点和环境变量创建实例。当XML文档中的selector属性没有指定的时候，使用缺省的类dftClass来创建。
     *
     * @param e        XML配置节点
     * @param p        环境变量
     * @param dftClass 缺省的类名
     * @return Selector实例
     * @since 1.6.0.5
     */
    public static Selector newInstanceWithDefault(Element e, Properties p, String dftClass) {
        return theFactory.newInstance(e, p, "selector", dftClass);
    }

    /**
     * 创建实例
     *
     * @param e
     * @param p
     * @param preferredClass 指定的类名
     * @return Selector实例
     * @since 1.2.8.1
     */
    public static Selector newInstance(Element e, Properties p, String preferredClass) {
        Selector instance = theFactory.newInstance(preferredClass);
        instance.configure(e, p);
        return instance;
    }

    /**
     * to get the id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * to get the default value
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 是否OK
     *
     * @return 是否OK
     */
    public boolean isOk() {
        return id.length() > 0;
    }

    public void configure(Element _e, Properties _properties)
            throws BaseException {
        XmlElementProperties p = new XmlElementProperties(_e, _properties);
        id = PropertiesConstants.getString(p, "selector-id", "", true);
        defaultValue = PropertiesConstants.getString(p, "selector-default", defaultValue, true);

        ignoreException = PropertiesConstants.getBoolean(p,
                "selector-ignoreException", ignoreException, true);
        onConfigure(_e, p);
    }

    /**
     * Configure事件处理
     *
     * @param _e 配置XML节点
     * @param _p 变量集
     * @throws BaseException
     */
    public abstract void onConfigure(Element _e, Properties _p)
            throws BaseException;

    /**
     * 从{@link top.toptimus.util.formula.DataProvider DataProvider}中获取字段值
     *
     * @param _dataProvider 变量提供者
     * @return 计算之后的value
     */
    public String select(DataProvider _dataProvider) {
        long before = System.nanoTime();
        try {
            return onSelect(_dataProvider);
        } catch (RuntimeException ex) {
            if (!ignoreException) {
                throw ex;
            }
            logger.error("Error occurs when onSelect.Default value is returned.", ex);
            return defaultValue;
        } finally {
            String name = getClass().getName();
            Stat found = stats.get(getClass().getName());
            if (found == null) {
                found = new Stat();
                found._name = name;
                found._duration = 0;
                found._times = 0;
                stats.put(name, found);
            }
            found._times++;
            found._duration += System.nanoTime() - before;
        }
    }

    /**
     * Select事件处理
     *
     * @param _dataProvider
     * @return 计算之后的value
     */
    public abstract String onSelect(DataProvider _dataProvider);

    /**
     * 统计结构
     *
     * @author gaoyu
     */
    public static class Stat {
        String _name;
        long _times;
        long _duration;
    }

    /**
     * 工厂类
     *
     * @author gaoyu
     */
    public static class TheFactory extends Factory<Selector> {
        public String getClassName(String _module) throws BaseException {
            String __module = _module;
            if (__module.indexOf(".") < 0) {
                __module = "com.anysoft.selector.impl." + __module;
            }
            return __module;
        }
    }
}
