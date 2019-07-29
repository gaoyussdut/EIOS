package top.toptimus.meta.MetaRelation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据权限查询表头meta所能查看的分录、备查账meta
 * Created by JiangHao on 2019/1/22.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetaAuthRelationDTO implements Serializable {

    private static final long serialVersionUID = 5024907029972131746L;

    private String id; // 主键
    // metaId和role联合确定唯一的配置
    private String metaId; // metaId
    private String roleId; // 角色id
    private List<MetaViewInfoDTO> relMetaId; // 配置信息
}
