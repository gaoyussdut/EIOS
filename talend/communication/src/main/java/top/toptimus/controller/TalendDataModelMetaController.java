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
@RequestMapping(value = "/talendData")
@Controller
public class TalendDataModelMetaController {

    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 分页查询data_model_pOJO，返回：K:talend主键，V:data model名
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return K:talend主键，V:data model名
     */
    @ApiOperation(value = "dataModel一览", notes = "")
    @RequestMapping(value = {"/getTalendDataModelMeta"}, method = RequestMethod.GET)
    public String getTalendDataModelMeta(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Destination destination = new ActiveMQQueue("talendDataModelMeta.queue");
        String requst = pageNo.toString() + "," + pageSize.toString();
        return jmsTemplate.convertSendAndReceive(destination, requst, String.class);
    }


}
