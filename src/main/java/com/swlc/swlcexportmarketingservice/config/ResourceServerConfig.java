package com.swlc.swlcexportmarketingservice.config;

import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                // Admin
                .antMatchers("/api/v1/user/operator/create").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_ADMIN.toString())
                .antMatchers("/api/v1/admin/category/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
                .antMatchers("/api/v1/admin/product/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")

                // Registered User(Customer)
                .antMatchers("/api/v1/user/**").access("hasAuthority('ROLE_USER')")
                .antMatchers("/api/order/**").access("hasAuthority('ROLE_USER')")

                // Any User
                .antMatchers("/api/v1/user/customer/create").permitAll()
                .antMatchers("/api/v1/user/category/**").permitAll()
                .antMatchers("/api/v1/user/product/**").permitAll()
                .antMatchers("/**").permitAll()

                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        http.csrf().disable();
    }
}
