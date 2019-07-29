package top.toptimus.repository.meta.MetaRelation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.toptimus.dao.meta.MasterBillMetaRelationDao;

import java.util.List;

public interface MasterBillMetaRelationRepository extends ElasticsearchRepository<MasterBillMetaRelationDao, String> {
    public MasterBillMetaRelationDao findByBillMasterMetaId(String billMetaId);
}
