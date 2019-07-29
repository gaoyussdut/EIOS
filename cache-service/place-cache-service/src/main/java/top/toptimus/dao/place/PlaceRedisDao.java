package top.toptimus.dao.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import top.toptimus.dao.place.baseDao.RedisBaseDao;
import top.toptimus.place.PlaceDTO;

/**
 * 选中的备查账的组id和库所，redis存储
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("place")
public class PlaceRedisDao extends RedisBaseDao {
    /**
     *
     */
    private static final long serialVersionUID = 1295319306549822304L;

    @Id
    private String billTokenId; // 表头TokenId
    private String billMetaId; //TTID
    private String processId; // 流程Id
    private String userTaskId; // 流程节点Id
    private String tokenTemplateName; // TTname赋值,如果是代办传空，是AO的话需要传值前端显示
    private String poTokenId; // 任务tokenId
    @Indexed
    private String lotNo; // lotNo
    private String orgId; // 工作中心ID
    private int lotSelfIndex; // lotSelfIndex
    private String wbsInsId; // WBSid
    private String userId; // 操作用户Id

    /**
     * 转换成PlaceDTO 仅存储索引
     *
     * @return 库所
     */
    public PlaceDTO convertPlaceDTO() {
        return new PlaceDTO(this.billTokenId, this.billMetaId, this.tokenTemplateName, this.userId);
    }

}
