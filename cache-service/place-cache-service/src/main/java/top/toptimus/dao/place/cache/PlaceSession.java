package top.toptimus.dao.place.cache;

import lombok.Getter;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 单据缓存
 */
@Getter
public class PlaceSession {
    private String sessionId;
    private String authId;
    private List<MerklePlaceModel> merklePlaceModels = new ArrayList<>();
    private long timeStamp; //  时间戳

    /**
     * 生成session对象
     *
     * @param placeDTO 库所
     */
    public PlaceSession(PlaceDTO placeDTO) {
        this.sessionId = UUID.randomUUID().toString();
        this.authId = placeDTO.getUserId();
        this.merklePlaceModels.add(new MerklePlaceModel(placeDTO));
        this.timeStamp = DateTimeUtil.currentTimeStamp();
    }

    /**
     * 根据token id从session中取库所
     *
     * @param billTokenId 表头token id
     * @return merkle
     */
    public MerklePlaceModel getMerkleByTokenId(String billTokenId) {
        for (MerklePlaceModel merklePlaceModel : this.merklePlaceModels) {
            if (billTokenId.equals(merklePlaceModel.getPlaceDTO().getBillTokenId()))
                return merklePlaceModel;
        }
        return null;    //  TODO，自定义exception
    }

    /**
     * 根据表头token id更新数据
     *
     * @param billTokenId 表头token id
     * @param datas       数据
     */
    public void updateDatasByBillTokenId(String billTokenId, Map<String, List<TokenDataDto>> datas) {
        this.getMerkleByTokenId(billTokenId).getPlaceDTO().setDatas(datas);
    }

    /**
     * 根据缓存id从session中取库所
     *
     * @param id redis主键
     * @return merkle
     */
    public MerklePlaceModel getMerkleById(String id) {
        for (MerklePlaceModel merklePlaceModel : this.merklePlaceModels) {
            if (id.equals(merklePlaceModel.getCacheId()))
                return merklePlaceModel;
        }
        return null;    //  TODO，自定义exception
    }

    /**
     * 更新单据
     *
     * @param placeDTO 库所
     * @return 库所model，带缓存主键
     */
    public MerklePlaceModel updateMerkleById(PlaceDTO placeDTO) {
        MerklePlaceModel merklePlaceModel = this.getMerkleByTokenId(placeDTO.getBillTokenId());
        if (null != merklePlaceModel) {
            //  更新
            return merklePlaceModel.build(placeDTO);    //  单据变更
        } else {
            merklePlaceModel = new MerklePlaceModel(placeDTO);  //  新增单据
            this.merklePlaceModels.add(merklePlaceModel);
            return merklePlaceModel;
        }
    }

}
