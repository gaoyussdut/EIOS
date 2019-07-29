package top.toptimus.formula.loadbalance.impl;

import top.toptimus.formula.loadbalance.AbstractLoadBalance;
import top.toptimus.formula.loadbalance.Load;
import top.toptimus.formula.util.Properties;

import java.util.List;


/**
 * 基于优先级的LoadBalance
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141020 gaoyu]
 * - 改造loadbalance模型
 */
public class Priority<load extends Load> extends AbstractLoadBalance<load> {

    public Priority(Properties props) {
        super(props);
    }


    public load onSelect(String key, Properties props, List<load> loads) {
        load found = null;

        int size = loads.size();
        if (size > 0) {
            int highestIndex = 0;
            int highestPriority = 0;
            for (int i = 0; i < size; i++) {
                int _p = loads.get(i).getPriority();
                if (_p > highestPriority) {
                    highestIndex = i;
                    highestPriority = _p;
                }
            }
            found = loads.get(highestIndex);
        }
        return found;
    }

}
