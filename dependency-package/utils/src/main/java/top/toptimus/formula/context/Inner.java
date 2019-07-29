package top.toptimus.formula.context;

import org.w3c.dom.Element;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.Reportable;
import top.toptimus.formula.util.Watcher;

import java.util.Map;

/**
 * Source文件内置的Context
 *
 * @param <O>
 * @author gaoyu
 * @version 1.6.4.20 [20151222 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 * @since 1.5.0
 */
public abstract class Inner<O extends Reportable> implements Context<O> {

    /**
     * Holder
     */
    protected Holder<O> holder = null;

    @Override
    public void close() {
        if (holder != null) {
            holder.close();
        }
    }

    @Override
    public void configure(Element element, Properties props) {
        holder = new Holder<O>(getDefaultClass(), getObjectName()); // NOSONAR
        holder.configure(element, props);
    }

    public abstract String getObjectName();

    public abstract String getDefaultClass();

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

            json.put("objCnt", String.valueOf(holder != null ? holder.getObjectCnt() : 0));

            if (holder != null && holder.getObjectCnt() > 0) {
                holder.report(json);
            }
        }
    }
}
