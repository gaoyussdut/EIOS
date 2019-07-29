package top.toptimus.formula.batch;

import top.toptimus.formula.util.DefaultProperties;

/**
 * 缺省的处理过程
 *
 * @author gaoyu
 */
public class DefaultProcess implements Process {

    @Override
    public int init(DefaultProperties p) {
        p.list(System.out); // NOSONAR
        return 0;
    }

    @Override
    public int run() {
        return 0;
    }

}
