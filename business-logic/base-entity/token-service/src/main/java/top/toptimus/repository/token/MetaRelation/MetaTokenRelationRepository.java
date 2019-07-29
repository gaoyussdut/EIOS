package top.toptimus.repository.token.MetaRelation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.token.MetaTokenRelationDao;

import java.util.List;

@Repository
public interface MetaTokenRelationRepository extends ElasticsearchRepository<MetaTokenRelationDao, String> {
    List<MetaTokenRelationDao> findAllByBillTokenIdAndBillMetaIdAndEntryMetaId(String billTokenId, String billMetaId, String entryMetaId);

    List<MetaTokenRelationDao> findAllByBillTokenId(String billTokenId);

    void deleteByEntryTokenId(String entryTokenId);

    List<MetaTokenRelationDao> findAllByEntryMetaIdAndEntryTokenId(String entryMetaId, String entryTokenId);

    void deleteAllByBillMetaIdAndBillTokenIdAndEntryMetaIdAndEntryTokenId(String billMetaId,String billTokenId,String entryMetaId, String entryTokenId);
}
