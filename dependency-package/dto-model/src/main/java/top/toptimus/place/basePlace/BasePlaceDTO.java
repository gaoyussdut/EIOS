package top.toptimus.place.basePlace;

import lombok.Getter;
import lombok.Setter;
import top.toptimus.common.enums.event.PlaceBusinessContextEnum;
import top.toptimus.tokendata.TokenDataDto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库所基类，包含库所关键信息
 * 库所的生命周期很短，用于：
 * 1、两段式提交的第一步 2、给前端提供闭包的业务字段描述，主要是meta和数据
 *
 * @author gaoyu
 * @since 2018-11-30
 */
@Getter
@Setter
public abstract class BasePlaceDTO implements Serializable {
    protected String billMetaId;
    protected String billTokenId;
    protected PlaceBusinessContextEnum placeBusinessContextEnum = PlaceBusinessContextEnum.NONE;// place保存类型
    protected Map<String, List<TokenDataDto>> datas = new HashMap<>();  //  K: meta id , V: token list
    protected String userId; // 操作用户Id

    //  -----------------------------
    //  构造函数
    //  -----------------------------

    /**
     * 构建place保存类型，如果为空就是默认值
     */
    protected BasePlaceDTO() {
        this.buildPlaceBusinessContextEnum();   //  构建place保存类型，如果为空就是默认值
    }

    /**
     * 生成PlaceDTO
     *
     * @param billTokenId              表头token id
     * @param datas                    数据
     * @param billMetaId               billMetaid
     * @param placeBusinessContextEnum place库所应用上下文枚举
     */
    protected BasePlaceDTO(
            String billTokenId,
            Map<String, List<TokenDataDto>> datas
            , String billMetaId
            , PlaceBusinessContextEnum placeBusinessContextEnum
    ) {
        this.billTokenId = billTokenId;
        this.datas = datas;
        this.billMetaId = billMetaId;
        this.placeBusinessContextEnum = placeBusinessContextEnum;
        this.buildPlaceBusinessContextEnum();   //  构建place保存类型，如果为空就是默认值
    }

    /**
     * 构建place数据,只做数据呈现，不需要bill token id
     *
     * @param datas K：meta id，V:token数据
     */
    protected BasePlaceDTO(Map<String, List<TokenDataDto>> datas) {
        this.datas = datas;
        this.buildPlaceBusinessContextEnum();  //  构建place保存类型，如果为空就是默认值
    }

    /**
     * 构建place数据
     *
     * @param datas K：meta id，V:token数据
     */

    protected BasePlaceDTO(String billTokenId, Map<String, List<TokenDataDto>> datas) {
        this.billTokenId = billTokenId;
        this.datas = datas;
        this.buildPlaceBusinessContextEnum();  //  构建place保存类型，如果为空就是默认值
    }

    /**
     * 构建place数据，不带data
     *
     * @param billMetaId  billMetaid
     * @param billTokenId 表头token id
     */
    protected BasePlaceDTO(String billMetaId, String billTokenId) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.buildPlaceBusinessContextEnum();  //  构建place保存类型，如果为空就是默认值
    }

    //  -----------------------------
    //  build方法
    //  -----------------------------

    /**
     * 构建place保存类型，如果为空就是默认值
     */
    private void buildPlaceBusinessContextEnum() {
        if (null == this.placeBusinessContextEnum) {
            this.placeBusinessContextEnum = PlaceBusinessContextEnum.NONE;
        }
    }

    /**
     * 根据meta id更新token数据
     *
     * @param metaId        meta id
     * @param tokenDataDtos token数据列表
     */
    public void buildTokenDatasByMetaId(String metaId, List<TokenDataDto> tokenDataDtos) {
        this.getDatas().put(metaId, tokenDataDtos);
    }

    /**
     * 根据meta id清洗datas
     *
     * @param metaId meta id
     */
    protected void buildMetaId(String metaId) {
        for (String allMetaId : this.getDatas().keySet()) {
            if (allMetaId.equals(metaId)) {
                this.datas.put(metaId, this.getDatas().get(metaId));
            }
        }
    }

    /**
     * 返回表头token数据
     *
     * @return token数据
     */
    public TokenDataDto getBillToken() {
        for (TokenDataDto tokenDataDto : this.datas.get(billMetaId)) {
            if (tokenDataDto.getTokenId().equals(this.billTokenId))
                return tokenDataDto;
        }
        throw new RuntimeException("表头token不存在");
    }
}
