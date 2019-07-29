package top.toptimus.entity.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.secutiry.OrganizationRepository;
import top.toptimus.user.OrganizationDTO;

/**
 * Created by JiangHao on 2018/12/26.
 */
@Component
public class OrganizationQueryFacadeEntity {

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * 根据用户id查询组织信息
     *
     * @param userId 用户id
     */
    public OrganizationDTO findOrganizationInfoByUserId(String userId) {
        return organizationRepository.findByUserId(userId);
    }


}
