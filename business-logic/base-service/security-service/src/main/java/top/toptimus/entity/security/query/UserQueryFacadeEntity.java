package top.toptimus.entity.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.toptimus.repository.secutiry.UserRepository;
import top.toptimus.user.UserDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JiangHao on 2018/12/18.
 */
@Component
public class UserQueryFacadeEntity {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据账户查找用户信息
     *
     * @param account 账户
     * @return 用户信息
     */
    public UserDTO findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    /**
     * 根据用户的access_token 获取用户信息
     *
     * @return 用户信息
     */
    public UserDTO findByAccessToken() {
//        // 1.获取User信息
//        UserDTO userDTO = userRepository.findByAccount(
//                SecurityContextHolder.getContext().getAuthentication().getName()
//        );
//        // 2.获取下属UserId
//        List<UserDTO> userDTOS = userRepository.findSubordinateByUserId(userDTO.getId());
//        userDTO.setChildUserIds(new ArrayList<String>(){{
//            for (UserDTO userDTOtmp : userDTOS) {
//                if (!userDTOtmp.getId().equals(userDTO.getId()))
//                    add(userDTOtmp.getId());
//            }
//        }});
//        return userDTO;
        //TODO
        UserDTO userDTO = new UserDTO();
        userDTO.setId("3");
        userDTO.setRoleId("4");
        return userDTO;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserDTO findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * 根据用户id查询其和下属用户信息(PostgreSQL递归)
     *
     * @param userId 用户id
     * @return 下属用户信息
     */
    public List<UserDTO> findSubordinateByUserId(String userId) {
        return userRepository.findSubordinateByUserId(userId);
    }

    /**
     * 根据用户id 查询用户的基础信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserDTO findUserBaseInfoById(String userId) {
        return userRepository.findUserBaseInfoById(userId);
    }


}
