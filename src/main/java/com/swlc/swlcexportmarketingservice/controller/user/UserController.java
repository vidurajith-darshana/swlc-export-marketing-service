package com.swlc.swlcexportmarketingservice.controller.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import com.swlc.swlcexportmarketingservice.dto.DeliveryDto;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.Oauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/operator/update")
    public ResponseEntity<CommonResponseDTO> updateOperator(@RequestBody UserDto dto) {
        return oauth2UserService.updateOperator(dto);
    }

    @GetMapping("/operator/get-all")
    public ResponseEntity<CommonResponseDTO> getAllOperator() {
            return oauth2UserService.getAllOperator();
    }

    @DeleteMapping("/operator/remove")
    public ResponseEntity<CommonResponseDTO> deleteOperator(@RequestParam int id) {
        return oauth2UserService.deleteOperator(id);
    }

    @GetMapping("/getDetails/{email:.+}")
    public ResponseEntity<CommonResponseDTO> getUser(@PathVariable("email") String email) {
        return oauth2UserService.getUser(email);
    }

    @PostMapping("/customer/save/delivery-details")
    public ResponseEntity<CommonResponseDTO> saveDeliveryDetails(@RequestBody DeliveryDto deliveryDto) {
        return oauth2UserService.saveDeliveryDetails(deliveryDto);
    }

    @PostMapping("/customer/update")
    public ResponseEntity<CommonResponseDTO> updateCustomer(@RequestBody UserDto userDto) {
        if (userDto.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_CUSTOMER.toString())) {
            return oauth2UserService.updateUser(userDto);
        } else {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "This API valid only to update customers!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/operator/update")
//    public ResponseEntity<CommonResponseDTO> updateOperator(@RequestBody UserDto userDto) {
//        if (userDto.getRole().equals(ApplicationConstant.USER_ROLES.ROLE_OPERATOR.toString())) {
//            return oauth2UserService.updateUser(userDto);
//        } else {
//            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "This API valid only to update operators!"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/customer/forget-password/{email:.+}")
    public ResponseEntity<CommonResponseDTO> forgetPassword(@PathVariable("email") String email) {
        return oauth2UserService.forgetPassword(email);
    }

    @PostMapping("/customer/reset-password")
    public ResponseEntity<CommonResponseDTO> resetPassword(@RequestBody JsonNode jsonNode) {
        String email = jsonNode.get("email").asText();
        String verifyCode = jsonNode.get("verifyCode").asText();
        String password = jsonNode.get("password").asText();

        return oauth2UserService.resetPassword(email,verifyCode,password);
    }

}
