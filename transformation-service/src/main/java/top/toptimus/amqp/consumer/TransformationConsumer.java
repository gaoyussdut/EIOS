package top.toptimus.amqp.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.toptimus.entity.event.transformation.TransformationEventEntity;
import top.toptimus.entity.query.RuleQueryFacadeEntity;
import top.toptimus.transformation.TransformationDTO;

@Component
public class TransformationConsumer {

    @Autowired
    private TransformationEventEntity transformationEventEntity;
    @Autowired
    private RuleQueryFacadeEntity ruleQueryFacadeEntity;

    /**
     * 下推时单据转换规则
     *
     * @param transformationDTO 单据转换上下级单据索引信息
     */
    @JmsListener(destination = "transformation.pushdown")
    public TransformationDTO pushDownTransformation(TransformationDTO transformationDTO) {
        try {

            // 下推
//            transformationEventEntity.pushDownTansformation(
//                    transformationDTO
//                    , ruleQueryFacadeEntity.getGraphQLModel(transformationDTO)
//            );
            return transformationDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("单据转换失败！");
        }
    }

}
