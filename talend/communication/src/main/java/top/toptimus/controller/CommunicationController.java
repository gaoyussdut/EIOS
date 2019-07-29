package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;

/**
 * Created by JiangHao on 2018/8/2.
 */

@Api(value = "", tags = "dataModel管理")
@RestController
@RequestMapping(value = "/dataModel")
@Controller
public class CommunicationController {

    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 从talend model中生成数据视图meta
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名 token meta id，V:数据视图meta
     */
    @ApiOperation(value = "数据视图meta", notes = "")
    @RequestMapping(value = {"/getTokenMetaInfoDTOS"}, method = RequestMethod.GET)
    public String getTokenMetaInfoDTOS(@RequestParam String x_pk_x_talend_id) {
        Destination destination = new ActiveMQQueue("metaInfoDTO.queue");
        return jmsTemplate.convertSendAndReceive(destination, x_pk_x_talend_id, String.class);
    }
}
