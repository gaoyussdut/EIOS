package top.toptimus.dao.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.MetaRelation.ViewMemorandvnDTO;
import top.toptimus.meta.MetaRelation.ViewMetaDTO;

import java.util.List;

/**
 * 单据内meta关系DAO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "meta_rel", type = "meta_rel", shards = 1, replicas = 0, refreshInterval = "-1")
public class MetaRelationDao {
    @Id
    private String masterMetaId; //单据主数据meta
    private String masterMetaName; //单据主数据metaName
    private List<ViewMetaDTO> viewMetaDTOList; //单据内视图meta
    private String masterMemorandvnMetaId; //单据内备查帐主数据meta
    private List<ViewMemorandvnDTO> viewMemorandvnDTOList; //单据内视图备查
    private String storedProcedure; //单据提交存储过程

    public MetaRelationDTO build() {
        return new MetaRelationDTO(
                this.masterMetaId
                , this.masterMetaName
                , this.viewMetaDTOList
                , this.masterMemorandvnMetaId
                , this.viewMemorandvnDTOList
                , this.storedProcedure
        );
    }

}
