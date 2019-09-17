package top.toptimus.model.place;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.dao.place.cache.PlaceSession;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.place.PlaceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据线程缓存
 */
@Getter
@NoArgsConstructor
public class PlaceSessionModel extends BaseModel {
    private static List<PlaceSession> placeSessions = new ArrayList<>();   //  单据缓存列表，带单据体、时间戳、auth id、session id

    /**
     * 判断当前操作的会话是否存在，若存在返回对应的PlaceSession，否则返回null
     *
     * @param authId authId
     * @return PlaceSession
     */
    public PlaceSession authIdExist(String authId) {
        if (null != placeSessions && placeSessions.size() > 0) {
            for (PlaceSession placeSession : placeSessions) {
                if (placeSession.getAuthId().equals(authId)) {
                    return placeSession;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 根据表头token id和authid取得库所
     * 这里维护的是基础库所信息，如果需要其他信息，重载
     * 如果缓存中不存在中不存在 返回空
     *
     * @param billTokenId 表头token id
     * @return 库所
     */
    public MerklePlaceModel findPlace(String billTokenId, String authId) {
        PlaceSession placeSession = this.authIdExist(authId);   //  根据authId找placeSession
        if (null != placeSession) {
            //  根据billTokenid再placeSession中找PlaceDTO
            for (MerklePlaceModel merklePlaceModel : placeSession.getMerklePlaceModels()) {
                if (merklePlaceModel.getPlaceDTO().getBillTokenId().equals(billTokenId)) {
                    //如果存在，返回查询到的PlaceDTO
                    return merklePlaceModel;
                }
            }
        }
        //否则返回空
        return null;
    }

    /**
     * 将库所存入session
     *
     * @param placeDTO 库所
     * @param authId   session id
     * @return 库所model，带缓存主键
     */
    public MerklePlaceModel savePlaceIntoSession(PlaceDTO placeDTO, String authId) {
        PlaceSession placeSession = this.authIdExist(authId);
        /*
        存place session
         */
        //  如果当前会话存在
        if (null != placeSession) {
            //判断PlaceDTO在当前placeSession中是否存在
            return placeSession.updateMerkleById(placeDTO);
            //如果当前会话不存在
        } else {
            placeSession = new PlaceSession(placeDTO);
            placeSessions.add(placeSession);
            return placeSession.getMerkleByTokenId(placeDTO.getBillTokenId());
        }
    }
}
