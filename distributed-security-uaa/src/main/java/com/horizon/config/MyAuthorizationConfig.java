package com.horizon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

@Configuration
public class MyAuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 配置ClientDetailsService
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 内存配置的方式配置用户信息，注入ClientDetailsService
        clients.inMemory()//内存方式
                .withClient("c1") // client_id
                .secret(new BCryptPasswordEncoder().encode("secret"))// 客户端秘钥
                .resourceIds("order", "salary")// 客户端拥有的资源列表
                .authorizedGrantTypes("authorization_code",
                        "password", "client_credentials", "implicit", "refresh_token")// 该client允许的授权类型
                .scopes("all")// 允许的授权范围
                .autoApprove(false)// 跳转到授权页面
                .redirectUris("http://www.baidu.com");// 回调地址
//                .and() //继续注册其他客户端
//                .withClient()
//                ...
//   加载自定义的客户端管理服务:
//      Client Details客户端详情，能够在应用程序运行的时候进行更新，
//      可以通过访问底层的存储服务(例如访问mysql，就提供了JdbcClientDetailsService)
//      或者通过自己实现ClientRegistrationService接口,同时也可以实现ClientDetailsService接口来进行定制(可以参考JdbcClientDetailsService)。
//   clients.withClientDetails(clientDetailsService);
    }

    /**
     * 会通过之前的ClientDetailsServiceConfigurer注入到Spring容器中
     */
    @Resource
    private ClientDetailsService clientDetailsService;

    @Resource
    private TokenStore tokenStore;
/*
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); // 客户端详情服务
        service.setSupportRefreshToken(true); // 允许令牌自动刷新
        service.setTokenStore(tokenStore); // 令牌存储策略-内存
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }
*/
    //使用JWT令牌
    @Resource
    private JwtAccessTokenConverter accessTokenConverter;

    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); //客户端详情服务
        service.setSupportRefreshToken(true); //允许令牌自动刷新
        service.setTokenStore(tokenStore); //令牌存储策略-内存
        //使用JWT令牌
        service.setTokenEnhancer(accessTokenConverter);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    @Resource
    private AuthorizationCodeServices authorizationCodeServices;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
//                .pathMapping("/oauth/confirm_access", "/customer/confirm_access")// 定制授权同意页面
                .authenticationManager(authenticationManager)// 认证管理器
                .userDetailsService(userDetailsService)// 密码模式的用户信息管理
                .authorizationCodeServices(authorizationCodeServices)// 授权码服务
                .tokenServices(tokenService())// 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 设置授权码模式的授权码如何存取，暂时用内存方式。
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
        // JdbcAuthorizationCodeServices
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") // oauth/token_key公开
                .checkTokenAccess("permitAll()") // oauth/check_token公开
                .allowFormAuthenticationForClients(); // 表单认证，申请令牌
    }
}
