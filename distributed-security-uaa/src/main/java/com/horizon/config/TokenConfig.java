package com.horizon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    /**
     * 使用基于内存的普通令牌
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
    */

    private static final String SIGN_KEY="uaa";

    /**
     * 使用JWT令牌。
     */
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConvert());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConvert(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGN_KEY);
        return converter;
    }

}
