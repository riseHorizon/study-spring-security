package com.horizon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 默认Url根路径跳转到/login，此url为spring security提供
        registry.addViewController("/").setViewName("redirect:/login");
    }

    /**
     * 自行注入一个PasswordEncoder。
     */
    @Bean
    public PasswordEncoder getPassWordEncoder(){
        // 密码处理器
        return new BCryptPasswordEncoder(10);
        // 非加密密码处理器
        // return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 自行注入一个UserDetailsService
     * 如果没有的话，在UserDetailsServiceAutoConfiguration中会默认注入一个包含user用户的InMemoryUserDetailsManager
     * 另外也可以采用修改configure(AuthenticationManagerBuilder auth)方法并注入authenticationManagerBean的方式。
     */
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager userDetailsManager =
                new InMemoryUserDetailsManager(
                        User.withUsername("admin").password(getPassWordEncoder().encode("admin"))
                                .authorities("mobile", "salary").build(),
                        User.withUsername("manager").password(getPassWordEncoder().encode("manager"))
                                .authorities("salary").build(),
                        User.withUsername("worker").password(getPassWordEncoder().encode("worker"))
                                .authorities("worker").build());
        return userDetailsManager;
//      return new JdbcUserDetailsManager(DataSource dataSource);
    }
}
