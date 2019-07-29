package top.toptimus.repository.meta.metaSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.meta.MetaSearchConfigDao;

/**
 * meta检索配置Repo
 */
@Repository
public interface MetaSearchConfigRepository extends ElasticsearchRepository<MetaSearchConfigDao, String> {

}
