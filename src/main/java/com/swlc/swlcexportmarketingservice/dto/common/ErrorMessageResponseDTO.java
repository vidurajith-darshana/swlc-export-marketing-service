package com.swlc.swlcexportmarketingservice.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponseDTO {
    private boolean success;
    private int errorCode;
    private Object errorContent;
}
