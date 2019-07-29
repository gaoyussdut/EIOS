package top.toptimus.meta.MetaRelation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.UserControlEnum;
import top.toptimus.common.enums.process.MetaRelEnum;

/**
 * Created by ccr on 2019/1/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MetaViewInfoDTO {

    private String viewMetaId;            // 视图metaId  根据角色不同 所能查看的字段信息是不同的
    private String masterDataMetaId;      // 主数据metaId  获取具体数据所用的主数据metaId
    private String metaName;              // metaName
    private MetaRelEnum metaRelEnum;      //关联类型
    private String relMetaId;             // 如果关联类型为MEMORANDVN(引用单据),此处存放引用单据的meta
    private UserControlEnum usercontrol;  // 用户控件
}
