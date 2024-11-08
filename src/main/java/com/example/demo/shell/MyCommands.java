package com.example.demo.shell;

import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import com.example.demo.price.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@ShellComponent
public class MyCommands {

    private final AuthenticationService authenticationService;
    private boolean islogin = false;

    private final PriceService priceService;
    @ShellMethod
    public String login(long id, String password) {
        if(islogin){
            return "이미 로그인이 됐습니다.";
        }
        Account loginuser = authenticationService.login(id,password);
        if(Objects.nonNull(loginuser)){
            islogin = true;
            return "login!";
        }else{
            return "login fail";
        }
    }

    @ShellMethod
    public String logout() {
        if(islogin){
            authenticationService.logout();
            islogin = false;
            return "로그아웃 했습니다.";
        }
        else{
            return "로그아웃이 되어있습니다.";
        }
    }

    @ShellMethod
    public String currentUser() {
        if (islogin) {
            try {
                return authenticationService.getCurrentAccount().toString();
            } catch (Exception e) {
                return "사용자 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage();
            }
        } else {
            return "접속 중인 유저가 없습니다.";
        }
    }

    @ShellMethod
    public String city() {
        try {
            return priceService.cities().toString();
        } catch (Exception e) {
            return "도시 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @ShellMethod
    public String sector(String city) {
        if (Objects.isNull(city) || city.isEmpty()) {
            return "도시 이름을 입력하세요.";
        }
        try {
            return priceService.sectors(city).toString();
        } catch (Exception e) {
            return "섹터 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @ShellMethod
    public String price(String city, String sector) {
        if (Objects.isNull(city) || city.isEmpty() || Objects.isNull(sector) || sector.isEmpty()) {
            return "도시와 섹터 이름을 입력하세요.";
        }
        try {
            return priceService.price(city, sector).toString();
        } catch (Exception e) {
            return "요금을 불러오는 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @ShellMethod
    public String billTotal(String city, String sector, int usage) {
        if (Objects.isNull(city) || city.isEmpty() || Objects.isNull(sector) || sector.isEmpty()) {
            return "도시와 섹터 이름을 입력하세요.";
        }
        if (usage <= 0) {
            return "사용량은 1 이상이어야 합니다.";
        }
        try {
            return priceService.billTotal(city, sector, usage);
        } catch (Exception e) {
            return "요금 계산 중 오류가 발생했습니다: " + e.getMessage();
        }
    }


}