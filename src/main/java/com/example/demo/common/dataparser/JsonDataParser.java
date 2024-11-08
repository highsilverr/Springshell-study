package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.price.dto.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.shell.command.invocation.InvocableShellMethod.log;


public class JsonDataParser implements DataParser {

    private final String accountPath;
    private final String pricePath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonDataParser(String accountPath, String pricePath) {
        this.accountPath = accountPath;
        this.pricePath = pricePath;
    }

    @Override
    public List<String> cities() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath)) {
            if (inputStream == null) {
                log.error("price.json file not found in resources.");
                return Collections.emptyList();
            }

            List<Map<String, Object>> records = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            return records.stream()
                    .map(record -> (String) record.get("지자체명"))
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error reading JSON file for cities: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> sectors(String city) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath)) {
            if (inputStream == null) {
                log.error("price.json file not found in resources.");
                return Collections.emptyList();
            }

            List<Map<String, Object>> records = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            return records.stream()
                    .filter(record -> city.equals(record.get("지자체명")))
                    .map(record -> (String) record.get("업종"))
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error reading JSON file for sectors: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Price price(String city, String sector) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath)) {
            if (inputStream == null) {
                log.error("price.json file not found in resources.");
                return null;
            }

            List<Map<String, Object>> records = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> record : records) {
                if (city.equals(record.get("지자체명")) && sector.equals(record.get("업종"))) {
                    long id = Long.parseLong(record.get("순번").toString().trim());
                    String cityName = record.get("지자체명").toString().trim();
                    String sectorName = record.get("업종").toString().trim();
                    int unitPrice = Integer.parseInt(record.get("구간금액(원)").toString().trim());

                    return new Price(id, cityName, sectorName, unitPrice);
                }
            }
        } catch (IOException e) {
            log.error("Error reading JSON file for prices: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Account> accounts() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(accountPath)) {
            if (inputStream == null) {
                log.error("account.json file not found in resources.");
                return null;
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<Account>>() {});
        } catch (IOException e) {
            log.error("account.json parse error message: {}", e.getMessage(), e);
            return null;
        }
    }

}