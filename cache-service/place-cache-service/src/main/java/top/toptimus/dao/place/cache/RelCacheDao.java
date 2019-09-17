package top.toptimus.dao.place.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.place.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库所关联结构
 *
 * @author gaoyu
 * @since 2019-01-28
 */
@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("place_rel")
public class RelCacheDao {

    @Id
    private String id;
    private String sessionId;
    @Indexed
    private String billTokenId;
    private String billMetaId;

    //前置单据关联信息
    private CacheRelToken preCacheRelToken;
    /*
        关系数据是在前端请求的时候加载
     */
    //  原始凭证关系记录，报价单，入库单
    private List<CacheRelToken> certificates = new ArrayList<>();
//    //  业务记录关系记录，商机、服务支持，成员客户等
//    private List<CacheRelToken> businessRecord;
//    //  关联备查账，后续走merkle
//    private List<CacheRelToken> memorandums;

    //  活动记录，活动的记录不用CacheRelToken，活动上的token记录根据配置，会自动带到单据上  TODO

    //  关联业务记录，双向状态,不用CacheRelToken TODO

    protected String datas;
    //    protected Map<String, List<TokenDataDto>> datas = new HashMap<>();  //  K: meta id , V: token list
    @Indexed
    private String authId;

    /**
     * 构造函数
     *
     * @param merklePlaceModel 库所
     * @param placeSession     session
     */
    public RelCacheDao(MerklePlaceModel merklePlaceModel, PlaceSession placeSession) {
        this.id = merklePlaceModel.getCacheId();
        this.billTokenId = merklePlaceModel.getPlaceDTO().getBillTokenId();
        this.billMetaId = merklePlaceModel.getPlaceDTO().getBillMetaId();
        this.buildRel(merklePlaceModel.getPlaceDTO());
        this.datas = JSON.toJSONString(merklePlaceModel.getPlaceDTO().getDatas());
        this.authId = placeSession.getAuthId();
        this.sessionId = placeSession.getSessionId();
    }

    /**
     * 添加关联关系
     *
     * @param placeDTO 关联单据
     */
    public void addRel(PlaceDTO placeDTO) {
        //  TODO
//        this.getCertificates().add(new CacheRelToken(placeDTO));
    }

    /**
     * 编辑关系信息
     *
     * @param placeDTO placeDTO
     */
    private void buildRel(PlaceDTO placeDTO) {
        //  TODO
//        //编辑与前置单据的关联信息
//        if (null != placeDTO.getPreMetaTokenRelationDTO()) {
//            this.preCacheRelToken = new CacheRelToken(placeDTO.getPreMetaTokenRelationDTO());
//        }
//
//        //编辑后续单据的关联信息
//        placeDTO.getMetaTokenRelationDTOS().forEach(metaTokenRelationDTO -> placeDTO.getRelationEntryMetaIds().forEach(masterMetaInfoDTO -> {
//            if (metaTokenRelationDTO.getTargetBillMetaId().equals(masterMetaInfoDTO.getEntryMasterMetaId())) {
//                switch (masterMetaInfoDTO.getMetaRelEnum()) {
////                        case MEMORANDVN:
////                            this.memorandums.add(new CacheRelToken(metaTokenRelationDTO));
////                            break;
//                    case CERTIFICATE:
//                        this.certificates.add(new CacheRelToken(metaTokenRelationDTO));
//                        break;
////                        case BUSINESSRECORD:
////                            this.businessRecord.add(new CacheRelToken(metaTokenRelationDTO));
////                            break;
//                    // 其他关联信息 TODO
//
//                    default:
//                        break;
//                }
//            }
//
//        }));
    }

    /**
     * 表单token提交时更新RelCacheDao 的 datas
     *
     * @param merklePlaceModel placeDTO
     * @return RelCacheDao
     */
    public RelCacheDao buildDatas(MerklePlaceModel merklePlaceModel) {
        this.datas = JSON.toJSONString(merklePlaceModel.getPlaceDTO().getDatas());
        return this;
    }

    public RelCacheDao cleanCertificates(String billTokenId) {
        this.setCertificates(
                new ArrayList<CacheRelToken>() {{
                    for (CacheRelToken cacheRelToken : certificates) {
                        if (cacheRelToken.getTokenId().equals(billTokenId)) {
                            add(cacheRelToken);
                        }
                    }
                }}
        );
        return this;
    }

    /**
     * 清洗datas中的分录信息，并更新datas
     *
     * @param entryMetaId  分录metaId
     * @param entryTokenId 分录tokenId
     * @return K:meta id,V:token list
     */
    public Map<String, List<TokenDataDto>> cleanDatasByEntryTokenId(String entryMetaId, String entryTokenId) {
        Map<String, List<TokenDataDto>> datas = JSON.parseObject(
                this.getDatas()
                , new TypeReference<HashMap<String, List<TokenDataDto>>>() {
                }
        );
        datas.put(entryMetaId, new ArrayList<TokenDataDto>() {{
            datas.get(entryMetaId).forEach(tokenDataDto -> {
                if (!tokenDataDto.getTokenId().equals(entryTokenId)) {
                    add(tokenDataDto);
                }
            });
        }}); // 清洗数据
        this.setDatas(JSON.toJSONString(datas));    //  更新datas
        return datas;
    }


}
