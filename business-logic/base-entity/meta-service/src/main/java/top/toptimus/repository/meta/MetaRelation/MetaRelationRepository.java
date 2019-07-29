package top.toptimus.repository.meta.MetaRelation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.toptimus.dao.meta.MetaRelationDao;

import java.util.List;

public interface MetaRelationRepository extends ElasticsearchRepository<MetaRelationDao, String> {

    List<MetaRelationDao> findByMasterMemorandvnMetaId(String masterMemorandvnMetaId);
}
