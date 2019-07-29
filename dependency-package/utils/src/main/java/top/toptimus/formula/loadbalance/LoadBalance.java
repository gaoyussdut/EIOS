package top.toptimus.formula.loadbalance;

import top.toptimus.formula.util.Properties;

import java.util.List;

/**
 * LoadBalance接口
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141120 gaoyu]
 * - 改造loadbalance模型
 * @since 1.2.0
 */
public interface LoadBalance<load extends Load> {

    public load select(String key, Properties props, List<load> loads);
}
