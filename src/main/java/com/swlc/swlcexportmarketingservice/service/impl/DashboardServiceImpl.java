package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.GetDashboardResponseDTO;
import com.swlc.swlcexportmarketingservice.repository.*;
import com.swlc.swlcexportmarketingservice.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.REQUEST_SUCCESS_MESSAGE;

/**
 * @author hp
 */
@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<CommonResponseDTO> getAdminDashboardData() {
        log.info("Execute getAdminDashboardData: ");
        try {
            int countOfOrders = orderRepository.getCountOfOrders();
            int countOfUser = userRepository.getCountOfUser();
            int countOfProducts = productRepository.getCountOfProducts();
            int countOfCategories = categoryRepository.getCountOfCategories();
            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, new GetDashboardResponseDTO(countOfUser, countOfProducts, countOfCategories, countOfOrders)), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Execute getAdminDashboardData: " + e.getMessage(), e);
            throw e;
        }
    }

}
