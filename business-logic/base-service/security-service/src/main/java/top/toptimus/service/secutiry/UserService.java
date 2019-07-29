package top.toptimus.service.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.result.Result;
import top.toptimus.entity.security.query.RoleQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.user.RoleDTO;
import top.toptimus.user.UserDTO;

import java.util.List;

/**
 * Created by JiangHao on 2018/12/18.
 */
@Service
public class UserService {

    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private RoleQueryFacadeEntity roleQueryFacadeEntity;

    /**
     * 根据账户查找用户信息
     *
     * @param account 账户
     * @return 用户信息
     */
    public UserDTO getUserInfoByAccount(String account) {
        return userQueryFacadeEntity.findByAccount(account);
    }


    /**
     * 根据用户的access_token 获取用户信息
     *
     * @return 用户信息
     */
    public Result getUserInfoByAccessToken() {
        try {
            return Result.success(
                    userQueryFacadeEntity.findByAccessToken()
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserDTO getUserInfoByUserId(String userId) {
        return userQueryFacadeEntity.findByUserId(userId);
    }

    /**
     * 根据用户id查询其和下属用户信息(PostgreSQL递归)
     *
     * @param userId 用户id
     * @return 下属用户信息
     */
    public List<UserDTO> findSubordinateByUserId(String userId) {
        return userQueryFacadeEntity.findSubordinateByUserId(userId);
    }

    /**
     * 根据用户id查询角色
     *
     * @return 角色
     */
    public List<RoleDTO> getUserRolesInfoByAccessToken() {
        return roleQueryFacadeEntity.findRolesByUserId(
                userQueryFacadeEntity.findByAccessToken().getId()
        );
    }

    /**
     * 查询所有可用角色
     *
     * @return 角色
     */
    public Result findAllRole() {
        try {
            return Result.success(roleQueryFacadeEntity.findAllRole());
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据用户id 查询用户的基础信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public Result getUserBaseInfoById(String userId) {
        try {
            return Result.success(userQueryFacadeEntity.findUserBaseInfoById(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

}
