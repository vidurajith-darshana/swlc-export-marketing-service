package com.swlc.swlcexportmarketingservice.config;

import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomTokenEnhancer extends JwtAccessTokenConverter {
    private final Oauth2UserService oauth2UserService;
    private final HttpServletRequest request;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public CustomTokenEnhancer(Oauth2UserService oauth2UserService, HttpServletRequest request, BCryptPasswordEncoder encoder) {
        this.oauth2UserService = oauth2UserService;
        this.request = request;
        this.encoder = encoder;
    }


}
