package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/**
 * Created by JiangHao on 2018/8/15.
 */
@Api(value = "", tags = "dataModel管理")
@RestController
@RequestMapping(value = "/talendReference")
@Controller
public class TalendReferenceController {


    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    @ApiOperation(value = "dataModel一览", notes = "")
    @RequestMapping(value = {"/getTalendReference"}, method = RequestMethod.GET)
    public String getTalendReference(@RequestParam String x_pk_x_talend_id, @RequestParam String referenceEntityTypeName) {
        Destination destination = new ActiveMQQueue("talendReference.queue");
        String requst = x_pk_x_talend_id + "," + referenceEntityTypeName;
        return jmsTemplate.convertSendAndReceive(destination, requst, String.class);
    }

}
