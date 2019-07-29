//package top.toptimus.entity.aop;
//
//import com.alibaba.fastjson.JSON;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import top.toptimus.amqp.producer.SaveTokenProducer;
//import top.toptimus.place.PlaceAlterDto;
//
//@Aspect
//@Component
//public class SaveTokenAop {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private SaveTokenProducer saveTokenProducer;
//
//    /**
//     * Token数据持久化到数据库中
//     *
//     * @param retValue PlaceAlterDto
//     */
//    @AfterReturning(returning = "retValue"
//            , pointcut = "execution(" +
//            "public * top.toptimus.entity.place.PlaceRedisEntity.saveTokens(..))"
//    )
//    public void saveTokenDataAfterReturning(Object retValue) {
//        PlaceAlterDto placeAlterDto = (PlaceAlterDto) retValue;
//        logger.info("Token数据持久化到数据库中:" + JSON.toJSONString(placeAlterDto));
//        saveTokenProducer.saveToken(placeAlterDto);
//    }
//
//}
