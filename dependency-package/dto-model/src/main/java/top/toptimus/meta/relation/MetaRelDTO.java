package top.toptimus.meta.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.MetaTypeEnum;

/**
 * Created by lzs on 2019/8/7.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaRelDTO {
    private String billMetaId;  //表头meta
    private String entryMetaId; //分录、关联单据meta
    private MetaTypeEnum metaType; //分录类型。
    private int order_;  //分录顺序

}