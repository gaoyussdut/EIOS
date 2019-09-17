package top.toptimus.amqp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.toptimus.amqp.producer.TransitionEntity;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.place.place_deprecated.BillTokenSaveResultDTO;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.repository.token.dynamicTokenQuery.DynamicTokenQueryRepository;

@Component
public class SavePlaceConsumer {
    @Autowired
    private PlaceRedisEntity placeRedisEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;
    @Autowired
    private TransitionEntity transitionEntity;

    private static final Logger logger = LoggerFactory.getLogger(DynamicTokenQueryRepository.class);

    /**
     * 存分录数据
     *
     * @param billTokenSaveResultDTO
     */
    @JmsListener(destination = "saveEntryTokenData.save")
    public void saveEntryToken(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        try {
            //1.保存tokendata
                    tokenEventEntity.saveDatas(
                            billTokenSaveResultDTO
                                    .getBillTokenResultBody()
                                    .getEntryTokenData()
                            , billTokenSaveResultDTO
                                    .getBillTokenResultBody()
                                    .getEntryMetaId()
                    );
            //2.同步关系到pg
            tokenEventEntity.saveRel(billTokenSaveResultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("失败！");
        }
    }

    /**
     * 保存单据表头数据 AUTH TODO
     *
     * @param billTokenSaveResultDTO
     */
    @JmsListener(destination = "saveBillTokenData.save")
    public void saveBillToken(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        try {
            //保存表头数据到PG
            tokenEventEntity.saveDatas(
                    billTokenSaveResultDTO
                            .getBillTokenResultBody()
                            .getBillTokenData()
                    ,billTokenSaveResultDTO
                            .getBillTokenResultBody()
                            .getBillMetaId()
            );

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("失败！");
        }
    }

    /**
     * 获取placeDTO数据 从缓存中获取PlaceDTO数据
     *
     * @param billTokenSaveResultDTO 库所保存结果
     * @return placeDTO
     */
    private PlaceDTO getPlaceDTO(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        PlaceDTO placeDTO;
        // 先去缓存中查找place
        placeDTO = placeRedisEntity.findPlace(
                billTokenSaveResultDTO.getBillTokenResultBody().getBillTokenId()
                , billTokenSaveResultDTO.getBillTokenResultBody().getAuthId()
        ).getPlaceDTO();
        // 再没找到就报错
        if (null == placeDTO) {
            throw new TopException(TopErrorCode.PLACE_DATA_NOT_EXIST); //未获取到placeDTO!
        }
        return placeDTO;
    }


}

