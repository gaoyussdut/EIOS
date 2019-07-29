package top.toptimus.repository.meta.MetaRelation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.toptimus.dao.meta.MetaAuthRelationDao;

import java.util.List;

public interface MetaAuthRelationRepository extends ElasticsearchRepository<MetaAuthRelationDao, String> {
    MetaAuthRelationDao findByMetaIdAndRoleId(String metaId, String roleId);

    List<MetaAuthRelationDao> findMetaAuthRelationDaosByMetaIdInAndRoleId(List<String> metaIds, String roleId);

    List<MetaAuthRelationDao> findMetaAuthRelationDaosByMetaId(String metaId);
}
