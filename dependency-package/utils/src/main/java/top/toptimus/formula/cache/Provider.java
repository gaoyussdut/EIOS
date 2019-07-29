package top.toptimus.formula.cache;

import top.toptimus.formula.util.Watcher;


/**
 * 缓存对象的提供者
 *
 * @author gaoyu
 * @version 1.6.4.17 [20151216 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 * @since 1.0.6
 */
public interface Provider<D1 extends Cacheable> {
    /**
     * 装入对象
     *
     * @param id           对象ID
     * @param cacheAllowed 允许装入缓存的对象
     * @return data
     */
    public D1 load(String id, boolean cacheAllowed);

    /**
     * 注册监控器
     *
     * @param watcher
     */
    public void addWatcher(Watcher<D1> watcher);

    /**
     * 注销监控器
     *
     * @param watcher
     */
    public void removeWatcher(Watcher<D1> watcher);
}
