package com.example.demo.price.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Price {
    @JsonProperty("순번")
    long id;
    @JsonProperty("지자체명")
    String city;
    @JsonProperty("업종")
    String sector;
    @JsonProperty("구간금액(원)")
    int unitPrice;

    public Price(long id, String city, String sector, int unitPrice) {
        this.id = id;
        this.city = city;
        this.sector = sector;
        this.unitPrice = unitPrice;
    }
}
