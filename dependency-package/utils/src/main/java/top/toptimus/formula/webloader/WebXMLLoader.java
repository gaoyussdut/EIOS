package top.toptimus.formula.webloader;

import org.w3c.dom.Element;
import top.toptimus.formula.util.Factory;
import top.toptimus.formula.util.Properties;

import javax.servlet.ServletContext;


/**
 * 将web.xml中的内容装入到ServletContext中
 *
 * @author gaoyu
 * @since 1.6.0.0
 */
public interface WebXMLLoader {

    /**
     * 将XML节点中的内容装入到ServletContext
     *
     * @param settings
     * @param root
     * @param sc
     */
    public void load(Properties settings, Element root, ServletContext sc);

    /**
     * 工厂类
     * <p>
     * 用于创建WebXMLLoader.
     *
     * @author gaoyu
     */
    public static class TheFactory extends Factory<WebXMLLoader> {

    }
}
