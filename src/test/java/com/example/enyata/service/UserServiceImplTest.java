package com.example.enyata.service;

import com.example.enyata.dto.TransactionDto;
import com.example.enyata.enums.ProductCategory;
import com.example.enyata.enums.UserStatus;
import com.example.enyata.models.User;
import com.example.enyata.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldReturnCorrectAmountForCustomerBuyingGroceries(){
        User user = new User();
        user.setEmail("ben@example.com");
        user.setUserId(1l);
        user.setUserStatus(UserStatus.CUSTOMER);
        user.setDateJoined(LocalDate.parse("2021-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("ben@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.GROCERIES));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));


        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(950));
    }

    @Test
    void shouldReturnCorrectAmountForCustomerWithOverTwoYearsNotBuyingGroceries(){
        User user2 = new User();
        user2.setUserId(2l);
        user2.setDateJoined(LocalDate.parse("2008-09-07"));
        user2.setUserStatus(UserStatus.CUSTOMER);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("ben@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.OTHERS));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user2));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(905));

    }

    @Test
    void shouldReturnCorrectAmountForCustomerWithLessThanTwoYearsNotBuyingGroceries(){
        User user = new User();
        user.setEmail("ben@example.com");
        user.setUserId(1l);
        user.setUserStatus(UserStatus.CUSTOMER);
        user.setDateJoined(LocalDate.parse("2021-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("ben@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.OTHERS));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(950));

    }

    @Test
    void shouldReturnCorrectAmountForEmployeeNotBuyingGroceries(){
        User user3 = new User();
        user3.setUserId(3l);
        user3.setUserStatus(UserStatus.EMPLOYEE);
        user3.setEmail("jane@example.com");
        user3.setDateJoined(LocalDate.parse("2011-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("ben@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.OTHERS));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user3));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(666));
    }

    @Test
    void shouldReturnCorrectAmountForEmployeeBuyingGroceries(){
        User user3 = new User();
        user3.setUserId(3l);
        user3.setUserStatus(UserStatus.EMPLOYEE);
        user3.setEmail("jane@example.com");
        user3.setDateJoined(LocalDate.parse("2011-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("jane@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.GROCERIES));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user3));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(950));
    }

    @Test
    void shouldReturnSameAmount(){
        User user3 = new User();
        user3.setUserId(3l);
        user3.setUserStatus(UserStatus.EMPLOYEE);
        user3.setEmail("jane@example.com");
        user3.setDateJoined(LocalDate.parse("2011-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("jane@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.GROCERIES));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(90));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user3));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(90));
    }
    @Test
    void shouldReturnCorrectAmountForAffilateBuyingGroceries(){
        User user4 = new User();
        user4.setUserId(4l);
        user4.setUserStatus(UserStatus.AFFILATE);
        user4.setEmail("obi@example.com");
        user4.setDateJoined(LocalDate.parse("2015-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("obi@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.GROCERIES));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user4));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(950));
    }

    @Test
    void shouldReturnCorrectAmountForAffilateNotBuyingGroceries(){
        User user4 = new User();
        user4.setUserId(4l);
        user4.setUserStatus(UserStatus.AFFILATE);
        user4.setEmail("obi@example.com");
        user4.setDateJoined(LocalDate.parse("2015-05-09"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEmail("obi@example.com");
        transactionDto.setProductCategory(String.valueOf(ProductCategory.OTHERS));
        transactionDto.setAmountToBePaid(BigDecimal.valueOf(1000));

        BDDMockito.given(userRepository.findByEmail(transactionDto.getEmail())).willReturn(Optional.of(user4));
        BigDecimal amount = userService.calculatePayment(transactionDto);
        Assertions.assertThat(amount).isEqualTo(BigDecimal.valueOf(855));
    }
}
