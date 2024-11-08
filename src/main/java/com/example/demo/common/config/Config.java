package com.example.demo.common.config;

import com.example.demo.common.dataparser.CsvDataParser;
import com.example.demo.common.dataparser.DataParser;
import com.example.demo.common.dataparser.JsonDataParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class Config {

    @Value("${file.type}")
    private String fileType;

    @Value("${file.account-path}")
    private String accountPath;

    @Value("${file.price-path}")
    private String pricePath;

    @Bean
    public DataParser dataParser() {
        if ("json".equalsIgnoreCase(fileType)) {
            System.out.println("Using JSON parser for accounts.");
            return new JsonDataParser(accountPath, pricePath);
        } else if ("csv".equalsIgnoreCase(fileType)) {
            System.out.println("Using CSV parser for accounts.");
            return new CsvDataParser(accountPath, pricePath);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}
