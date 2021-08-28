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
                .antMatchers("/api/v1/testimonial/create").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_ADMIN.toString())
                .antMatchers("/api/v1/testimonial/delete/{id}").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_ADMIN.toString())
                .antMatchers("/api/v1/admin/category/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
                .antMatchers("/api/v1/admin/product/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
                .antMatchers("/api/v1/admin/promotion/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")

                // Registered User(Customer)
                .antMatchers("/api/order/**").access("hasAnyRole('ROLE_OPERATOR','ROLE_CUSTOMER','ROLE_ADMIN')")

                // Any User
                .antMatchers("/api/v1/testimonial/getAll").permitAll()
                .antMatchers("/api/v1/user/category/**").permitAll()
                .antMatchers("/api/v1/user/product/**").permitAll()
                .antMatchers("/api/v1/user/promotion/**").permitAll()
                .antMatchers("/api/v1/user/customer/create").permitAll()
                .antMatchers("/api/v1/user/customer/forget-password").permitAll()
                .antMatchers("/api/v1/user/customer/reset-password").permitAll()
                .antMatchers("/swlc-data/**").permitAll()

                .antMatchers("/api/v1/user/operator/create").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_ADMIN.toString())
                .antMatchers("/api/v1/user/customer/save/delivery-details").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())
                .antMatchers("/api/v1/user/customer/update").hasAuthority(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())
                .antMatchers("/api/v1/user/operator/update").access("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        http.csrf().disable();
    }
}
