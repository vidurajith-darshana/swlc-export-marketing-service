package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.service.TestService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger LOGGER = LogManager.getLogger(TestServiceImpl.class);

    @Override
    public String testMethod() {
        try{
            LOGGER.info("Execute testMethod Method @Param: No");
            // body here

            return "ok";

        } catch (Exception e) {
            LOGGER.error("Method testMethod : " + e.getMessage(), e);
            throw e;
        }
    }
}
