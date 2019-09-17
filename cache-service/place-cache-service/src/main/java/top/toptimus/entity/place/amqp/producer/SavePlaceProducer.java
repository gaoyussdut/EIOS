package top.toptimus.entity.place.amqp.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import top.toptimus.place.place_deprecated.BillTokenSaveResultDTO;


@Service
public class SavePlaceProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    // 保存placeDTO的消息队列
    //创建表单或提交分录
    private static final String SAVE_PLACE = "saveEntryTokenData.save";
    //提交表头数据
    private static final String SAVE_BILL_PLACE = "saveBillTokenData.save";

    /**
     * 创建表单或提交分录
     *
     * @param  billTokenSaveResultDTO
     */
    public void saveEntryToken(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        jmsMessagingTemplate.convertAndSend(SAVE_PLACE, billTokenSaveResultDTO);
    }

    /**
     * 提交表头
     *
     * @param  billTokenSaveResultDTO
     */
    public void saveBillToken(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        jmsMessagingTemplate.convertAndSend(SAVE_BILL_PLACE, billTokenSaveResultDTO);
    }
}
