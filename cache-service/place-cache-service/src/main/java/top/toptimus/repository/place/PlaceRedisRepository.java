package top.toptimus.repository.place;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.toptimus.dao.place.PlaceRedisDao;

import java.util.List;

@Repository
public interface PlaceRedisRepository extends CrudRepository<PlaceRedisDao, String> {

    List<PlaceRedisDao> findByLotNo(String lotNo);
}
