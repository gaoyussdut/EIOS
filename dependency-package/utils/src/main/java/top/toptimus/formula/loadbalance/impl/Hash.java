package top.toptimus.formula.loadbalance.impl;

import top.toptimus.formula.loadbalance.AbstractLoadBalance;
import top.toptimus.formula.loadbalance.Load;
import top.toptimus.formula.util.Properties;

import java.util.List;
import java.util.Random;

/**
 * 基于主键Hash的LoadBalance
 *
 * @param <load>
 * @author gaoyu
 * @version 1.5.3 [20141020 gaoyu]
 * - 改造loadbalance模型
 */
public class Hash<load extends Load> extends AbstractLoadBalance<load> {

    public static Random r = new Random();


    public Hash(Properties props) {
        super(props);
    }

    public load onSelect(String key, Properties props, List<load> loads) {
        load found = null;

        int size = loads.size();
        if (size > 0) {
            int hashcode = 0;
            if (key == null || key.length() <= 0) {
                //当没有传入Key的时候，同Rand模式
                hashcode = r.nextInt(size) % size;
            } else {
                hashcode = key.hashCode();
            }
            found = loads.get((hashcode & Integer.MAX_VALUE) % size);
        }

        return found;
    }
}
