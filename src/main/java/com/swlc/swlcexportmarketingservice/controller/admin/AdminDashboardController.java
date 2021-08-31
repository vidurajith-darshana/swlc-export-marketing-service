package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hp
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getDashboardData(HttpServletRequest httpServletRequest) {
        log.info("End point: " + httpServletRequest.getPathInfo());
        return dashboardService.getAdminDashboardData();
    }
}
