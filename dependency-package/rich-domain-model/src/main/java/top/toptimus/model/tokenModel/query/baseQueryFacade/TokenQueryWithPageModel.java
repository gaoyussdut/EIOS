package top.toptimus.model.tokenModel.query.baseQueryFacade;

import lombok.Getter;
import org.springframework.util.StringUtils;
import top.toptimus.baseModel.query.BasePageQueryModel;
import top.toptimus.common.enums.SearchCommonEnum;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.common.search.SearchItem;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryPageAbleMainTable;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.user.UserDTO;

import java.util.List;


/**
 * token数据查询model,带分页
 *
 * @author gaoyu
 * @since 2018-11-26
 */
@Getter
public class TokenQueryWithPageModel extends BasePageQueryModel {
    private String metaId;
    private TokenMetaInfoDTO tokenMetaInfoDTO;


    /**
     * 构建token ids和主表
     *
     * @param metaId     meta id
     * @param pageNumber 页码
     * @param pageSize   页宽
     */
    public TokenQueryWithPageModel(
            String metaId
            , SearchInfo searchInfo
            , Integer pageNumber
            , Integer pageSize
    ) {
        if (pageNumber < 1) {
            throw new TopException(TopErrorCode.INVALID_PAGE_NUMBER);
        }
        this.metaId = metaId;
        super.searchInfo = searchInfo;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }


    /**
     * 根据缓存的MainTable准备查询的数据
     *
     * @param mainTable  缓存的mainTable
     * @param pageNumber 当前页
     * @param pageSize   当前页大小
     */
    public TokenQueryWithPageModel(MainTable mainTable, Integer pageNumber, Integer pageSize, SearchInfo searchInfo) {
        this.mainTable = new ViewQueryPageAbleMainTable(mainTable);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.searchInfo = searchInfo;
    }

    /**
     * 构建meta信息和构建主表信息
     *
     * @param tokenMetaInfoDTO meta info
     * @param tableName        表名
     * @return this
     */
    public TokenQueryWithPageModel build(TokenMetaInfoDTO tokenMetaInfoDTO, String tableName) {
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
        this.mainTable = new ViewQueryPageAbleMainTable(tokenMetaInfoDTO, tableName);
        return this;
    }

    /**
     * 构建数据总条数，检查分页
     *
     * @param count 数据总条数
     * @return this
     */
    public TokenQueryWithPageModel build(int count) {
        this.count = count;
        // 判断所查数据下标收否超过数据条数
        this.checkPage = (pageNumber - 1) * pageSize < count || (pageNumber - 1) * pageSize == 0;
        return this;
    }

    /**
     * 构建检索条件的参数
     *
     * @param searchInfo
     * @param userDTO
     * @return
     */
    public TokenQueryWithPageModel build(SearchInfo searchInfo, UserDTO userDTO) {
        if (null != searchInfo && null != searchInfo.getSearchItem())
            for (SearchCommonEnum searchCommonEnum : searchInfo.getSearchItem().getSearchCommonMap().keySet()) {
                switch (searchCommonEnum) {
                    case USERID:
                        String userId = searchInfo != null && searchInfo.getParam() != null && !StringUtils.isEmpty(searchInfo.getParam().get(searchCommonEnum)) ? (String) searchInfo.getParam().get(searchCommonEnum) : userDTO.getId();
                        super.param.add(userId);
                        break;
                    case ORGID:
                        String orgId = searchInfo != null && searchInfo.getParam() != null && !StringUtils.isEmpty(searchInfo.getParam().get(searchCommonEnum)) ? (String) searchInfo.getParam().get(searchCommonEnum) : userDTO.getOrganizationId();
                        super.param.add(orgId);
                        break;
                    case ROLEID:
                        // 传入UserId，sql中根据UserId查询权限
                        String roleUserId = searchInfo != null && searchInfo.getParam() != null && !StringUtils.isEmpty(searchInfo.getParam().get(searchCommonEnum)) ? (String) searchInfo.getParam().get(searchCommonEnum) : userDTO.getId();
                        super.param.add(roleUserId);
                        break;
                    case CHILD_USERIDS:
                        //List<String> userIds = searchInfo!=null && !StringUtils.isEmpty(searchInfo.getParam().get(searchCommonEnum)) ? (List<String>) searchInfo.getParam().get(searchCommonEnum) : userDTO.getChildUserIds();
                        // 取得父UserId在 sql中取得其下属子UserId
                        String parentUserId = searchInfo != null && searchInfo.getParam() != null && !StringUtils.isEmpty(searchInfo.getParam().get(searchCommonEnum)) ? (String) searchInfo.getParam().get(searchCommonEnum) : userDTO.getId();
                        super.param.add(parentUserId);
                    default:
                }
            }
        return this;
    }

}
