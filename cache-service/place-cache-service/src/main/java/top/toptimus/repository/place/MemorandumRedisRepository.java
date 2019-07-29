package top.toptimus.repository.place;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.place.MemorandumsRedisDao;

@Repository
public interface MemorandumRedisRepository extends CrudRepository<MemorandumsRedisDao, String> {
}
