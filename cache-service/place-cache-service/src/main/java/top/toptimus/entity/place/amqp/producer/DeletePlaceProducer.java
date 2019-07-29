package top.toptimus.entity.place.amqp.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import top.toptimus.place.BillTokenDelResultDTO;
import top.toptimus.place.BillTokenSaveResultDTO;
import top.toptimus.place.PlaceReduceDTO;


@Service
public class DeletePlaceProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    // 删除分录
    private static final String DELETE_PLACE_ENTRY = "deleteEntryTokenData.delete";
    // 删除单据
    private static final String DELETE_PLACE_BILL = "deleteBillTokenData.delete";

    /**
     * 删除分录信息
     *
     * @param  billTokenDelResultDTO  库所删除结果
     */
    public void deleteEntryData(BillTokenDelResultDTO billTokenDelResultDTO) {
        jmsMessagingTemplate.convertAndSend(DELETE_PLACE_ENTRY, billTokenDelResultDTO);
    }

    /**
     * 删除单据信息
     *
     * @param  placeReduceDTO  库所信息
     */
    public void deleteBillData(PlaceReduceDTO placeReduceDTO){
        jmsMessagingTemplate.convertAndSend(DELETE_PLACE_BILL, placeReduceDTO);
    }

}
