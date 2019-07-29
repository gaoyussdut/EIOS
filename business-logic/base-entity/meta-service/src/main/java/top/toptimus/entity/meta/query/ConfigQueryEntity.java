package top.toptimus.entity.meta.query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.search.SearchItem;
import top.toptimus.dao.meta.MetaAuthRelationDao;
import top.toptimus.dao.meta.MetaSearchConfigDao;
import top.toptimus.meta.MetaRelation.MasterBillMetaRelationDTO;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.repository.meta.MetaRelation.MasterBillMetaRelationRepository;
import top.toptimus.repository.meta.MetaRelation.MetaAuthRelationRepository;
import top.toptimus.repository.meta.metaSearch.MetaSearchConfigRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConfigQueryEntity {

    @Autowired
    private MetaSearchConfigRepository metaSearchConfigRepository;
    @Autowired
    private MetaAuthRelationRepository metaAuthRelationRepository;
    @Autowired
    private MasterBillMetaRelationRepository masterBillMetaRelationRepository;

    /**
     * 根据metaId和meta中的属性查询，备查帐检索信息
     *
     * @param metaId     metaId
     * @param selectType meta中的属性（tab页）
     * @return SearchItem 备查帐检索信息
     */
    public SearchItem findMetaSearchConfigById(String metaId, String selectType) {
        Optional<MetaSearchConfigDao> metaSearchConfigDao = metaSearchConfigRepository.findById(metaId);
        if (metaSearchConfigDao.isPresent()) {
            for (SearchItem searchItem : metaSearchConfigDao.get().getMetaSearchDaos()) {
                if (searchItem.getIndex() == 1 && StringUtils.isEmpty(selectType)) {
                    return searchItem;
                } else if (searchItem.getSelectType().equals(selectType)) {
                    return searchItem;
                }
            }
        }
        return null;
    }

    /**
     * 根据metaId中的属性查询，备查帐检索信息
     *
     * @param metaId metaId
     * @return List<SearchItem>
     */
    public List<SearchItem> findMetaSearchConfigByMetaId(String metaId) {
        Optional<MetaSearchConfigDao> metaSearchConfigDao = metaSearchConfigRepository.findById(metaId);
        if (metaSearchConfigDao.isPresent())
            return metaSearchConfigDao.get().getMetaSearchDaos();
        return new ArrayList<>();
    }

    /**
     * 根据权限查询表头meta所能查看的分录、备查账meta
     *
     * @param metaId metaId
     * @param roleId 角色id
     * @return MetaAuthRelationDTO
     */
    public MetaAuthRelationDTO findMetaAuthRelationByMetaIdAndRoleId(String metaId, String roleId) {
        MetaAuthRelationDao metaAuthRelationDao = metaAuthRelationRepository.findByMetaIdAndRoleId(metaId, roleId);
        if (null != metaAuthRelationDao) {
            return metaAuthRelationDao.build();
        } else {
            return null;
        }

    }

    /**
     * 根据主数据meta查询已配置的单据的权限
     *
     * @param metaId 单据数据metaid
     * @return List<MetaAuthRelationDTO>
     */
    public List<MetaAuthRelationDTO> findMetaAuthRelationDaosByMetaId(String metaId) {
        List<MetaAuthRelationDao> metaAuthRelationDaos = metaAuthRelationRepository.findMetaAuthRelationDaosByMetaId(metaId);
        if (null != metaAuthRelationDaos && metaAuthRelationDaos.size() > 0) {
            return new ArrayList<MetaAuthRelationDTO>() {
                private static final long serialVersionUID = -3270723164480336349L;

                {
                    metaAuthRelationDaos.forEach(metaAuthRelationDao -> add(metaAuthRelationDao.build()));
                }
            };
        } else {
            return null;
        }

    }

    /**
     * 保存role和单据视图meta的关系
     *
     * @param metaAuthRelationDTO role和单据视图meta的关系
     */
    public void saveMetaAuthRelation(MetaAuthRelationDTO metaAuthRelationDTO) {
        metaAuthRelationRepository.save(new MetaAuthRelationDao(
                        metaAuthRelationDTO.getId()
                        , metaAuthRelationDTO.getMetaId()
                        , metaAuthRelationDTO.getRoleId()
                        , metaAuthRelationDTO.getRelMetaId()
                )
        );

    }

    /**
     * 根据权限查询表头meta所能查看的分录、备查账meta
     *
     * @param metaIds metaIds
     * @param roleId  角色id
     * @return MetaAuthRelationDTO
     */
    public List<MetaAuthRelationDTO> findMetaAuthRelationByMetaIdAndRoleId(List<String> metaIds, String roleId) {
        metaAuthRelationRepository.findMetaAuthRelationDaosByMetaIdInAndRoleId(metaIds, roleId);
        return new ArrayList<MetaAuthRelationDTO>() {
            private static final long serialVersionUID = -3270723164480336349L;

            {
                metaAuthRelationRepository.findMetaAuthRelationDaosByMetaIdInAndRoleId(metaIds, roleId)
                        .forEach(metaAuthRelationDao -> add(metaAuthRelationDao.build()));
            }
        };
    }

    /**
     * 获取所有表单的配置关联信息
     *
     * @return List<MasterBillMetaRelationDTO>
     */
    public List<MasterBillMetaRelationDTO> findAllBillRelationInfo() {
        return new ArrayList<MasterBillMetaRelationDTO>() {
            private static final long serialVersionUID = -8888983107130104092L;

            {
                masterBillMetaRelationRepository.findAll()
                        .forEach(masterBillMetaRelationDao -> add(masterBillMetaRelationDao.build()));
            }
        };
    }

    /**
     * 根据主数据的表头metaId获取单据的关联信息
     *
     * @param billMasterMetaId 表头的主数据metaId
     * @return MasterBillMetaRelationDTO
     */
    public MasterBillMetaRelationDTO findBillRelationInfoById(String billMasterMetaId) {
        return masterBillMetaRelationRepository.findById(billMasterMetaId).get().build();
    }

}
