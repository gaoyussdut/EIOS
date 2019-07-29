package top.toptimus.model.tokenData.query;

import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.tokendata.TokenDataDto;

import java.util.List;

/**
 * 数据清洗model
 *
 * @author gaoyu
 * @since 2018-6-29
 */
public class ReduceTokenModel extends TokenDataModel {
    public ReduceTokenModel(List<TokenDataDto> cleanTokenDataDtoList) {
        this.buildCleanTokenDataDto(cleanTokenDataDtoList);
    }
}
