package top.toptimus.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.place.PlaceDTO;
import top.toptimus.rule.formula.MetaAndFkeyDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单据转换DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransformationDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8632293587675498365L;

    private String ruleType;  //规则类型
    private String ruleMetaId;  //规则meta
    private PlaceDTO currentPlaceDTO;
    private PlaceDTO prePlaceDTO;
    private Map<String, List<TokenDataDto>> transformationDatas = new HashMap<>(); // 转换后其他单据数据，K：meta id， V：token列表

    public TransformationDTO(String ruleType, String ruleMetaId, PlaceDTO prePlaceDTO, PlaceDTO currentPlaceDTO) {
        this.ruleType = ruleType;
        this.ruleMetaId = ruleMetaId;
        this.prePlaceDTO = prePlaceDTO;
        this.currentPlaceDTO = currentPlaceDTO;
    }

    /**
     * 替换公式中的字段为数值
     *
     * @param params K:公式字段，fkey, v: 源单meta和fkey
     * @return K:公式字段, V:json data
     */
    public Map<String, Object> buildExplainFormula(Map<String, MetaAndFkeyDTO> params, Map<String, List<TokenDataDto>> datas) {
        return new HashMap<String, Object>() {{
            params.keySet().forEach(param -> {
                if (currentPlaceDTO.getBillMetaId().equals(params.get(param).getMetaId())) {
                    //  表头
                    datas.get(params.get(param).getMetaId()).get(0).getFields().forEach(fkeyField -> {
                        if (fkeyField.getKey().equals(params.get(param).getKey())) {
                            put(param, fkeyField.getJsonData());
                        }
                    });
                } else {
                    //  分录
                    for (TokenDataDto tokenDataDto : datas.get(params.get(param).getMetaId())) {
                        tokenDataDto.getFields().forEach(fkeyField -> {
                            if (fkeyField.getKey().equals(params.get(param).getKey())) {
                                put(param, fkeyField.getJsonData());
                            }
                        });
                    }
                }
            });
        }};
    }

//    /**
//     * 更新数据
//     *
//     * @param currentPlaceDTO 当前单据
//     * @param prePlaceDTO     源单
//     * @return 单据转换DTO
//     */
//    public TransformationDTO build(PlaceDTO currentPlaceDTO, PlaceDTO prePlaceDTO) {
//        List<TokenDataDto> preDatas = prePlaceDTO.getDatas().get();
//        // 获取当前单据数据
//        List<TokenDataDto> curDatas = currentPlaceDTO.getDatas().get(
//                prePlaceDTO.getBillMetaid() //  前置单据的meta
//        );
//        this.getCurrentDatas().put(
//                prePlaceDTO.getBillMetaid() //  前置单据的meta
//                , curDatas
//        );
//        this.getPreDatas().put(this.getPreMetaTokenRelationDTO().getSourceBillMetaId(), preDatas);
//        return this;
//    }

//    /**
//     * 根据主数据清洗单据转换DTO
//     *
//     * @param businessCode         事务码
//     * @param currentCachePlaceDTO 缓存中的当前单据DTO
//     * @param preCachePlaceDTO     缓存中的源单
//     * @return this
//     */
//    public TransformationDTO build(
//            String businessCode
//            , PlaceDTO currentCachePlaceDTO
//            , PlaceDTO preCachePlaceDTO
//    ) {
//        // 获取业务编码
//        this.build(currentCachePlaceDTO, preCachePlaceDTO); //  更新数据
//        return this;
//    }
}
