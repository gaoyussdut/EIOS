package top.toptimus.formula.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import top.toptimus.formula.util.*;
import top.toptimus.formula.util.resource.ResourceFactory;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;


/**
 * 基于XMLResource的简单模型Provider
 *
 * @author gaoyu
 * @version 1.6.7.9 [20170201 gaoyu] <br>
 * - 采用SLF4j日志框架输出日志 <br>
 * @since 1.0.14
 */

public abstract class XMLResourceSimpleModelProvider<M extends Cacheable> implements Provider<M> {

    protected static Logger logger = LoggerFactory.getLogger(Provider.class);
    protected Document doc = null;
    private Set<String> allModels = new HashSet<String>();     // NOSONAR

    public XMLResourceSimpleModelProvider(Properties props) {
        doc = loadResource(props);

        //从XMLDOM中预先提取出所有的ID
        NodeList models = XmlTools.getNodeListByPath(doc.getDocumentElement(), "model");

        for (int i = 0; i < models.getLength(); i++) {
            Node n = models.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element e = (Element) n;
            String id = e.getAttribute("id");
            if (id != null && id.length() > 0) {
                allModels.add(id);
            }
        }
    }

    /**
     * 从主/备地址中装入文档
     *
     * @param master    主地址
     * @param secondary 备用地址
     * @return XML文档
     */
    protected static Document loadDocument(String master, String secondary) {
        Settings profile = Settings.get();
        ResourceFactory rm = (ResourceFactory) profile.get("ResourceFactory");
        if (null == rm) {
            rm = new ResourceFactory();
        }

        Document ret = null;
        InputStream in = null;
        try {
            in = rm.load(master, secondary, null);
            ret = XmlTools.loadFromInputStream(in);
        } catch (Exception ex) {
            logger.error("Error occurs when load xml file,source=" + master, ex);
        } finally {
            IOTools.closeStream(in);
        }
        return ret;
    }

    public M load(String id) {
        return load(id, true);
    }

    @Override
    public M load(String id, boolean cacheAllowed) {
        if (doc == null)
            return null;

        /**
         * 当allModels没有找到该ID直接返回，不再搜索XMLDOM
         */
        if (!allModels.contains(id)) {
            return null;
        }

        Node found = XmlTools.getNodeByPath(doc.getDocumentElement(), "model[@id='" + id + "']");

        if (found == null || found.getNodeType() != Node.ELEMENT_NODE)
            return null;

        Element e = (Element) found;

        return newModel(id, e);
    }

    protected abstract Document loadResource(Properties props);

    protected abstract M newModel(String id, Element e);

    @Override
    public void addWatcher(Watcher<M> listener) {
        // to do noting
    }

    @Override
    public void removeWatcher(Watcher<M> listener) {
        // to do noting
    }

}
