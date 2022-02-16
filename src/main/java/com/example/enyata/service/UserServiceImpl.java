package com.example.enyata.service;

import com.example.enyata.dto.TransactionDto;
import com.example.enyata.dto.UserDto;
import com.example.enyata.enums.ProductCategory;
import com.example.enyata.enums.UserStatus;
import com.example.enyata.exceptions.NotFoundException;
import com.example.enyata.models.Transaction;
import com.example.enyata.models.User;
import com.example.enyata.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

   @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createNewUser(UserDto userDto) {

       boolean status=userRepository.findByEmail(userDto.getEmail()).isEmpty();
       if(status){
           User user = new User();
           user.setUserStatus(UserStatus.valueOf(userDto.getRole()));
           user.setEmail(userDto.getEmail());
           user.setDateJoined(userDto.getDateJoined());
           User user1=userRepository.save(user);
           log.info(user1.getEmail());

       }else{
           throw new IllegalArgumentException("user already exist");
       }
    }

    @Override
    public BigDecimal calculatePayment(TransactionDto transactionDto) {
       BigDecimal amountToBePaid = transactionDto.getAmountToBePaid();
        User user = userRepository.findByEmail(transactionDto.getEmail())
                .orElseThrow(()-> new NotFoundException("User not found"));
        UserStatus status = user.getUserStatus();
        Period period = user.getDateJoined().until(LocalDate.now());
        int yearsOfMembership = period.getYears();
        if(!ProductCategory.GROCERIES.equals(ProductCategory.valueOf(transactionDto.getProductCategory()))) {
            if (status.equals(UserStatus.EMPLOYEE)) {
                Transaction transaction = calculatePaymentWithDiscount(transactionDto.getAmountToBePaid(), 0.3);
                transaction.setUser(user);
                amountToBePaid = transaction.getAmountPaid();

            } else if (status.equals(UserStatus.AFFILATE)) {
                Transaction transaction = calculatePaymentWithDiscount(transactionDto.getAmountToBePaid(), 0.1);
                transaction.setUser(user);
                amountToBePaid = transaction.getAmountPaid();
            } else if (status.equals(UserStatus.CUSTOMER) && yearsOfMembership >= 2) {
                Transaction transaction = calculatePaymentWithDiscount(transactionDto.getAmountToBePaid(), 0.05);
                transaction.setUser(user);
                amountToBePaid = transaction.getAmountPaid();
            }
        }
        return customaryDiscountPerThousand(amountToBePaid);
    }

    public Transaction calculatePaymentWithDiscount(BigDecimal amount,Double deduction){
       BigDecimal val1 = new BigDecimal(deduction);
       BigDecimal discount = val1.multiply(amount).setScale(0, RoundingMode.FLOOR);
       Transaction transaction = new Transaction();
       transaction.setAmountPaid(amount.subtract(discount).setScale(0,RoundingMode.FLOOR));
       transaction.setCreationTime(LocalDate.now());
       return transaction;
    }

    public BigDecimal customaryDiscountPerThousand(BigDecimal amount){
       int count =0;
       BigDecimal initialAmount = amount;
        BigDecimal val1 = new BigDecimal(100);
        BigDecimal val2 = new BigDecimal(5);
       while (amount.compareTo(val1)>=0){
           count++;
           amount=amount.subtract(val1);
       }
       BigDecimal discountAmount = val2.multiply(BigDecimal.valueOf(count));
       return initialAmount.subtract(discountAmount);
    }


}
