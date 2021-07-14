package com.swlc.swlcexportmarketingservice.util;

import java.io.BufferedReader;
import java.io.FileReader;

public class HtmlToString {

    public String convertHtmlToString(String filePath) {

        StringBuilder html = new StringBuilder();
        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            String val;

            while ((val = br.readLine()) != null) {
                html.append(val);
            }
            br.close();

            return html.toString();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
