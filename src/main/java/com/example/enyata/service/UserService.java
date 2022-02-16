package com.example.enyata.service;

import com.example.enyata.dto.TransactionDto;
import com.example.enyata.dto.UserDto;
import com.example.enyata.models.Transaction;
import com.example.enyata.models.User;

import java.math.BigDecimal;

public interface UserService {
    void createNewUser(UserDto userDto);
    BigDecimal calculatePayment(TransactionDto transactionDto);
}
