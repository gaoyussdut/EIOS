package top.toptimus.formula.util;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;

/**
 * 对象的工厂类
 *
 * @param <OBJECT> 对象的类名
 * @author gaoyu
 * @version 1.6.4.29 [20160126 gaoyu] <br>
 * - 修正取构造器时的异常 <br>
 * @see XMLConfigurable
 */
public class Factory<OBJECT> {

    /**
     * 创建所需的ClassLoader
     */
    protected ClassLoader classLoader = null;

    /**
     * 缺省构造函数
     */
    public Factory() {
        // nothing to do
    }

    /**
     * 定制ClassLoader的构造函数
     *
     * @param cl ClassLoader实例
     */
    public Factory(ClassLoader cl) {
        classLoader = cl;
    }

    /**
     * 创建新的对象实例
     *
     * @param xml   创建对象所需的XML参数
     * @param props 所需的变量集
     * @return 对象实例
     * @throws BaseException
     */
    public OBJECT newInstance(Element xml, Properties props) {
        return newInstance(xml, props, "module");
    }

    /**
     * 创建新的对象实例
     * <p>如果对象为{@link XMLConfigurable}的实例，则调用{@link XMLConfigurable#configure(Element, Properties)}来初始化对象.</p>
     *
     * @param xml        创建对象所需的XML参数
     * @param props      所需的变量集
     * @param moduleAttr 表示module属性的属性名称
     * @return 对象实例
     * @throws BaseException
     * @see XMLConfigurable
     * @see #newInstance(String)
     */
    public OBJECT newInstance(Element xml, Properties props, String moduleAttr) {
        String module = xml.getAttribute(moduleAttr);
        if (StringUtils.isEmpty(module)) {
            throw new BaseException(Factory.class.getName(),
                    "Can not find attr in the element,attr id = " + moduleAttr);
        }

        OBJECT instance = newInstance(module);

        if (instance instanceof XMLConfigurable) {
            ((XMLConfigurable) instance).configure(xml, props);
        }

        return instance;
    }

    /**
     * 创建新的对象实例
     * <p>如果对象为{@link XMLConfigurable}的实例，则调用{@link XMLConfigurable#configure(Element, Properties)}来初始化对象.</p>
     *
     * @param xml        创建对象所需的XML参数
     * @param props      所需的变量集
     * @param moduleAttr 表示module属性的属性名称
     * @param dftClass   缺省的类
     * @return object
     * @throws BaseException
     * @since 1.3.5
     */
    public OBJECT newInstance(Element xml, Properties props, String moduleAttr, String dftClass) {
        String module = xml.getAttribute(moduleAttr);
        if (StringUtils.isEmpty(module)) {
            module = dftClass;
        }

        OBJECT instance = newInstance(module);

        if (instance instanceof XMLConfigurable) {
            ((XMLConfigurable) instance).configure(xml, props);
        }

        return instance;
    }

    /**
     * 按照指定的module来创建对象实例
     * <p>module不完全是对象的类名，在使用之前需调用{@link #getClassName(String)}进行转换。如果module不使用类名的话，
     * 可以override函数{@link #getClassName(String)}将module转换为类名.</p>
     * <p>在某些时候需要选定ClassLoader来创建实例，需定制{@link #classLoader}.</p>
     *
     * @param module 类型或者类名
     * @return 对象实例
     * @throws BaseException 创建过程中抛出此异常
     */
    @SuppressWarnings("unchecked")
    public OBJECT newInstance(String module) {
        String className = getClassName(module);
        try {
            if (classLoader == null) {
                classLoader = Settings.getClassLoader();
            }
            return (OBJECT) classLoader.loadClass(className).newInstance();
        } catch (Exception ex) {
            throw new BaseException(Factory.class.getName(),
                    "Can not create instance of " + className, ex);
        }
    }

    /**
     * 按照指定的module来创建对象实例
     * <p>
     * <br>
     * 按照指定的module来创建对象实例,如果对象是构造函数为object(Properties)，则采用Properties来构造实例。
     *
     * @param module 对象实例
     * @param props  初始化参数
     * @return 对象实例
     * @throws BaseException
     * @since 1.0.9
     * @since 1.6.3.37 [gaoyu 20150804]<br>
     * - 支持Configurable的自动初始化 <br>
     */
    @SuppressWarnings("unchecked")
    public OBJECT newInstance(String module, Properties props) {
        String className = getClassName(module);
        try {
            if (classLoader == null) {
                classLoader = Settings.getClassLoader();
            }
            Class<?> clazz = classLoader.loadClass(className);
            Constructor<?> constructor = getConstructor(clazz);
            if (constructor != null) {
                return (OBJECT) constructor.newInstance(new Object[]{props});
            } else {
                OBJECT instance = (OBJECT) clazz.newInstance();

                if (instance instanceof Configurable) {
                    ((Configurable) instance).configure(props);
                }

                return instance;
            }
        } catch (Exception ex) {
            throw new BaseException(Factory.class.getName(),
                    "Can not create instance of " + className, ex);
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor(new Class[]{Properties.class});
        } catch (NoSuchMethodException ex) { // NOSONAR
            return null;
        }
    }

    /**
     * 将module转化为全路径类名
     *
     * @param module module名
     * @return 全路径类名
     * @throws BaseException
     */
    public String getClassName(String module) {
        return module;
    }
}
