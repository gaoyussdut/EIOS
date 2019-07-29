package top.toptimus.dao.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 新增关联单据时需调用的存储过程(单据下推)
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "bill_meta_stored_procedure", type = "bill_meta_stored_procedure", shards = 1, replicas = 0, refreshInterval = "-1")

public class BillMetaStoredProcedureDao {
    @Id
    private String id;
    private String billMasterMetaId; //单据表头主数据meta
    private String entryMasterMetaId;//关联单据(下推单据)主数据meta
    private String storedProcedure;  //下推时需调用的存储过程名
}
