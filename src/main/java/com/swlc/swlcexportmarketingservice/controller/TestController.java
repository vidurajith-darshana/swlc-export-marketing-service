package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

/**
 * This is testing controller only
 *
 * @author Kavindu
 */
@RestController
@CrossOrigin
@RequestMapping("v1/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommonResponseDTO> testEndPoint() {
        String value = testService.testMethod();
        return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, value), HttpStatus.OK);
    }


}
