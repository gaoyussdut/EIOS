//package top.toptimus.entity.aop;
//
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import top.toptimus.amqp.producer.SavePlaceProducer;
//import top.toptimus.common.result.Result;
//import top.toptimus.exception.TopErrorCode;
//import top.toptimus.place.BillTokenSaveResultDTO;
//
//@Aspect
//@Component
//public class SavePlaceAop {
//    @Autowired
//    private SavePlaceProducer savePlaceProducer;
//
//    @AfterReturning(returning = "retValue"
//            , pointcut = "execution(" +
//            "public * top.toptimus.entity.place.PlaceRedisEntity.submitBillToken(..))"
//    )
//    public void savePlaceDTOAfterReturning(Object retValue) {
//        Result result = (Result) retValue;
//        // 成功的场合
//        if (result.getResultCode() != TopErrorCode.SUCCESS.getErrcode()) {
//            return;
//        }
//        BillTokenSaveResultDTO billTokenSaveResultDTO = (BillTokenSaveResultDTO)result.getData();
//        if (billTokenSaveResultDTO.getResultCode() == 0) {
//            savePlaceProducer.savePlace(billTokenSaveResultDTO);
//        }
//    }
//
//}
