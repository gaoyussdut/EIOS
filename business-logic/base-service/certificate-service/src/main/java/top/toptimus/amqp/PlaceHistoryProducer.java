package top.toptimus.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import top.toptimus.place.PlaceDTO;
import top.toptimus.place.PlacePDFDTO;

import java.util.UUID;

/**
 * 表单历史数据生产者
 * Created by JiangHao on 2018/10/30.
 */
@Service("producer")
public class PlaceHistoryProducer {

    // 保存历史记录的消息队列
    private static final String SAVE_PLACE_DESTINATION = "savePlaceHistory.save";
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 生成提交的历史数据和单据的PDF
     *
     * @param placeDTO 表单
     */
    public void savePlaceHistory(PlaceDTO placeDTO) {
        jmsMessagingTemplate.convertAndSend(SAVE_PLACE_DESTINATION, new PlacePDFDTO(
                UUID.randomUUID().toString()
                , placeDTO.getBillTokenId()
                , String.valueOf(System.currentTimeMillis() / 1000)
                , placeDTO
        ));
    }

}
