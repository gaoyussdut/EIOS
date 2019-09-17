package top.toptimus.transaction;


import com.google.common.collect.Lists;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TransactionTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PlaceRedisEntity placeRedisEntity;

//    @Test
//    public void testRedisDiff() {
//        String billTokenId = UUID.randomUUID().toString();
//        String metaId = "meta1";
//
//        placeRedisEntity.cacheSavePlaceTokens(
//                new PlaceDTO(billTokenId, new HashMap<>())
//                        .buildProcessData("userTaskId", "poTokenId", "processId")
//        ); //  保存空库所
//        logger.info("空库所token：" + JSON.toJSONString(
//                placeRedisEntity.findTokenDataDtoByMetaId(billTokenId, metaId)
//        )); //
//        PlaceDTO placeDTO = this.initData(billTokenId, metaId); //  初始化数据
//        placeRedisEntity.cacheSavePlaceTokens(placeDTO); //  保存redis数据
//        this.alterData(placeDTO);   //  数据变更
//
//        logger.info("变更前：" + JSON.toJSONString(
//                placeRedisEntity.findPlace(billTokenId)
//        )); //  测试查询库所
//
//
//        logger.info(
//                JSON.toJSONString(
//                        "diff:" +
//                                JSON.toJSONString(
//                                        placeRedisEntity.saveTokens(
//                                                new PlaceSaveTokenModel(
//                                                        billTokenId
//                                                        , metaId
//                                                        , DomainTypeEnum.ENTRY.name()
//                                                        , "meta2"
//                                                        , placeDTO.getDatas().get(metaId)
//                                                        , Lists.newArrayList("4", "44")
//                                                )
//                                        )//  测试diff
//                                )
//                )
//        );
//
//        logger.info("变更后取库所token：" + JSON.toJSONString(placeRedisEntity.findPlaceTokenDatas(billTokenId)));
//
//        placeRedisEntity.closePlaceThread(placeDTO);
//
//        logger.info("释放后：" + JSON.toJSONString(
//                placeRedisEntity.findPlace(billTokenId)
//        )); //  测试查询库所
//
////        logger.info(
////                JSON.toJSONString(
////                        "datas:" + JSON.toJSONString(transactionTransmittableThreadLocalModel.getMerkleModel().getTokenDatas())  //  sha-1加密数据
////                )
////        );
//    }

    private void alterData(PlaceDTO placeDTO) {
        //  构造变更的数据
        placeDTO.getDatas().get("meta1").addAll(
                new ArrayList<TokenDataDto>() {{
                    //  变更的数据
                    add(new TokenDataDto(
                            "33"
                            , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", "1"), new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", "22"))
                    ));
                    //  新增的数据
                    add(new TokenDataDto(
                            "xxxx"
                            , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", "1"), new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", "22"))
                    ));
                }}
        );
    }

    private PlaceDTO initData(String billTokenId, String metaId) {
        Map<String, List<TokenDataDto>> datas = new HashMap<String, List<TokenDataDto>>() {
            {
                put(metaId
                        , new ArrayList<TokenDataDto>() {
                            {
                                for (int i = 0; i < 50; i++) {
                                    add(
                                            new TokenDataDto(
                                                    String.valueOf(i)
                                                    , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", String.valueOf(i)))
                                            )
                                    );
                                }
                            }
                        });
                put("meta2"
                        , new ArrayList<TokenDataDto>() {
                            {
                                for (int i = 50; i < 100; i++) {
                                    add(
                                            new TokenDataDto(
                                                    String.valueOf(i)
                                                    , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, "卧槽", String.valueOf(i)))
                                            )
                                    );
                                }
                            }
                        });
            }
        };

        return new PlaceDTO(billTokenId, datas);

    }

}
