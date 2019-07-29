package top.toptimus.dao.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.toptimus.meta.MetaRelation.MasterBillMetaRelationDTO;
import top.toptimus.meta.MetaRelation.MasterMetaInfoDTO;

import java.util.List;

/**
 * 记录表头meta和（分录、关联单据、引用单据）之间的meta关系
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "master_bill_meta_rel", type = "master_bill_meta_rel", shards = 1, replicas = 0, refreshInterval = "-1")
public class MasterBillMetaRelationDao {
    @Id
    private String billMasterMetaId; //单据主数据meta
    private List<MasterMetaInfoDTO> masterMetaInfoDTOS;

    public MasterBillMetaRelationDTO build() {
        return new MasterBillMetaRelationDTO(
                this.billMasterMetaId
                , this.masterMetaInfoDTOS
        );
    }
}
