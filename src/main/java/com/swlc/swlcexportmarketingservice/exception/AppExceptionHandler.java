package com.swlc.swlcexportmarketingservice.exception;

import com.swlc.swlcexportmarketingservice.dto.common.ErrorMessageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ErrorMessageResponseDTO> handleAnyException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMessageResponseDTO(false, 100, APPLICATION_ERROR_OCCURRED_MESSAGE), HttpStatus.EXPECTATION_FAILED);
    }
}
