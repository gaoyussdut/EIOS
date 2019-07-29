package top.toptimus.entity.event;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.repository.StockDetailRepository;
import top.toptimus.stock.StockDetailDTO;

import java.util.List;
import java.util.UUID;

/**
 * Stock event
 *
 */
@Component
public class StockEventEntity {

    @Autowired
    private StockDetailRepository stockDetailRepository;

    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;


    /**
     * 插入库存明细帐
     *
     * @param stockDetailDTOS 库存明细
     */
    public void saveAllStockDetailDTO(List<StockDetailDTO> stockDetailDTOS) {
        String userId = userQueryFacadeEntity.findByAccessToken().getId();
        for (StockDetailDTO stockDetailDTO : stockDetailDTOS) {
            stockDetailDTO.setCreateUser(userId); // 设置用户ID
            if(StringUtils.isEmpty(stockDetailDTO.getId())) {
                stockDetailDTO.setId(UUID.randomUUID().toString());
            }
        }
        stockDetailRepository.saveAll(stockDetailDTOS);
    }

    /**
     * 更新库存如出库类型
     *
     * @param ids     数据Id
     */
    public void updateType(List<String> ids) {
        String userId = userQueryFacadeEntity.findByAccessToken().getId();
        stockDetailRepository.updateType(ids ,userId);
    }


}
