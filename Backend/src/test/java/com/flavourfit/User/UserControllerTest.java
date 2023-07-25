package com.flavourfit.User;

import com.flavourfit.Authentication.AuthController;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    IUserService userService;
    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void userPremiumPaymentTest() throws PaymentException {
        // Pass Case
        PremiumUserPaymentDetailsDto requestValid = new PremiumUserPaymentDetailsDto();
        int user_id = 1;
        String testToken = "token";
        when(authService.extractUserIdFromToken(testToken)).thenReturn(user_id);

        requestValid.setCardNumber("9876543210123456");
        requestValid.setExpiryMonth("02");
        requestValid.setExpiryYear("23");
        requestValid.setCvv("667");

        LocalDate currentDate = LocalDate.now();
        LocalDate nextYearDate = currentDate.plusYears(1);

        requestValid.setStartDate(java.sql.Date.valueOf(currentDate));
        requestValid.setEndDate(java.sql.Date.valueOf(nextYearDate));

        ResponseEntity responseEntity1 = userController.getUserPaymentForPremium(testToken, requestValid);

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());

    }



}
