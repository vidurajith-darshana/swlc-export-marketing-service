package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.repository.UserRepository;
import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.APPLICATION_ERROR_OCCURRED_MESSAGE;
import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.REQUEST_SUCCESS_MESSAGE;

@Service(value = "userService")
public class Oauth2UserServiceImpl implements UserDetailsService, Oauth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LogManager.getLogger(Oauth2UserServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {

            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserByEmail(email);
            if (user != null) {

                return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
            } else {
                throw new UsernameNotFoundException("User not found!");
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> createUser(UserDto userDto) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserByEmail(userDto.getEmail());
            if (user == null && userDto.getEmail() != null) {

                user = new com.swlc.swlcexportmarketingservice.entity.User();
                user.setEmail(userDto.getEmail());
                user.setFirstName(userDto.getFirstName());
                user.setFirstName(userDto.getLastName());
                user.setPreferredLanguage(userDto.getLanguage());
                user.setRole(userDto.getRole());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                userRepository.save(user);

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "User created successfully!"), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Given email already exists!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
