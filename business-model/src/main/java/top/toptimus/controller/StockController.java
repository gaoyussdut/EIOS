package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.enums.StockTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.service.StockService;
import top.toptimus.stock.StockDetailDTO;

import java.util.List;

/**
 * 库存接口
 *
 */
@Api(value = "库存接口", tags = "库存管理")
@RestController
@RequestMapping(value = "/stock")
@Controller
public class StockController {

    @Autowired
    private StockService stockService;


    /**
     * 保存库存明细信息
     *
     * @param stockDetailDTOS
     * @return Result
     */
    @PostMapping(value = "/saveStockDetail")
    public Result saveAllStockDetailDTO(@RequestBody List< StockDetailDTO > stockDetailDTOS) {
        return stockService.saveAllStockDetailDTO(stockDetailDTOS);
    }

    /**
     * 根据入出库类型查询库存明细
     *
     * @param stockTypeEnum 入出库类型
     * @return Result
     */
    @ApiOperation(value = "根据入出库类型查询库存明细")
    @GetMapping(value = "/getStockDetail")
    public Result findStockDetailByType(@RequestParam StockTypeEnum stockTypeEnum) {
        return stockService.findStockDetailByType(stockTypeEnum);
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
    @ApiOperation(value = "根据入出库类型查询库存明细")
    @GetMapping(value = "/getStockDetailByContract")
    public Result findStockDetailByContract(@RequestParam String billMetaId,
                                            @RequestParam String billTokenId,
                                            @RequestParam String entryMetaId,
                                            @RequestParam StockTypeEnum stockTypeEnum) {
        return stockService.findStockDetailByContract(billMetaId, billTokenId, entryMetaId, stockTypeEnum);
    }

    /**
     * 根据数据Id更新出入库明细的类型
     *
     * @param ids  数据Id
     * @return Result
     */
    @PostMapping(value = "/updateStockDetail")
    public Result updateStockDetailType(@RequestBody List<String> ids) {
        return stockService.updateType(ids);
    }

}
