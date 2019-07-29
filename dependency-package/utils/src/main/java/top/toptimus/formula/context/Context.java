package top.toptimus.formula.context;

import top.toptimus.formula.util.Reportable;
import top.toptimus.formula.util.Watcher;
import top.toptimus.formula.util.XMLConfigurable;


/**
 * 通用配置环境
 *
 * @param <O> 配置对象
 * @author gaoyu
 * @version 1.6.4.20 [20151222 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 * @since 1.5.0
 */
public interface Context<O extends Reportable> extends XMLConfigurable, AutoCloseable, Reportable {

    /**
     * 通过ID获取对象
     *
     * @param id
     * @return object
     */

    public O get(String id);

    /**
     * 注册监控器
     *
     * @param watcher
     */
    public void addWatcher(Watcher<O> watcher);

    /**
     * 注销监控器
     *
     * @param watcher
     */
    public void removeWatcher(Watcher<O> watcher);
}
