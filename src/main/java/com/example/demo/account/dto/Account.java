package com.example.demo.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    @JsonProperty("아이디")
    long id;
    @JsonProperty("비밀번호")
    String password;
    @JsonProperty("이름")
    String name;

    public Account(long id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

}
