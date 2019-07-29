package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.StockTypeEnum;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.repository.StockDetailRepository;
import top.toptimus.stock.StockDetailDTO;

import java.util.List;

/**
 * Stock Query
 *
 */
@Component
public class StockQueryFacadeEntity {

    @Autowired
    private TokenEventEntity tokenEventEntity;

    @Autowired
    private StockDetailRepository stockDetailRepository;

    /**
     * 获取库存明细帐
     *
     * @param stockTypeEnum 出入库类型
     * @return  库存明细帐
     */
    public List<StockDetailDTO> findStockDetailByType(StockTypeEnum stockTypeEnum) {
        return stockDetailRepository.findStockDetailByType(stockTypeEnum);
    }

    /**
     * 根据表头信息和入出库类型查询库存明细
     *
     * @param billMetaId     表头MetaId
     * @param billTokenId    表头TokenId
     * @param entryMetaId    分录MetaId
     * @param stockTypeEnum  入出库类型
     * @return List<StockDetailDTO>
     */
    public List<StockDetailDTO> findStockDetailByContract( String billMetaId,String billTokenId,String entryMetaId,StockTypeEnum stockTypeEnum) {
        List<String> tokenIds = tokenEventEntity.findRel(billTokenId, billMetaId, entryMetaId);
        return stockDetailRepository.findStockDetailByContract(tokenIds ,stockTypeEnum);
    }


}
