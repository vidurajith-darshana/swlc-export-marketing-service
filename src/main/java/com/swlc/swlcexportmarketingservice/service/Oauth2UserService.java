package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.DeliveryDto;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

public interface Oauth2UserService {

    ResponseEntity<CommonResponseDTO> createUser(UserDto userDto);

    ResponseEntity<CommonResponseDTO> getUser(String email);

    ResponseEntity<CommonResponseDTO> saveDeliveryDetails(DeliveryDto deliveryDto);

    ResponseEntity<CommonResponseDTO> updateUser(UserDto userDto);

    ResponseEntity<CommonResponseDTO> forgetPassword(String email);

    ResponseEntity<CommonResponseDTO> resetPassword(String email,String verifyCode,String password);
}
