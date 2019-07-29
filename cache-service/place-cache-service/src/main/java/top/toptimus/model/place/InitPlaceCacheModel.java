//package top.toptimus.model.place;
//
//import lombok.Getter;
//import top.toptimus.dao.place.RelRedisDao;
//import top.toptimus.merkle.MerkleWorkPackageModel;
//import top.toptimus.place.PlaceDTO;
//import top.toptimus.relation.MetaTokenRelationDTO;
//import top.toptimus.tokendata.TokenDataDto;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 初始化表单缓存model
// *
// * @author gaoyu
// * @since 2019-01-22
// */
//@Getter
//public class InitPlaceCacheModel {
//    private List<RelRedisDao> relRedisDao = new ArrayList<>();    //  记录单据关系redis结构
//    private TokenDataDto tokenDataDto;  //  返回值
//    private boolean initToken = false;
//
//    /**
//     * 提供初始化表单的返回值，清洗数据，缓存数据
//     *
//     * @param placeDTO 当前placeDTO
//     */
//    public InitPlaceCacheModel(PlaceDTO placeDTO
//            , MerkleWorkPackageModel merkleWorkPackageModel
//    ) {
//        //  返回值定义
//        if (placeDTO.getDatas().get(placeDTO.getBillMetaid()).isEmpty())
//            throw new RuntimeException("metaId为空");
//        else
//            this.tokenDataDto = placeDTO.getDatas().get(placeDTO.getBillMetaid()).get(0);
//        //  数据清洗
//        if (null != placeDTO.getMetaTokenRelationDTOS() && !placeDTO.getMetaTokenRelationDTOS().isEmpty()) {
//            this.initToken = true;
//            List<MetaTokenRelationDTO> metaTokenRelationDTOS = new ArrayList<>();   //  新加入的关系
//            //  更新线程中的数据关系缓存
//            placeDTO.getMetaTokenRelationDTOS().forEach(
//                    metaTokenRelationDTO -> {
//                        {
//                            for (MetaTokenRelationDTO tokenRelationDTO : merkleWorkPackageModel.getMetaTokenRelationDTOS()) {
//                                if (!tokenRelationDTO.getId().equals(metaTokenRelationDTO.getId())) {
//                                    //  新数据加入
//                                    metaTokenRelationDTOS.add(metaTokenRelationDTO);    //  计入本地
//                                    this.relRedisDao.add(new RelRedisDao(metaTokenRelationDTO));    //  加入缓存
//                                }
//                            }
//                        }
//                    }
//            );
//            merkleWorkPackageModel.getMetaTokenRelationDTOS().addAll(metaTokenRelationDTOS);
//        }
//    }
//}
