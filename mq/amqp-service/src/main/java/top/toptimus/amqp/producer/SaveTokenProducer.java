package top.toptimus.amqp.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import top.toptimus.place.place_deprecated.PlaceAlterDto;

@Service
public class SaveTokenProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    // 保存Token的消息队列
    private static final String SAVE_TOKEN = "saveToken.save";

    /**
     * 根据placeSaveResultDTO从redis当中取PlaceDTO并保存PlaceDTO
     *
     * @param  placeAlterDto  库所保存结果
     */
    public void saveToken(PlaceAlterDto placeAlterDto) {
        jmsMessagingTemplate.convertAndSend(SAVE_TOKEN, placeAlterDto);
    }
}
