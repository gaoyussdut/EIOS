package top.toptimus.formula.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.toptimus.formula.util.*;
import top.toptimus.formula.util.resource.ResourceFactory;

import java.io.InputStream;
import java.util.Map;

/**
 * 基于XML文件的配置环境
 *
 * @param <O>
 * @author gaoyu
 * @version 1.6.7.9 [20170201 gaoyu] <br>
 * - 采用SLF4j日志框架输出日志 <br>
 * @since 1.5.0
 */
public abstract class XMLResource<O extends Reportable> implements Context<O> {

    protected static final Logger logger = LoggerFactory.getLogger(XMLResource.class);

    protected String configFile;
    protected String secondaryConfigFile;

    /**
     * Holder
     */
    protected Holder<O> holder = null;

    /**
     * 从主/备地址中装入文档
     *
     * @param master    主地址
     * @param secondary 备用地址
     * @return XML文档
     */
    protected static Document loadDocument(String master, String secondary) {
        ResourceFactory rm = Settings.getResourceFactory();
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

    @Override
    public void close() {
        if (holder != null) {
            holder.close();
        }
    }

    @Override
    public void configure(Element root, Properties props) {
        XmlElementProperties p = new XmlElementProperties(root, props);

        String defaultXrc = getDefaultXrc();

        configFile = p.GetValue("xrc.master", defaultXrc); // NOSONAR
        secondaryConfigFile = p.GetValue("xrc.secondary", defaultXrc); // NOSONAR

        Document doc = loadDocument(configFile, secondaryConfigFile);

        if (doc != null && doc.getDocumentElement() != null) {
            holder = new Holder<O>(getDefaultClass(), getObjectName()); // NOSONAR
            holder.configure(doc.getDocumentElement(), props);
        }
    }

    public abstract String getObjectName();

    public abstract String getDefaultClass();

    public abstract String getDefaultXrc();

    @Override
    public O get(String id) {
        return holder != null ? holder.get(id) : null;
    }

    @Override
    public void addWatcher(Watcher<O> watcher) {
        // nothing to do
    }

    @Override
    public void removeWatcher(Watcher<O> watcher) {
        // nothing to do
    }

    @Override
    public void report(Element xml) {
        if (xml != null) {
            xml.setAttribute("module", getClass().getName());
            xml.setAttribute("dftClass", getDefaultClass());
            xml.setAttribute("objName", getObjectName());

            xml.setAttribute("xrc.master", configFile);
            xml.setAttribute("xrc.secondary", secondaryConfigFile);
            xml.setAttribute("objCnt", String.valueOf(holder != null ? holder.getObjectCnt() : 0));

            if (holder != null && holder.getObjectCnt() > 0) {
                holder.report(xml);
            }
        }
    }

    @Override
    public void report(Map<String, Object> json) {
        if (json != null) {
            json.put("module", getClass().getName());
            json.put("dftClass", getDefaultClass());
            json.put("objName", getObjectName());

            json.put("xrc.master", configFile);
            json.put("xrc.secondary", secondaryConfigFile);
            json.put("objCnt", String.valueOf(holder != null ? holder.getObjectCnt() : 0));

            if (holder != null && holder.getObjectCnt() > 0) {
                holder.report(json);
            }
        }
    }
}
