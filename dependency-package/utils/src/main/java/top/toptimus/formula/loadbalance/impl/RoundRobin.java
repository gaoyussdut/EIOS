package top.toptimus.formula.loadbalance.impl;

import top.toptimus.formula.loadbalance.AbstractLoadBalance;
import top.toptimus.formula.loadbalance.Load;
import top.toptimus.formula.util.Properties;

import java.util.List;


/**
 * 基于RoundRobin的LoadBalance
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141020 gaoyu]
 * - 改造loadbalance模型
 */

public class RoundRobin<load extends Load> extends AbstractLoadBalance<load> {

    protected volatile int currentSelect = 0;


    public RoundRobin(Properties props) {
        super(props);
    }

    public load onSelect(String key, Properties props, List<load> loads) {
        load found = null;

        int size = loads.size();
        if (size > 0) {
            found = loads.get(currentSelect % size);
            synchronized (this) {
                currentSelect++;
                if (currentSelect >= size) {
                    currentSelect = 0;
                }
            }
        }
        return found;
    }
}
