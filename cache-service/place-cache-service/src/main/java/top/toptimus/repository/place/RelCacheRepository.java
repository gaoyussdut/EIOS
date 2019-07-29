package top.toptimus.repository.place;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.place.cache.RelCacheDao;

import java.util.List;

@Repository
public interface RelCacheRepository extends CrudRepository<RelCacheDao, String> {

    List<RelCacheDao> findByBillTokenId(String billTokenId);

    void deleteAllByBillTokenIdAndAuthId(String billTokenId,String authId);
}
