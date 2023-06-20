package top.sleepnano.sso.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * 当我们在执行登录操作时,底层逻辑(了解):
 * 1)Filter(过滤器)
 * 2)AuthenticationManager (认证管理器)
 * 3)AuthenticationProvider(认证服务处理器)
 * 4)UserDetailsService(负责用户信息的获取及封装)
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 初始化加密对象
     * 此对象提供了一种不可逆的加密方式,相对于md5方式会更加安全
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 定义认证管理器对象，这个对象负责完成用户信息的认证，
     * 即判定用户身份信息的合法性，在基于oauth2协议完成认
     * 证时，需要此对象，所以这里讲此对象拿出来交给spring管理
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

}
