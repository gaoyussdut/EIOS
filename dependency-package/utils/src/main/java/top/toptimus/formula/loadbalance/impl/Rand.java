package top.toptimus.formula.loadbalance.impl;

import top.toptimus.formula.loadbalance.AbstractLoadBalance;
import top.toptimus.formula.loadbalance.Load;
import top.toptimus.formula.util.Properties;

import java.util.List;
import java.util.Random;

/**
 * 基于随机算法的LoadBalance
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141020 gaoyu]
 * - 改造loadbalance模型
 */
public class Rand<load extends Load> extends AbstractLoadBalance<load> {

    public static Random r = new Random();


    public Rand(Properties props) {
        super(props);
    }

    public load onSelect(String key, Properties props, List<load> loads) {
        load found = null;
        int size = loads.size();
        if (size > 0) {
            found = loads.get(r.nextInt(size) % size);
        }
        return found;
    }
}
