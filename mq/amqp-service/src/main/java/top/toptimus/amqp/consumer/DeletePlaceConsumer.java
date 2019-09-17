package top.toptimus.amqp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.place.place_deprecated.BillTokenDelResultDTO;
import top.toptimus.place.place_deprecated.PlaceReduceDTO;

@Component
public class DeletePlaceConsumer {
    @Autowired
    private TokenEventEntity tokenEventEntity;

    private static final Logger logger = LoggerFactory.getLogger(DeletePlaceConsumer.class);

    /**
     * 删除分录
     *
     * @param billTokenDelResultDTO 库所删除结果
     */
    @JmsListener(destination = "deleteEntryTokenData.delete")
    public void deleteEntryToken(BillTokenDelResultDTO billTokenDelResultDTO) {
//        try {
//            // 1.删除分录ES关系
//            tokenEventEntity.delRelByEntryTokenId(billTokenDelResultDTO.getBillTokenResultBody().getTokenId());
//            // 2.删除分录的数据
////            tokenQueryFacadeEntity.deleteTokenDatas(new TokenDeleteModel(billTokenDelResultDTO.getBillTokenResultBody().getMetaId(), new ArrayList<String>(){{
////                add(billTokenDelResultDTO.getBillTokenResultBody().getTokenId());
////            }}));
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("失败！");
//        }
    }

    /**
     * 删除单据关系
     * 仅仅删除其作为
     *
     * @param placeReduceDTO 库所精简信息
     */
    @JmsListener(destination = "deleteBillTokenData.delete")
    public void deleteBillToken(PlaceReduceDTO placeReduceDTO) {
        try {
            // 1.删除ES关系
//            tokenEventEntity.delRelByBillTokenId(placeReduceDTO.getBillTokenId()); // 1.删除表头ES关系
            tokenEventEntity.delRelByEntryTokenId(placeReduceDTO.getBillTokenId()); //1.删除表头和其他单据的关系ES关系

            // 2.删除数据
            // 2.1 删除表头数据
//            tokenQueryFacadeEntity.deleteTokenDatas(new TokenDeleteModel(placeReduceDTO.getBillMetaId(), new ArrayList<String>(){{
//                add(placeReduceDTO.getBillTokenId());
//            }}));
//            // 2.2.删除分录数据
//            for (String metaId : placeReduceDTO.getEntryIds().keySet()) {
//                // 根据分录删除数据
//                tokenQueryFacadeEntity.deleteTokenDatas(new TokenDeleteModel(metaId, placeReduceDTO.getEntryIds().get(metaId)));
//            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("失败！");
        }
    }

}

