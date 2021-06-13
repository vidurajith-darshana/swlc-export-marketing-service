package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

public interface Oauth2UserService {

    ResponseEntity<CommonResponseDTO> createUser(UserDto userDto);
}
