package top.toptimus.activemq;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.toptimus.entity.DataModelEntity;
import top.toptimus.service.DataModelRuntimeService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
public class Consumer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataModelEntity dataModelEntity;

    @Autowired
    private DataModelRuntimeService dataModelRuntimeService;


    /**
     * 从talend model中生成数据视图meta
     * 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名 token meta id，V:数据视图meta
     */
    @JmsListener(destination = "metaInfoDTO.queue")
    public String getTokenMetaInfoDTOS(String x_pk_x_talend_id) throws IOException {
        logger.info("请求数据:" + x_pk_x_talend_id);
        return JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTOS(x_pk_x_talend_id));
    }


    /**
     * 分页查询data_model_pOJO，返回：K:talend主键，V:data model名
     *
     * @param requst 页码和页宽
     * @return K:talend主键，V:data model名
     */
    @JmsListener(destination = "talendDataModelMeta.queue")
    public String getTalendDataModelMeta(String requst) {
        logger.info("请求数据:" + requst);
        List<String> pageNoAndPageSizes = Arrays.asList(requst.split(","));
        return JSON.toJSONString(dataModelEntity.getTalendDataModelMeta(Integer.valueOf(pageNoAndPageSizes.get(0)), Integer.valueOf(pageNoAndPageSizes.get(1))));
    }

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    @JmsListener(destination = "talendReference.queue")
    public String getTalendReference(String requst) {
        logger.info("请求数据:" + requst);
        List<String> pageNoAndPageSizes = Arrays.asList(requst.split(","));
        try {
            return JSON.toJSONString(dataModelRuntimeService.getTalendReference(pageNoAndPageSizes.get(0), pageNoAndPageSizes.get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
