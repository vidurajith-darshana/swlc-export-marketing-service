package com.swlc.swlcexportmarketingservice.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CustomGenerator {

    /**
     * @return this function will return a five digit random number as String
     */
    public String generateNumber() {
        Random r = new Random();
        int low = 1000;
        int high = 10000;
        int code = r.nextInt(high - low) + low;
        return String.valueOf(code);
    }
}
