package top.toptimus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.enums.StockTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.StockEventEntity;
import top.toptimus.entity.query.StockQueryFacadeEntity;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.stock.StockDetailDTO;

import java.util.List;

/**
 * 库存服务
 */
@Service
public class StockService {

    @Autowired
    private StockEventEntity stockEventEntity;
    @Autowired
    private StockQueryFacadeEntity stockQueryFacadeEntity;

    /**
     * 获取库存明细帐
     *
     * @param stockTypeEnum 出入库类型
     * @return  库存明细帐
     */
    public Result findStockDetailByType(StockTypeEnum stockTypeEnum) {
        try {
            return Result.success(
                    stockQueryFacadeEntity.findStockDetailByType(stockTypeEnum)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据表头信息和入出库类型查询库存明细
     *
     * @param billMetaId     表头MetaId
     * @param billTokenId    表头TokenId
     * @param entryMetaId    分录MetaId
     * @param stockTypeEnum  入出库类型
     * @return Result
     */
    public Result findStockDetailByContract(String billMetaId, String billTokenId, String entryMetaId ,StockTypeEnum stockTypeEnum) {
        try {
            return Result.success(
                    stockQueryFacadeEntity.findStockDetailByContract(billMetaId, billTokenId ,entryMetaId ,stockTypeEnum )
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 插入库存明细帐
     *
     * @param stockDetailDTOS 库存明细
     * @return  处理结果
     */
    public Result saveAllStockDetailDTO(List<StockDetailDTO> stockDetailDTOS) {
        try {
            stockEventEntity.saveAllStockDetailDTO(stockDetailDTOS);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 更新库存如出库类型
     *
     * @param ids  数据Id
     * @return  处理结果
     */
    public Result updateType(List<String> ids) {

        try {
            stockEventEntity.updateType(ids);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

}
