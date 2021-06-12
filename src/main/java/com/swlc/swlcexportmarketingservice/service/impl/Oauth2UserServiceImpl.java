package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service(value = "userService")
public class Oauth2UserServiceImpl implements UserDetailsService, Oauth2UserService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            String clientId = user.getUsername();

            if(clientId.equals("ADMIN_CLIENT_ID")) {
//                username: "em_admin"
//                password: "123"
                return new org.springframework.security.core.userdetails.User("em_admin", "$2y$12$MWcHkYnx.ltgF2B7iNfb6u.tcgi8Yt9pcQ902S3BSAikLYSOG2WqO",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else if(clientId.equals("DATA_ENTRY_OPERATOR")) {
//                username: "em_dep"
//                password: "123"
                return new org.springframework.security.core.userdetails.User("em_dep", "$2y$12$MWcHkYnx.ltgF2B7iNfb6u.tcgi8Yt9pcQ902S3BSAikLYSOG2WqO",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEP")));
            } else {
//                username: "em_user"
//                password: "123"
                return new org.springframework.security.core.userdetails.User("em_user", "$2y$12$MWcHkYnx.ltgF2B7iNfb6u.tcgi8Yt9pcQ902S3BSAikLYSOG2WqO",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            }

        } catch (Exception e) {
            throw e;
        }
    }
}
