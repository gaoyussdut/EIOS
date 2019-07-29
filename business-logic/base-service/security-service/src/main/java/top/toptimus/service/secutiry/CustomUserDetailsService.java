package top.toptimus.service.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import top.toptimus.constantConfig.AuthConstants;
import top.toptimus.entity.security.query.RoleQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.user.RoleDTO;
import top.toptimus.user.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Component(value = "customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private RoleQueryFacadeEntity roleQueryFacadeEntity;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userQueryFacadeEntity.findByAccount(username);
        if (null == user) {
            throw new TopException(TopErrorCode.USER_DOES_NOT_EXIST);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getAccount()
                , new BCryptPasswordEncoder().encode(user.getPasswordDigest()) // TODO 数据库中就应该是encode之后的了  取出来就可以用  目前测试 先手动encode
                , user.getEnabled()   // 是否用户被禁用
                , true  // 是否账户不是过期的
                , true  // 是否凭证不是过期的
                , true  // 是否非锁定账户
                , this.getAuthority(user.getId())
        );  // 获取用户对应的角色信息
    }

    /**
     * 获取用户角色  用于security判断角色是否可以访问url
     *
     * @param userId 用户id
     * @return 用户角色
     */
    private List<SimpleGrantedAuthority> getAuthority(String userId) {
        List<RoleDTO> roleList = roleQueryFacadeEntity.findRolesByUserId(userId);
        return new ArrayList<SimpleGrantedAuthority>() {

            private static final long serialVersionUID = 1564919183738560020L;

            {
                for (RoleDTO role : roleList) {
                    // 判断角色是否是ROLE_为前缀 不是的话 就加上前缀
                    if (role.getName().startsWith(AuthConstants.ROLE_)) {
                        add(
                                new SimpleGrantedAuthority(role.getName())
                        );
                    } else {
                        add(
                                // spring securit必须有这个ROLE_的前缀 所以从数据库中取出来角色后加个前缀
                                new SimpleGrantedAuthority(AuthConstants.ROLE_ + role.getName())
                        );
                    }
                }
            }
        };
    }
}