//package top.toptimus.entity.aop;
//
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import top.toptimus.entity.event.reltransformation.yewubianma1yibu;
//import top.toptimus.entity.tokendata.event.TokenEventEntity;
//import top.toptimus.transformation.TransformationDTO;
//
//@Aspect
//public class TransformationAop {
//
//    @Autowired
//    private TokenEventEntity tokenEventEntity;
//
//    @Autowired
//    private yewubianma1yibu yewubianma1yibu;
//
//    /**
//     * 保存meta 分成4个结构
//     *
//     * @param retValue SaveMetaModel
//     */
//    @AfterReturning(returning = "retValue"
//            , pointcut = "execution(" +
//            "public * top.toptimus.amqp.consumer.TransformationConsumer.pushDownTransformation(..))"
//    )
//    public void transformationAfterReturning(Object retValue) {
//        TransformationDTO transformationDTO = (TransformationDTO) retValue;
//
//        // 异步进行其他单据转换
//        switch (transformationDTO.getRuleType()) {
//            case "业务编码1":
//                yewubianma1yibu.transformation(transformationDTO);
//                break;
//        }
//
//        // 存入数据库
//        transformationDTO.getTransformationDatas().keySet().forEach(metaId -> {
//            transformationDTO.getTransformationDatas().get(metaId).forEach(tokenDataDto -> {
//                tokenEventEntity.saveDatas(tokenDataDto, metaId);
//            });
//        });
//
//    }
//}
