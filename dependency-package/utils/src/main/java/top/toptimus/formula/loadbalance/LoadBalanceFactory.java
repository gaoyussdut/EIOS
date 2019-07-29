package top.toptimus.formula.loadbalance;

import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Factory;

/**
 * 工厂类
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141120 gaoyu]
 * - 改造loadbalance模型
 * @since 1.2.0
 */
public class LoadBalanceFactory<load extends Load> extends Factory<LoadBalance<load>> {
    public String getClassName(String _module) throws BaseException {
        if (_module.indexOf('.') < 0) {
            return "com.anysoft.loadbalance.impl." + _module;
        }
        return _module;
    }
}
