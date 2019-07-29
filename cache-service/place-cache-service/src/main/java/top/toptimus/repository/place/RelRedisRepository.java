package top.toptimus.repository.place;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.place.RelRedisDao;

import java.util.List;

@Repository
public interface RelRedisRepository extends CrudRepository<RelRedisDao, String> {
    List<RelRedisDao> findAllByCurrentTokenId(String currentTokenId);
    List<RelRedisDao> findAllByCurrentTokenIdAndAndRelTokenId(String currentTokenId,String relTokenId);
    List<RelRedisDao> findAllByRelTokenId(String relTokenId);
}
