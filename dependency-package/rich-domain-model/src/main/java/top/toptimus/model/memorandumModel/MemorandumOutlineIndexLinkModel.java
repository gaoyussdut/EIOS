package top.toptimus.model.memorandumModel;

import top.toptimus.businessmodel.memorandvn.dto.MemorandvnDTO;
import top.toptimus.common.enums.UserControlEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;


/**
 * 备查账一览的索引link信息 (描绘页面)
 * Created by JiangHao on 2019/1/23.
 */
public class MemorandumOutlineIndexLinkModel extends BaseResultLinkInfoModel {

    private MemorandvnDTO memorandvnDTO; // 备查账信息

    public MemorandumOutlineIndexLinkModel(MemorandvnDTO memorandvnDTO) {
        this.memorandvnDTO = memorandvnDTO;
        buildAllResultLinks();
        super.resultContext = new ResultContext(
                super.resultLinks
        );
    }

    /**
     * 构建全部link的信息
     */
    private void buildAllResultLinks() {
        this.resultLinks = new ArrayList<>();
        this.resultLinks.add(
                new ResultUserControl(
                        this.memorandvnDTO.getMemorandvnName()
                        , OUTLINE_USER_CONTROL + UserControlEnum.MEMORANDUM_LIST_VIEW
                        , ServiceURLConstants.MEMORANDUM_OUTLINE_DETAIL_URL
                        + "/" + URL_ARGS_META_ID + "/" + this.memorandvnDTO.getMemorandvnMetaId()
                )
        );
    }


}
