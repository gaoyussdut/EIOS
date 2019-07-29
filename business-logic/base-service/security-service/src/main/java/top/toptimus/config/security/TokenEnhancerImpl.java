package top.toptimus.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import top.toptimus.entity.security.query.RoleQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.user.RoleDTO;
import top.toptimus.user.UserDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义token的增强器
 * 用于认证成功后 除access_token等 信息外添加自定义的信息 如user_id 、角色信息等等
 * 但不建议添加其他信息 因为获取access_token 属于认证服务
 * 像用户信息等数据 应该划分为资源类服务 应用access_token去获取资源类的信息
 * Created by JiangHao on 2018/12/18.
 */
@Component
public class TokenEnhancerImpl implements TokenEnhancer {

    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private RoleQueryFacadeEntity roleQueryFacadeEntity;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserDTO user = userQueryFacadeEntity.findByAccount(authentication.getName());
        List<RoleDTO> roleList = roleQueryFacadeEntity.findRolesByUserId(user.getId());
        // 想返回结果中添加什么就往map里添加 K V 就可以
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("user_id", user.getId());
        additionalInfo.put("roles", roleList);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
