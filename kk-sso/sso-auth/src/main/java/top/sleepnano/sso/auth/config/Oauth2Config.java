package top.sleepnano.sso.auth.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.security.KeyPair;
import java.util.UUID;

/**
 * Oauth2 是一种认证授权规范，它基于认证和授权定义了一套规则，在这套规则中规定了
 * 实现一套认证授权系统需要哪些对象：
 * 1)系统资源(数据)
 * 2)资源拥有者(用户)
 * 3)管理资源的服务器
 * 4)对用户进行认证和授权的服务器
 * 5)客户端系统(负责提交用户身份信息的系统)
 *
 * 思考：对于一个认证授权系统来讲，需要什么？：
 * 1)提供一个认证的入口？(客户端去哪里认证)
 * 2)客户端应该携带什么信息去认证？(username,password,....)
 * 3)服务端通过谁去对客户端进行认证(一个负责认证的对象)？
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer //在oauth2规范中启动认证和授权
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {

//    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    //@Autowired
    private AuthenticationManager authenticationManager;
    //@Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //@Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    //@Autowired
    private TokenStore tokenStore;

//    @Autowired
//    private RedisTokenStore redisTokenStore;
    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    //提供一个认证的入口(客户端去哪里认证)？(http://ip:port/.....)
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //super.configure(security);
        //对外发布认证入口(/oauth/token),认证通过服务端会生成一个令牌
        security.tokenKeyAccess("permitAll()")
                //对外发布检查令牌的入口(/oauth/check_token)
                .checkTokenAccess("permitAll()")
                //允许用户通过表单方式提交认证,完成认证
                .allowFormAuthenticationForClients();
    }
    //定义客户端应该携带什么信息去认证？
    //指明哪些对象可以到这里进行认证(哪个客户端对象需要什么特点)。
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //super.configure(clients);

        clients.inMemory()
                //客户端标识
                .withClient("gateway-client")
                //客户端密钥(随意)
                .secret(passwordEncoder.encode("123456"))
                //指定认证类型(码密,刷新令牌，三方令牌，...)
                .authorizedGrantTypes("password","refresh_token")
                //作用域(在这里可以理解为只要包含我们规定信息的客户端都可以进行认证)
                .scopes("all");
    }
    //提供一个负责认证授权的对象？(完成客户端认证后会颁发令牌，默认令牌格式是uuid方式的)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //super.configure(endpoints);
        //设置认证授权对象
        endpoints.authenticationManager(authenticationManager)
                //设置令牌业务对象(此对象提供令牌创建及有效机制设置)
                .tokenServices(tokenService())//不写，默认是uuid
                //设置允许对哪些请求方式进行认证(默认支支持post):可选
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
    }

    @Bean
    public AuthorizationServerTokenServices tokenService(){
        //1.构建token业务对象
        DefaultTokenServices ts=new DefaultTokenServices();
        //2.设置令牌生成机制(创建令牌的方式，存储用户状态信息的方式)
        ts.setTokenStore(redisTokenStore());
        //3.设置令牌增强(改变默认令牌创建方式，没有这句话默认是UUID)
        ts.setTokenEnhancer(jwtAccessTokenConverter);
        //4.设置令牌有效时长(可选)
        ts.setAccessTokenValiditySeconds(3600);
        //5.设置刷新令牌以及它的有效时时长(可选)
        ts.setSupportRefreshToken(true);
        ts.setRefreshTokenValiditySeconds(3600*24);
        return ts;
    }

}
