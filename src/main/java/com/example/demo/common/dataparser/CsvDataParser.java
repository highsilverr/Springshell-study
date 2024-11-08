package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.price.dto.Price;
import lombok.Value;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import static org.springframework.shell.command.invocation.InvocableShellMethod.log;
import static org.springframework.shell.component.view.event.KeyEvent.Key.e;


public class CsvDataParser implements DataParser{

    private final String accountPath;
    private final String pricePath;

    public CsvDataParser(String accountPath, String pricePath) {
        this.accountPath = accountPath;
        this.pricePath = pricePath;
    }

    @Override
    public List<String> cities() {
        Set<String> citiesSet = new HashSet<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {

            if (inputStream == null) {
                throw new IOException("File not found: " + pricePath);
            }

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                String city = csvRecord.get(" 지자체명 ").trim();
                citiesSet.add(city);
            }
        } catch (IOException e) {
            log.error("Error reading CSV file for cities: {}", e.getMessage());
        }
        return new ArrayList<>(citiesSet);
    }

    @Override
    public List<String> sectors(String city) {
        Set<String> sectorsSet = new HashSet<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {

            if (inputStream == null) {
                throw new IOException("File not found: " + pricePath);
            }

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                String recordCity = csvRecord.get(" 지자체명 ").trim();
                if (recordCity.equals(city)) {
                    String sector = csvRecord.get(" 업종 ").trim();
                    sectorsSet.add(sector);
                }
            }
        } catch (IOException e) {
            log.error("Error reading CSV file for sectors: {}", e.getMessage());
        }
        return new ArrayList<>(sectorsSet);
    }

    @Override
    public Price price(String city, String sector) {
        List<Price> prices = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pricePath);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {

            if (inputStream == null) {
                throw new IOException("File not found: " + pricePath);
            }

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                String recordCity = csvRecord.get(" 지자체명 ").trim();
                String recordSector = csvRecord.get(" 업종 ").trim();

                if (recordCity.equals(city) && recordSector.equals(sector)) { //// 순번 , 지자체명 , 업종 , 단계 , 구간시작(세제곱미터)  , 구간끝(세제곱미터)  , 구간금액(원)  , 단계별 기본요금(원)
                    int step = Integer.parseInt(csvRecord.get(" 순번 ").trim());
                    String start = csvRecord.get(" 지자체명 ").trim();
                    String end = csvRecord.get(" 업종 ").trim();
                    int priceAmount = Integer.parseInt(csvRecord.get(" 구간금액(원)  ").trim());


                    prices.add(new Price(step, start, end, priceAmount));
                }
            }
        } catch (IOException e) {
            log.error("Error reading CSV file for prices: {}", e.getMessage());
        }
        return prices.isEmpty() ? null : prices.get(0); // 첫 번째 Price 객체 반환 (필요에 따라 변경 가능)
    }

    @Override
    public List<Account> accounts() {
        List<Account> accounts = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(accountPath);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {

            if (inputStream == null) {
                throw new IOException("File not found: " + accountPath);
            }

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            // 읽어온 헤더를 출력하여 확인
            System.out.println("읽어온 헤더: " + csvParser.getHeaderNames());

            for (CSVRecord csvRecord : csvParser) {
                String idStr = csvRecord.get("아이디 ").trim();
                String password = csvRecord.get(" 비밀번호").trim();
                String name = csvRecord.get(" 이름").trim();

                long id = Long.parseLong(idStr);
                accounts.add(new Account(id, password, name));
            }
            log.info(accounts.toString());
        } catch (IOException e) {
            e.printStackTrace();  // 로깅 권장
        } catch (IllegalArgumentException e) {
            log.error("CSV 헤더 또는 데이터가 예상과 일치하지 않습니다: " + e.getMessage());
        }
        return accounts;
    }


}