package top.toptimus.dao.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaViewInfoDTO;

import java.util.List;

/**
 * 根据权限查询表头meta所能查看的分录、备查账meta
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "meta_auth_view", type = "meta_auth_view", shards = 1, replicas = 0, refreshInterval = "-1")
public class MetaAuthRelationDao {

    @Id
    private String id;
    // metaId和role联合确定唯一的配置
    private String metaId; // 单据主数据metaId
    private String roleId; // 角色ID
    private List<MetaViewInfoDTO> relMetaId;

    public MetaAuthRelationDTO build() {
        return new MetaAuthRelationDTO(
                this.id
                , this.metaId
                , this.roleId
                , this.relMetaId
        );
    }


}
