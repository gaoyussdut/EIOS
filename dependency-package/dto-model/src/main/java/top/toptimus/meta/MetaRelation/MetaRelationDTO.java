package top.toptimus.meta.MetaRelation;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * meta关系dto
 * Created by lzs on 2019/1/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaRelationDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -79126780912672794L;

    private String masterMetaId;  //单据主数据meta
    private String masterMetaName; //单据主数据metaName
    private List<ViewMetaDTO> viewMetaDTOList;   //单据内视图meta
    private String masterMemorandvnMetaId;  //单据内备查帐主数据meta
    private List<ViewMemorandvnDTO> viewMemorandvnDTOList; //单据内视图备查
    private String storedProcedure; //单据提交存储过程
}
