package top.toptimus.dao.place.baseDao;

import lombok.Data;
import lombok.experimental.Wither;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

/**
 * redis基础dao
 *
 * @author gaoyu
 */
@Wither
@Data
public abstract class RedisBaseDao implements Serializable {
    @TimeToLive
    final long liveTime = 60L; //  TTL
}
