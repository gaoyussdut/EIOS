package top.toptimus.formula.loadbalance.impl;

import top.toptimus.formula.loadbalance.AbstractLoadBalance;
import top.toptimus.formula.loadbalance.Load;
import top.toptimus.formula.util.Properties;

import java.util.List;


/**
 * 基于最少连接的loadbalance
 *
 * @author gaoyu
 */
public class LeastBusy<load extends Load> extends AbstractLoadBalance<load> {

    public LeastBusy(Properties props) {
        super(props);
    }


    public load onSelect(String key, Properties props, List<load> loads) {
        load found = null;

        int size = loads.size();
        if (size > 0) {
            int leastIndex = 0;
            double leastUse = Double.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                int weight = loads.get(i).getWeight();
                long times = loads.get(i).getCounter(true).getTimes();

                double _p = (weight == 0) ? times : (times * 1.0f) / weight;
                if (_p < leastUse) {
                    leastIndex = i;
                    leastUse = _p;
                }
            }
            found = loads.get(leastIndex);
        }
        return found;
    }

}
