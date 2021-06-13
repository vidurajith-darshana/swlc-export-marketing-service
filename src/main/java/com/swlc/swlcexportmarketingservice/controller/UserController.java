package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.APPLICATION_ERROR_OCCURRED_MESSAGE;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private Oauth2UserService oauth2UserService;

    @PostMapping("/customer/create")
    public ResponseEntity<CommonResponseDTO> createUser(@RequestBody UserDto userDto) {
        if (userDto.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())) {
            return oauth2UserService.createUser(userDto);
        } else {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "This API valid only to create customers!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/operator/create")
    public ResponseEntity<CommonResponseDTO> createOperator(@RequestBody UserDto userDto) {
        if (userDto.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_OPERATOR.toString())) {
            return oauth2UserService.createUser(userDto);
        } else {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "This API valid only to create operators!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
