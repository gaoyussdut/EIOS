package top.toptimus.meta.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公式的配置信息
 *
 * @author liushikuan
 * @since 2017年11月15日
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormulaInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1828823961350228159L;
    private String formula; // 公式
    private String fkeys;    // 公式的输入字段（被序列化）
    private String resfkey; // 输出字段
}
