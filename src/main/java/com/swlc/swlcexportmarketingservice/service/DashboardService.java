package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

/**
 * @author hp
 */
public interface DashboardService {
    ResponseEntity<CommonResponseDTO> getAdminDashboardData();
}
