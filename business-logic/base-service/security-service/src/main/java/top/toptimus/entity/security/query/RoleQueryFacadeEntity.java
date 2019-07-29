package top.toptimus.entity.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.secutiry.RoleRepository;
import top.toptimus.user.RoleDTO;

import java.util.List;

/**
 * Created by JiangHao on 2018/12/18.
 */
@Component
public class RoleQueryFacadeEntity {

    @Autowired
    private RoleRepository roleRepository;


    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return 角色
     */
    public List<RoleDTO> findRolesByUserId(String userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    /**
     * 查询所有可用角色
     *
     * @return 角色
     */
    public List<RoleDTO> findAllRole(){
        return roleRepository.findAllRole();
    }
}
