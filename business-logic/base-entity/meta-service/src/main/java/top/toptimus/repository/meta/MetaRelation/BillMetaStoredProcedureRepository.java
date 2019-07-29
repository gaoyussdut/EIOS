package top.toptimus.repository.meta.MetaRelation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.toptimus.dao.meta.BillMetaStoredProcedureDao;

import java.util.List;

public interface BillMetaStoredProcedureRepository extends ElasticsearchRepository<BillMetaStoredProcedureDao, String> {

    List<BillMetaStoredProcedureDao> findAllByBillMasterMetaIdAndEntryMasterMetaId(String billMasterMetaId,String entryMasterMetaId);

    void deleteAllByBillMasterMetaIdAndAndEntryMasterMetaId(String billMasterMetaId,String entryMasterMetaId);
}
