package top.toptimus.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * 授权服务器配置器适配器
 * Created by JiangHao on 2018/12/13.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // 资源的id
    private static final String RESOURCE_ID = "toptimus";
    // 客户端id
    private static final String CLIENT_ID = "toptimus_client";
    // 支持的授权类型  密码模式
    private static final String SUPPORT_AUTHORIZATION_TYPE_PASSWORD = "password";
    // REFRESH_TOKEN  刷新token
    private static final String REFRESH_TOKEN = "refresh_token";
    // 作用域
    private static final String SCOPES = "select";
    // 客户端安全码
    private static final String SECRET = "123456";
    // 此客户端可以使用的权限（基于Spring Security authorities）
    private static final String AUTHORITIES = "oauth2";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource(name = "customUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private TokenEnhancerImpl tokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置一个客户端,一个用于password认证
        clients.inMemory()
                .withClient(CLIENT_ID)
                .resourceIds(RESOURCE_ID)
                .authorizedGrantTypes(SUPPORT_AUTHORIZATION_TYPE_PASSWORD, REFRESH_TOKEN)
                .scopes(SCOPES)
                .authorities(AUTHORITIES)
                .secret(new BCryptPasswordEncoder().encode(SECRET));
    }

    // 授权服务器端点配置器
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenServices(tokenServices())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)//支持GET  POST  请求获取token
                .userDetailsService(userDetailsService) //必须注入userDetailsService否则根据refresh_token无法加载用户信息
                .reuseRefreshTokens(true);  //开启刷新token
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        //允许表单认证
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()") //isAuthenticated():排除anonymous   isFullyAuthenticated():排除anonymous以及remember-me
                .allowFormAuthenticationForClients();  //允许表单认证
    }

    @Primary
    @Bean
    protected AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 设置access_token的有效时间  默认为12个小时   -1为永不失效
        defaultTokenServices.setAccessTokenValiditySeconds(36000);
        // 设置refresh_token的有效时间 -1为永不失效
        defaultTokenServices.setRefreshTokenValiditySeconds(-1);
        // 是否支持刷新令牌 刷新access_token
        defaultTokenServices.setSupportRefreshToken(true);
        // 是否支持刷新token的令牌 可以刷新
        defaultTokenServices.setReuseRefreshToken(false);
        // 设置token的储存方式  目前为redis存储
        defaultTokenServices.setTokenStore(tokenStore());
        // 自定义Token的增强器  可以往返回值中增加自定义的属性
        defaultTokenServices.setTokenEnhancer(tokenEnhancer);
        return defaultTokenServices;
    }

    /**
     * 使用自定义的redis存储access_token
     */
    @Bean
    protected TokenStore tokenStore() {
        return new CustomRedisTokenStore(redisConnectionFactory);
    }
}

