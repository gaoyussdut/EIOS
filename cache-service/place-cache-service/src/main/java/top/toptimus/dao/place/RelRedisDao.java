package top.toptimus.dao.place;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import top.toptimus.dao.place.baseDao.RedisBaseDao;

/**
 * 记录单据关系redis结构
 * TODO，为了解决并发问题，加入auth
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RedisHash("rel")
public class RelRedisDao extends RedisBaseDao {
    private static final long serialVersionUID = 765272662256403126L;

    @Id
    private String id;
    private String currentMetaId;  //  当前meta id
    private String currentTokenId;  //  当前token id
    private String relMetaId;   //  关联的meta id
    private String relTokenId; //  关联的token id

}
