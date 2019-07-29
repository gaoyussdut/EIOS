package top.toptimus.formula.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.toptimus.formula.util.Manager;
import top.toptimus.formula.util.Watcher;
import top.toptimus.formula.util.WatcherHub;


/**
 * 对象缓存管理器
 *
 * @param <D1> 缓存对象类
 * @author gaoyu
 * @version 1.6.7.9 [20170201 gaoyu] <br>
 * - 采用SLF4j日志框架输出日志 <br>
 * @since 1.0.6
 */
public class CacheManager<D1 extends Cacheable> extends Manager<D1>
        implements Provider<D1>, Watcher<D1> {

    /**
     * a logger of log4j
     */
    protected static Logger logger = LoggerFactory.getLogger(CacheManager.class);
    /**
     * 委托的Provider
     */
    protected Provider<D1> provider = null;

    /**
     * 锁对象
     */
    protected Object lock = new Object();

    protected WatcherHub<D1> watchers = new WatcherHub<D1>(); // NOSONAR

    /**
     * Constructor
     */
    public CacheManager() {
        // nothing to do
    }

    /**
     * Constructor
     *
     * @param p 委托的Provider
     */
    public CacheManager(Provider<D1> p) {
        provider = p;
        if (provider != null) {
            provider.addWatcher(this);
        }
    }


    public D1 load(String id) {
        return load(id, true);
    }

    @Override
    public D1 load(String id, boolean noCache) {
        if (provider != null) {
            return provider.load(id, noCache);
        }
        return null;
    }

    @Override
    public D1 get(String id) {
        D1 found = super.get(id);
        if (found == null) {
            synchronized (lock) {
                found = super.get(id);
                if (found == null) {
                    found = load(id);
                    if (found != null) {
                        super.add(id, found);
                    }
                }
            }
        } else {
            if (found.isExpired()) {
                //对象已过期
                found = load(id);
                if (found != null) {
                    add(found);
                }
            }
        }
        return found;
    }

    /**
     * 获取指定ID的数据
     * <p>
     * <br>
     * 提供一个快速接口给子类使用.
     *
     * @param id ID
     * @return data
     * @since 1.0.7
     */
    protected D1 _get(String id) { // NOSONAR
        return super.get(id);
    }

    /**
     * 向容器中增加数据
     * <p>
     * <br>
     * 提供一个快速接口给子类使用.
     *
     * @param id  id
     * @param obj 数据对象
     * @since 1.0.7
     */
    protected void _add(String id, D1 obj) { // NOSONAR
        synchronized (lock) {
            super.add(id, obj);
        }
    }

    @Override
    public void add(String id, D1 obj) {
        synchronized (lock) {
            super.add(id, obj);
        }
    }

    @Override
    public void remove(String id) {
        synchronized (lock) {
            super.remove(id);
        }
    }

    /**
     * 向缓存中增加对象
     *
     * @param obj
     */
    public void add(D1 obj) {
        synchronized (lock) {
            super.add(obj.getId(), obj);
        }
    }

    @Override
    public void changed(String id, D1 data) {
        synchronized (lock) {
            logger.info("model is changed,id = " + id);
            add(id, data);
        }

        if (watchers != null) {
            watchers.changed(id, data);
        }
    }

    @Override
    public void added(String id, D1 data) {
        // do nothing
        if (watchers != null) {
            watchers.added(id, data);
        }
    }

    @Override
    public void removed(String id, D1 data) {
        synchronized (lock) {
            logger.info("model is removed,id = " + id);
            remove(id);
        }

        if (watchers != null) {
            watchers.changed(id, data);
        }
    }

    @Override
    public void allChanged() {
        synchronized (lock) {
            clear();
        }
        if (watchers != null) {
            watchers.allChanged();
        }
    }

    @Override
    public void addWatcher(Watcher<D1> watcher) {
        if (watchers != null)
            watchers.addWatcher(watcher);
    }

    @Override
    public void removeWatcher(Watcher<D1> watcher) {
        if (watchers != null)
            watchers.removeWatcher(watcher);
    }


}
