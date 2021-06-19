package com.swlc.swlcexportmarketingservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SwlcExportMarketException extends RuntimeException{

    private int code;
    private String message;


    public SwlcExportMarketException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public SwlcExportMarketException(int code, String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public SwlcExportMarketException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

}
