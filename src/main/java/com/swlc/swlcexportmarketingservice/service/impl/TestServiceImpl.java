package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String testMethod() {
        try{

            // body here

            return "ok";

        } catch (Exception e) {
            throw e;
        }
    }
}
