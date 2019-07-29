package top.toptimus.dao.place;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import top.toptimus.dao.place.baseDao.RedisBaseDao;
import top.toptimus.tokendata.TokenDataDto;

/**
 * redis中存储的选中备查账簿中的token数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RedisHash("tokens")
public class TokenRedisDao extends RedisBaseDao {
    /**
     *
     */
    private static final long serialVersionUID = 4895319306549822303L;

    @Id
    private String tokenId; //  备查账的token id
    private TokenDataDto tokenDataDto;  //  备查账数据
    private boolean isLock = true;// true:上锁 false：未锁

    public TokenRedisDao(String tokenId) {
        this.tokenId = tokenId;
    }

    public TokenRedisDao(TokenDataDto tokenDataDto) {
        this.tokenId = tokenDataDto.getTokenId();
        this.tokenDataDto = tokenDataDto;
    }

    /**
     * 加锁
     *
     * @return this
     */
    public TokenRedisDao lock() {
        this.isLock = true;
        return this;
    }

    /**
     * 释放锁
     *
     * @return this
     */
    public TokenRedisDao releaseLock() {
        this.isLock = false;
        return this;
    }
}