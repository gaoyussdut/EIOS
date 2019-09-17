package top.toptimus.place.place_deprecated;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.common.enums.event.PlaceBusinessContextEnum;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.MetaRelation.MasterMetaInfoDTO;
import top.toptimus.place.basePlace.BasePlaceDTO;
import top.toptimus.place.placeSaveResult.PlaceSaveResultBody;
import top.toptimus.relation.MetaTokenRelationDTO;
import top.toptimus.relation.PreMetaTokenRelationDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.transformation.TransformationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * TODO 等待拆分
 * 不同业务使用的库所并不相同，因为业务的关键信息不同
 * 例如wbs中需要包含lot no，lot self id，wbs ins id；lot parent id酌情考虑是否加入
 * 流程定义中要包含user task id，process id，po token id
 * 以上是两种库所定义。纯单据提交是第三种，三个dto完全没必要长一样，冗余太重了
 * <p>
 * 另外，org id可能全程都要被包含。后期考虑加入用户令牌的id
 * token template name也存疑，什么业务中加入
 *
 * @author gaoyu
 * @since 2018-6-28
 */
@NoArgsConstructor
@Setter
@Getter
@Deprecated
public class PlaceDTO extends BasePlaceDTO {

    private static final long serialVersionUID = 6588124626387054060L;

    private PreMetaTokenRelationDTO preMetaTokenRelationDTO;//前置单据关联信息
    private String tokenTemplateName; // TTname赋值,如果是代办传空，是AO的话需要传值前端显示
    private List<MasterMetaInfoDTO> relationEntryMetaIds = new ArrayList<>(); //关联单据的metaid和关联类型（不包含分录meta）
    private List<MetaTokenRelationDTO> metaTokenRelationDTOS = new ArrayList<>(); //  关系
    private String userTaskId; // bpmn

    //  -----------------------------
    //  构造函数
    //  -----------------------------
    public PlaceDTO build(List<MetaTokenRelationDTO> metaTokenRelationDTOS) {
        this.metaTokenRelationDTOS = new ArrayList<>(metaTokenRelationDTOS);
        return this;
    }

    /**
     * 构造前置单据信息
     *
     * @param preBillTokenId 前置单据表头tokenid
     * @param preMetaId      前置单据metaid
     */
    public void buildpreMetaTokenRelation(String preBillTokenId, String preMetaId) {

        this.preMetaTokenRelationDTO = new PreMetaTokenRelationDTO(
                UUID.randomUUID().toString()
                , preMetaId
                , preBillTokenId
        );
    }

    /**
     * 构造前置单据信息
     *
     * @param metaTokenRelationDTOS 记录前置单据和当前单据之间的token关系
     * @return PlaceDTO
     */
    public PlaceDTO buildpreMetaTokenRelation(List<PreMetaTokenRelationDTO> metaTokenRelationDTOS) {
        //无前置单据
        if (metaTokenRelationDTOS.size() == 0) {
            return this;
        }
        //前置单据数量异常
        else if (metaTokenRelationDTOS.size() > 1) {
            throw new TopException(TopErrorCode.PLACE_PRE_BILL_ERR);
        }
        //存在前置单据
        else {
            this.preMetaTokenRelationDTO = new PreMetaTokenRelationDTO(
                    metaTokenRelationDTOS.get(0).getId()
                    , metaTokenRelationDTOS.get(0).getSourceBillMetaId()
                    , metaTokenRelationDTOS.get(0).getSourceBillTokenId()
            );
            return this;
        }

    }

    /**
     * 表单token提交（表头分录都一样）
     *
     * @param tokenDataDto 数据
     * @param metaId       meta
     * @param userId       用户id
     */
    public PlaceDTO build(TokenDataDto tokenDataDto, String metaId, String userId) {
        if (StringUtils.isEmpty(tokenDataDto.getTokenId()))
            tokenDataDto.build(UUID.randomUUID().toString());   //  新增分录的时候token id为空
        //如果表头tokenid和TokenDataDto的tokenid相同，说明是表头提交
        //表头提交
        if (this.billTokenId.equals(tokenDataDto.getTokenId())) {
            this.datas.remove(metaId);
            this.datas.put(metaId, Lists.newArrayList(tokenDataDto));
        }
        //分录提交
        else {
            //更新原分录数据
            if (this.datas.containsKey(metaId)) {
                for (int i = 0; i < this.datas.get(metaId).size(); i++) {
                    if (this.datas.get(metaId).get(i).getTokenId().equals(tokenDataDto.getTokenId())) {
                        this.datas.get(metaId).remove(i);
                        break;
                    }
                }
                this.datas.get(metaId).add(tokenDataDto);
            } else {
                this.datas.put(metaId, Lists.newArrayList(tokenDataDto));
            }
        }

        if (StringUtils.isEmpty(this.userId) || this.userId.equals(userId)) {
            this.userId = userId;
        } else {
            throw new RuntimeException("该单据已更新，无法提交数据");
        }
        this.placeBusinessContextEnum = PlaceBusinessContextEnum.PLACE_DATA_SAVE;
        return this;
    }

    //  -----------------------------
    //  TODO    下面的基本都是废的
    //  -----------------------------

