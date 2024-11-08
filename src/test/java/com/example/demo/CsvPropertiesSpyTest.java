package com.example.demo;

import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import com.example.demo.common.dataparser.DataParser;
import com.example.demo.price.dto.Price;
import com.example.demo.price.formatter.EnglishOutputFormatter;
import com.example.demo.price.formatter.KoreanOutputFormatter;
import com.example.demo.price.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "file.type=csv",
        "file.price-path=price.csv",
        "file.account-path=account.csv"
})
public class CsvPropertiesSpyTest {

    @Spy
    private DataParser dataParser;

    @SpyBean
    private AuthenticationService authenticationService;

    @SpyBean
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void beanTest() {
//        assertInstanceOf(CsvDataParser.class, dataParser);
//        verify(dataParser, times(1)).parse();  // 데이터 파서의 parse 메서드 호출을 검증합니다.
//    }

    @Test
    void loginTest() throws Exception {
        // 테스트에서 사용할 가짜 Account 객체 생성
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setName("선도형");

        when(authenticationService.login(1L, "1")).thenReturn(mockAccount);
        authenticationService.login(1L, "1");

        Account account = authenticationService.getCurrentAccount();
        assertEquals("선도형", account.getName());

        verify(authenticationService, times(1)).login(1L, "1");

    }

    @Test
    void logoutTest() throws Exception {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setPassword("1");
        mockAccount.setName("선도형");

        when(authenticationService.login(1L, "1")).thenReturn(mockAccount);
        when(authenticationService.getCurrentAccount()).thenReturn(null);

        authenticationService.login(1L, "1");
        authenticationService.logout();

        Account account = authenticationService.getCurrentAccount();
        assertNull(account);

        verify(authenticationService, times(1)).logout();
    }

    @Test
    void cityTest() throws Exception {
        List<String> cities = priceService.cities();
        assertEquals(21, cities.size());
        assertTrue(cities.contains("동두천시"));
        verify(priceService, times(1)).cities();
    }

    @Test
    void sectorsTest() throws Exception {
        List<String> sectors = priceService.sectors("동두천시");
        assertEquals(5, sectors.size());
        assertTrue(sectors.contains("가정용"));
        verify(priceService, times(1)).sectors("동두천시");
    }

    @Test
    void priceTest() throws Exception {
        Price price = priceService.price("동두천시", "가정용");
        assertEquals("동두천시", price.getCity());
        assertEquals("가정용", price.getSector());
        assertEquals(690, price.getUnitPrice());
        verify(priceService, times(1)).price("동두천시", "가정용");
    }

    @Test
    void billTotalTest() throws Exception {
        String billTotal = priceService.billTotal("동두천시", "가정용", 10);
        assertTrue(billTotal.contains("690"));
        verify(priceService, times(1)).billTotal("동두천시", "가정용", 10);
    }

    @Test
    void outputFormatterTest() throws Exception {
        Price price = priceService.price("동두천시", "가정용");

        KoreanOutputFormatter koreanOutputFormatter = spy(new KoreanOutputFormatter());
        EnglishOutputFormatter englishOutputFormatter = spy(new EnglishOutputFormatter());

        String korean = koreanOutputFormatter.format(price, 10);
        String english = englishOutputFormatter.format(price, 10);

        assertTrue(korean.contains("690"));
        assertTrue(english.contains("690"));
        assertNotEquals(korean, english);

        verify(koreanOutputFormatter, times(1)).format(price, 10);
        verify(englishOutputFormatter, times(1)).format(price, 10);
    }
}
