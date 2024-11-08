package com.example.demo.price.service;

import com.example.demo.common.dataparser.DataParser;
import com.example.demo.price.dto.Price;
import com.example.demo.price.formatter.OutPutFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PriceService {

    private final DataParser dataParser;
    private OutPutFormatter outPutFormatter;

    @Autowired
    public PriceService(DataParser dataParser, OutPutFormatter outPutFormatter) {
        this.dataParser = dataParser;
        this.outPutFormatter = outPutFormatter;
    }

    public List<String> cities() {
        try {
            List<String> cities = dataParser.cities();
            if (cities == null || cities.isEmpty()) {
                throw new RuntimeException("도시 목록을 불러오는 데 실패했습니다.");
            }
            return cities;
        } catch (Exception e) {
            throw new RuntimeException("도시 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public List<String> sectors(String city) {
        if (Objects.isNull(city) || city.isEmpty()) {
            throw new IllegalArgumentException("도시 이름이 비어 있거나 null입니다.");
        }

        try {
            List<String> sectors = dataParser.sectors(city);
            if (sectors == null || sectors.isEmpty()) {
                throw new RuntimeException("해당 도시의 섹터 목록을 불러오는 데 실패했습니다.");
            }
            return sectors;
        } catch (Exception e) {
            throw new RuntimeException("섹터 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public Price price(String city, String sector) {
        if (Objects.isNull(city) || city.isEmpty() || Objects.isNull(sector) || sector.isEmpty()) {
            throw new IllegalArgumentException("도시와 섹터 이름이 비어 있거나 null입니다.");
        }

        try {
            Price price = dataParser.price(city, sector);
            if (price == null) {
                throw new RuntimeException("해당 도시와 섹터에 대한 요금을 불러오는 데 실패했습니다.");
            }
            return price;
        } catch (Exception e) {
            throw new RuntimeException("요금 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public String billTotal(String city, String sector, int usage) {
        if (Objects.isNull(city) || city.isEmpty() || Objects.isNull(sector) || sector.isEmpty()) {
            throw new IllegalArgumentException("도시와 섹터 이름이 비어 있거나 null입니다.");
        }

        if (usage <= 0) {
            throw new IllegalArgumentException("사용량은 1 이상이어야 합니다.");
        }

        try {
            Price price = dataParser.price(city, sector);
            if (price == null) {
                throw new RuntimeException("해당 도시와 섹터에 대한 요금을 불러오는 데 실패했습니다.");
            }
            return outPutFormatter.format(price, usage);
        } catch (Exception e) {
            throw new RuntimeException("사용량 계산 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

}