    /**
     * 构造方法（仅构造索引）
     *
     * @param billTokenId       表头token id
     * @param tokenTemplateId   ttid
     * @param tokenTemplateName tt name
     */
    public PlaceDTO(String billTokenId, String tokenTemplateId, String tokenTemplateName, String userId) {
        super(tokenTemplateId, billTokenId);
        this.tokenTemplateName = tokenTemplateName;
        this.userId = userId;
    }

    /**
     * 构造
     *
     * @param metaId        表头metaid
     * @param billTokenId   billTokenId
     * @param billTokenData billTokenData
     */
    public PlaceDTO(String metaId, String billTokenId, TokenDataDto billTokenData) {
        this.billMetaId = metaId;
        this.billTokenId = billTokenId;
        this.datas.put(metaId, new ArrayList<TokenDataDto>() {
            private static final long serialVersionUID = -1180561097392820877L;

            {
                add(billTokenData);
            }
        });
    }

    /**
     * 构造
     *
     * @param metaId        表头metaid
     * @param billTokenData billTokenData
     */
    public PlaceDTO(String metaId, TokenDataDto billTokenData) {
        this.billMetaId = metaId;
        this.billTokenId = billTokenData.getTokenId();
        this.datas.put(metaId, new ArrayList<TokenDataDto>() {

            private static final long serialVersionUID = 1563767562589956971L;

            {
                add(billTokenData);
            }
        });
    }

    /**
     * 构建place数据
     *
     * @param datas K：meta id，V:token数据
     */
    public PlaceDTO(String billTokenId, Map<String, List<TokenDataDto>> datas) {
        super(billTokenId, datas);
    }

    /**
     * 构建place数据，不带data
     *
     * @param tokenTemplateId ttid
     * @param billTokenId     表头token id
     */
    public PlaceDTO(String tokenTemplateId, String billTokenId) {
        super(tokenTemplateId, billTokenId);    //  构建place数据，不带data
    }

    //  -----------------------------
    //  build方法
    //  -----------------------------

    /**
     * 根据meta id清洗datas
     *
     * @param metaId meta id
     */
    public void buildMetaId(String metaId) {
        super.buildMetaId(metaId);
    }

    /**
     * 根据meta id更新token数据
     *
     * @param metaId        meta id
     * @param tokenDataDtos token数据列表
     */
    public void buildTokenDatasByMetaId(String metaId, List<TokenDataDto> tokenDataDtos) {
        super.buildTokenDatasByMetaId(metaId, tokenDataDtos);   //  根据meta id更新token数据
    }

    /*
     * 根据meta id更新token数据
     *
     * @param datas token data list
     * @return this
     */
//    public PlaceDTO buildTokenDatas(Map<String, List<TokenDataDto>> datas) {
//        this.datas = datas;
//        return this;
//    }

    /**
     * 根据数据库索引类型构建索引
     *
     * @param placeSaveResultBody 库所更新返回值
     * @return 库所
     */
    public PlaceDTO build(PlaceSaveResultBody placeSaveResultBody) {
        super.billTokenId = StringUtils.isEmpty(placeSaveResultBody.getBillTokenId()) ? super.billTokenId : placeSaveResultBody.getBillTokenId();
        super.billMetaId = StringUtils.isEmpty(placeSaveResultBody.getBillTokenId()) ? super.billMetaId : placeSaveResultBody.getBillMetaId();
        this.tokenTemplateName = StringUtils.isEmpty(placeSaveResultBody.getTokenTemplateName()) ? this.getTokenTemplateName() : placeSaveResultBody.getTokenTemplateName();
        this.placeBusinessContextEnum = placeSaveResultBody.getPlaceBusinessContextEnum() == null ? this.placeBusinessContextEnum : placeSaveResultBody.getPlaceBusinessContextEnum();
        return this;
    }

    /**
     * 校验数据，如果tokenId不存在报错
     * 如果tokenId为空 存入了redis  会取不到数据
     */
    public void intendedEffectData() {
        Map<String, List<TokenDataDto>> datas = this.datas;
        for (String metaId : datas.keySet()) {
            for (TokenDataDto tokenDataDto : datas.get(metaId)) {
                if (Strings.isNullOrEmpty(tokenDataDto.getTokenId())) {
                    throw new TopException(TopErrorCode.TOKEN_ID_NOT_EXIST);
                }
            }
        }
    }

    /**
     * 构造userid
     *
     * @param userId userId
     * @return PlaceDTO
     */
    public PlaceDTO buildUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 根据单据转换结果清洗数据
     *
     * @param transformationDTO 单据转换DTO
     * @return this
     */
    public PlaceDTO buildTransition(TransformationDTO transformationDTO) {
        if(!transformationDTO.getTransformationDatas().containsKey(transformationDTO.getCurrentPlaceDTO().getBillMetaId())){
            this.datas.put(transformationDTO.getCurrentPlaceDTO().getBillMetaId(),Lists.newArrayList(new TokenDataDto().build(transformationDTO.getCurrentPlaceDTO().getBillTokenId())));
        }else this.datas = transformationDTO.getTransformationDatas();
        return this;
    }
}
