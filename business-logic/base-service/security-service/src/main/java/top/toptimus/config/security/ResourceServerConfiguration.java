package top.toptimus.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置器适配器
 *
 * @author JiangHao
 * @since 2018/12/13
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "toptimus";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(true);
        // 定义资源类异常转换类生效
//        resources.authenticationEntryPoint(new AuthExceptionEntryPointImpl());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // Since we want the protected resources to be accessible in the UI as well we need
                // session creation to be allowed (it's disabled by default in 2.0.6)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .requestMatchers().anyRequest()
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                /*
                 * .antMatchers("/task/**").authenticated()               token为有效的就可以访问
                 * .antMatchers("/task/**").hasAnyRole("ADMIN","USRR")    token为有效而且包含任意一种角色
                 * .antMatchers("/task/**").hasRole("ADMIN")              token为有效且拥有该角色
                 * .antMatchers("/task/**").anonymous()                   匿名可访问
                 * .antMatchers("/task/**").permitAll()                   无需认证 谁都可以访问
                 *  ...
                 */
//                .antMatchers("/task/**").hasAnyRole("ADMIN")
                .antMatchers("/placeHistorical/**").hasAnyRole("ADMIN");
//                .antMatchers("/memorandvn/**").authenticated();
    }


}
