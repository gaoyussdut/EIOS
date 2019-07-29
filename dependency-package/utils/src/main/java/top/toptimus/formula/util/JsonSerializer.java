package top.toptimus.formula.util;

import java.util.Map;

/**
 * JSON序列化接口
 *
 * @author gaoyu
 * @version [20140912 gaoyu]<br>
 * - 将Map参数进行参数化
 * @since 1.0.6
 */
public interface JsonSerializer {
    /**
     * 输出到JSON对象
     *
     * @param json
     */
    public void toJson(Map<String, Object> json);

    /**
     * 从JSON对象读入
     *
     * @param json
     */
    public void fromJson(Map<String, Object> json);
}
