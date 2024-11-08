package com.example.demo;
import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import com.example.demo.price.service.PriceService;
import com.example.demo.shell.MyCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MyCommandsLoginTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private MyCommands myCommands;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testLoginSuccess() {
        // Given
        Account account = new Account(1L, "1", "testUser");
        when(authenticationService.login(1L, "1")).thenReturn(account);

        // When
        String result = myCommands.login(1L, "1");

        // Then
        assertEquals("login!", result);
        verify(authenticationService, times(1)).login(1L, "1");
    }




}