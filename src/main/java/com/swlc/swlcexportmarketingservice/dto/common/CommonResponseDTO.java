package com.swlc.swlcexportmarketingservice.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDTO {
    private boolean success;
    private String message;
    private Object body;
}
