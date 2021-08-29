package com.swlc.swlcexportmarketingservice.util;

import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author hp
 */
@Slf4j
@Component
public class TokenValidator {

    private final UserRepository userRepository;

    @Autowired
    public TokenValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User retrieveUserInformationFromAuthentication() {
        log.info("Execute method retrieveUserInformationFromAuthentication");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {

                System.out.println("EMAIL: " + authentication.getName());

                User userByEmail = userRepository.findUserByEmail(authentication.getName());
                if (userByEmail == null) throw new SwlcExportMarketException(404, "Invalid user request");
                return userByEmail;
            }
            throw new SwlcExportMarketException(404, "Invalid user request2");
        } catch (Exception e) {
            log.error("Method retrieveUserInformationFromAuthentication : " + e.getMessage());
            throw e;
        }
    }

}
