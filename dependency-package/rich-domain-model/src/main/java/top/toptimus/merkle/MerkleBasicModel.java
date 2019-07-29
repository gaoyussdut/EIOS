package top.toptimus.merkle;

import lombok.Getter;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.merkle.baseModel.MerkleBaseModel;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.Map;

/**
 * 基础资料备查账merkleModel
 */
public class MerkleBasicModel extends MerkleBaseModel {

    private static final long serialVersionUID = 5794782823176936825L;

    @Getter
    private TokenDataPageableDto tokenDataPageableDto;

    /**
     * 基础资料备查账数据初始化
     *
     * @param tokenDataPageableDto 数据
     * @param mainTableMap         K:metaId V:mainTable
     */
    public MerkleBasicModel(TokenDataPageableDto tokenDataPageableDto, Map<String, MainTable> mainTableMap) {
        super(mainTableMap);
        this.tokenDataPageableDto = tokenDataPageableDto;
        this.mainTableMap = mainTableMap;
    }

}
