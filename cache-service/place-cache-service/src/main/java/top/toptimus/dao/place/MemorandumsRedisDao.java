package top.toptimus.dao.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import top.toptimus.dao.place.baseDao.RedisBaseDao;
import top.toptimus.place.placeRequire.PlaceRequireDTO;

import java.util.List;

/**
 * redis中存储的选中的备查账簿信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Memorandums")
public class MemorandumsRedisDao extends RedisBaseDao {
    /**
     *
     */
    private static final long serialVersionUID = 687319306549822304L;
    @Id
    private String lotNo; // 批次号，一种应用场景是有编码的，例如WBS或者业务编码（单号）；另外一种应用场景是用户选中多条备查账簿，作为引用备查账簿的组标识

    private String metaId; // 备查帐的metaId
    private String processId; // 启动流程ID
    private List<String> tokenIds; // 备查帐的tokenId

    public MemorandumsRedisDao(String lotNo, PlaceRequireDTO placeRequireDTO) {
        this.lotNo = lotNo;
        this.metaId = placeRequireDTO.getMemorandumInfoBody().getMetaId();
        this.processId = placeRequireDTO.getProcessId();
        this.tokenIds = placeRequireDTO.getMemorandumInfoBody().getTokenIds();
    }
}
