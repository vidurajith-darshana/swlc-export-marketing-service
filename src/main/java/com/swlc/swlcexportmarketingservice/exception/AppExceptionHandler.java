package com.swlc.swlcexportmarketingservice.exception;

import com.swlc.swlcexportmarketingservice.dto.common.ErrorMessageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler({SwlcExportMarketException.class})
    public ResponseEntity handleCommonException(SwlcExportMarketException e) {
        return error(OK, e);
    }

    private ResponseEntity<ErrorMessageResponseDTO> error(HttpStatus status, SwlcExportMarketException e) {
        log.error("Exception : " + e.getMessage());
        return new ResponseEntity<>(
                new ErrorMessageResponseDTO(false, e.getCode(), e.getMessage()), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ErrorMessageResponseDTO> handleAnyException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMessageResponseDTO(false, 100, APPLICATION_ERROR_OCCURRED_MESSAGE), HttpStatus.EXPECTATION_FAILED);
    }


}
