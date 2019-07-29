package top.toptimus.meta.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liushikuan
 * @since 2017年10月23日16:51:39
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RalValue implements Serializable {

    /**
     * 查询视图（表名），例如：供应商字段是供应商表
     */
    private String metaKey;
    /**
     * meta分组
     */
    private String metaName;
    /**
     * 选中后的展示的字段（唯一），例如：供应商字段展示供应商名称
     */
    private String fkey;

    public RalValue build(String metaKey) {
        this.metaKey = metaKey;
        return this;
    }

}
