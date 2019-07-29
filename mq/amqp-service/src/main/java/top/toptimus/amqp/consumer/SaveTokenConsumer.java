package top.toptimus.amqp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.place.query.PlaceCacheQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.model.tokenData.event.BillTokenSaveModel;
import top.toptimus.model.tokenData.event.TokenDeleteModel;
import top.toptimus.place.PlaceAlterDto;
import top.toptimus.repository.token.dynamicTokenQuery.DynamicTokenQueryRepository;

@Component
public class SaveTokenConsumer {

    @Autowired
    private PlaceCacheQueryFacadeEntity placeCacheQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;


    private static final Logger logger = LoggerFactory.getLogger(DynamicTokenQueryRepository.class);

    /**
     * 库所增量提交持久化到数据库中
     *
     * @param placeAlterDto 库所增量提交dto
     */
    @JmsListener(destination = "saveToken.save")
    public void saveTokenData(PlaceAlterDto placeAlterDto) {
        try {
            // 1.更新操作
            // 表头数据保存
            tokenEventEntity.saveTokenDatas(
                    new BillTokenSaveModel(
                            placeAlterDto.getBillTokenId()
                            , placeCacheQueryFacadeEntity.findAllTokenDataDtoByIds(placeAlterDto.getAlters())
                            , metaQueryFacadeEntity.getMetaInfo(placeAlterDto.getMetaId())
                    ));
            // 2.删除操作
            tokenQueryFacadeEntity.deleteTokenDatas(new TokenDeleteModel(placeAlterDto.getMetaId(), placeAlterDto.getRemoves()));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("库所增量提交持久化到数据库失败！");
        }
    }


}

