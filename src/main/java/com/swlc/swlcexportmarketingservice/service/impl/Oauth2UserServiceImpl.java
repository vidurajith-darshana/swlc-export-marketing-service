package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import com.swlc.swlcexportmarketingservice.dto.DeliveryDto;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.DeliveryDetail;
import com.swlc.swlcexportmarketingservice.repository.DeliveryRepository;
import com.swlc.swlcexportmarketingservice.repository.UserRepository;
import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import com.swlc.swlcexportmarketingservice.utility.HtmlToString;
import com.swlc.swlcexportmarketingservice.utility.MailSender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Service(value = "userService")
public class Oauth2UserServiceImpl implements UserDetailsService, Oauth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MailSender mailSender;

    @Value("classpath:html-templates/forgot-password.html")
    Resource forgetPasswordHtml;

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

    @Override
    public ResponseEntity<CommonResponseDTO> getUser(String email) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserByEmail(email);
            if (user != null) {

                UserDto userDto = new UserDto();
                userDto.setEmail(email);
                userDto.setFirstName(user.getFirstName());
                userDto.setLastName(user.getLastName());
                userDto.setRole(user.getRole());
                userDto.setId(user.getId());

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, userDto), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "User not found!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> saveDeliveryDetails(DeliveryDto deliveryDto) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserById(deliveryDto.getFkUser());
            if (user != null && user.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())) {

                DeliveryDetail deliveryDetail = deliveryRepository.findDeliveryDetailByUser(user);

                if (deliveryDetail == null) {
                    deliveryDetail = new DeliveryDetail();
                    deliveryDetail.setUser(user);
                }
                if (deliveryDto.getDeliveryAddress() != null && !deliveryDto.getDeliveryAddress().isEmpty()) {
                    deliveryDetail.setDeliveryAddress(deliveryDto.getDeliveryAddress());
                }
                if (deliveryDto.getBuyerAddress() != null && !deliveryDto.getBuyerAddress().isEmpty()) {
                    deliveryDetail.setBuyerAddress(deliveryDto.getBuyerAddress());
                }
                if (deliveryDto.getMobile() != null && !deliveryDto.getMobile().isEmpty()) {
                    deliveryDetail.setMobile(deliveryDto.getMobile());
                }
                if (deliveryDto.getTelephone() != null && !deliveryDto.getTelephone().isEmpty()) {
                    deliveryDetail.setTelephone(deliveryDto.getTelephone());
                }
                if (deliveryDto.getWhatsapp() != null && !deliveryDto.getWhatsapp().isEmpty()) {
                    deliveryDetail.setWhatsapp(deliveryDto.getWhatsapp());
                }

                deliveryRepository.save(deliveryDetail);

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Delivery details updated successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "User not found or user not a customer!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> updateUser(UserDto userDto) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserById(userDto.getId());
            if (user != null && userDto.getRole().equals(user.getRole())) {

                if (userDto.getFirstName() != null && !userDto.getFirstName().isEmpty()) {
                    user.setFirstName(userDto.getFirstName());
                }
                if (userDto.getLastName() != null && !userDto.getLastName().isEmpty()) {
                    user.setLastName(userDto.getLastName());
                }

                userRepository.save(user);

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "User details updated successfully!"), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "User not found or role mismatched!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> forgetPassword(String email) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserByEmail(email);
            if (user != null && user.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())) {

                SecureRandom random = new SecureRandom();
                int num = random.nextInt(100000);
                String formatted = String.format("%05d", num);

                user.setVerifyCode(formatted);
                userRepository.save(user);

                String html = new HtmlToString().convertHtmlToString(forgetPasswordHtml.getFile().getPath());
                html = html.replace("xVerifyCode",formatted);

                mailSender.sendEmail(user.getEmail(),null, FORGOT_PASSWORD_EMAIL_SUBJECT, html, true, null);

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Verification code sent to your email. Please check the inbox!"), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "User not found or role mismatched!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> resetPassword(String email, String verifyCode, String password) {
        try {
            com.swlc.swlcexportmarketingservice.entity.User user = userRepository.findUserByEmail(email);
            if (user != null && user.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())) {

                if (user.getVerifyCode() != null && user.getVerifyCode().equals(verifyCode)) {
                    user.setPassword(passwordEncoder.encode(password));
                    user.setVerifyCode(null);
                    userRepository.save(user);

                    return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "User password updated successfully!"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Verify code not matched!"), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "User not found or role mismatched!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
