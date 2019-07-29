package top.toptimus.formula.loadbalance;

import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.PropertiesConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * LoadBalance虚基类
 *
 * @param <load>
 * @author gaoyu
 */
abstract public class AbstractLoadBalance<load extends Load> implements LoadBalance<load> {

    protected boolean onlyValid = true;


    public AbstractLoadBalance(Properties props) {
        onlyValid = PropertiesConstants.getBoolean(props, "loadbalance.onlyvalid", onlyValid);
    }

    public load select(String key, Properties props, List<load> loads) {
        List<load> _loads = loads;

        if (onlyValid) {
            _loads = new ArrayList<load>(loads.size());

            for (load l : loads) {
                if (l.isValid()) {
                    _loads.add(l);
                }
            }

            if (_loads.size() <= 0) {
                _loads = loads;
            }
        }
        return onSelect(key, props, _loads);
    }

    /**
     * Select事件处理
     *
     * @param key
     * @param props
     * @param loads
     * @return load
     */
    protected abstract load onSelect(String key, Properties props, List<load> loads);
}
