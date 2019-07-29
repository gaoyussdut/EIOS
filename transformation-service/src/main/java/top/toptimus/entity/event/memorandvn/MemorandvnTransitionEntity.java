package top.toptimus.entity.event.memorandvn;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.dao.meta.MetaRelationDao;
import top.toptimus.repository.meta.MetaRelation.MetaRelationRepository;
import top.toptimus.repository.token.MetaRelation.MetaTokenRelationRepository;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;
import top.toptimus.transformation.TransformationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 备查帐转换处理实体类
 *
 * @author gaoyu
 * @since 2018-6-20
 */
@Component
public class MemorandvnTransitionEntity {

    @Autowired
    private MetaTokenRelationRepository metaTokenRelationRepository;
    @Autowired
    private MetaRelationRepository metaRelationRepository;

    // 计算字段
    private double shuliang;  //数量
    private double danjia;    //单价
    private double zongjine;  //总金额
    private double zherang = 0;   //折让
    private double zhekou = 1;    //折扣

    // 直接赋值字段
    private String fuzeren;  //负责人


    /**
     * 单据转换
     *
     * @param transformationDTO 当前单据数据
     */
    public void transformation(TransformationDTO transformationDTO) {

        // 当前单据抽值
//        transformationDTO.getCurrentDatas().get(transformationDTO.getBillMetaId()).forEach(tokenDataDto -> {
//            tokenDataDto.getFields().forEach(fkeyField -> {
//                switch (fkeyField.getKey()) {
//                    case "shuliang":
//                        if (!StringUtils.isEmpty(fkeyField.getJsonData()))
//                            this.shuliang = Double.parseDouble(fkeyField.getJsonData());
//                        break;
//                    case "danjia":
//                        if (!StringUtils.isEmpty(fkeyField.getJsonData()))
//                            this.danjia = Double.parseDouble(fkeyField.getJsonData());
//                        break;
//                    case "fuzeren":
//                        if (!StringUtils.isEmpty(fkeyField.getJsonData()))
//                            this.fuzeren = fkeyField.getJsonData();
//                        break;
//                    case "zherang":
//                        if (!StringUtils.isEmpty(fkeyField.getJsonData()))
//                            this.zherang = Double.parseDouble(fkeyField.getJsonData());
//                        break;
//                    case "zhekou":
//                        if (!StringUtils.isEmpty(fkeyField.getJsonData()))
//                            this.zhekou = Double.parseDouble(fkeyField.getJsonData());
//                        break;
//                }
//            });
//        });
        // 计算规则
        this.calculate();

//        MetaRelationDao metaRelationDao = metaRelationRepository.findById(transformationDTO.getBillMetaId()).get();

        List<FkeyField> fkeyFieldList = new ArrayList<FkeyField>() {{
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "shuliang", String.valueOf(shuliang)));
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "danjia", String.valueOf(danjia)));
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "zongjine", String.valueOf(zongjine)));
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "zherang", String.valueOf(zherang)));
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "zhekou", String.valueOf(zhekou)));
            add(new FkeyField().createPlainFkeyField(FkeyTypeEnum.STRING, "fuzeren", fuzeren));

        }};

//        TokenDataDto tokenDataDto = new TokenDataDto(transformationDTO.getBillTokenId(), fkeyFieldList);

        // 上级单据赋值
//        transformationDTO.getTransformationDatas().put(metaRelationDao.getMasterMemorandvnMetaId(), Lists.newArrayList(tokenDataDto));
    }

    private void calculate() {
        // 总金额计算
        this.zongjine = this.shuliang * this.danjia * this.zherang - this.zhekou;
    }
}
