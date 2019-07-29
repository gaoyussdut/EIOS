package top.toptimus.repository.place;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.place.TokenRedisDao;

@Repository
public interface TokenRedisRepository extends CrudRepository<TokenRedisDao, String> {
}
