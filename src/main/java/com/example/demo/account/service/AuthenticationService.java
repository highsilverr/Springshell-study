package com.example.demo.account.service;

import com.example.demo.account.dto.Account;
import com.example.demo.common.dataparser.DataParser;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;

@Component
@Getter
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private Account currentAccount;
    private  final DataParser dataParser;

    public AuthenticationService(DataParser dataParser){
        this.dataParser = dataParser;
    }
    public Account login(Long id, String password) {
        if (id == null || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("아이디나 비밀번호는 null이거나 빈 값일 수 없습니다.");
        }

        for (Account account : dataParser.accounts()) {
            if (account.getId() == id && Objects.equals(account.getPassword(), password)) {
                currentAccount = account;
                return currentAccount;
            }
        }

        // 로그인 실패 시 예외 처리
        try {
            throw new AuthenticationException("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        if(Objects.nonNull(currentAccount)){
            currentAccount = null;
        }
        else{
            log.debug("이미 로그아웃되어있습니다.");
        }
    }

}
