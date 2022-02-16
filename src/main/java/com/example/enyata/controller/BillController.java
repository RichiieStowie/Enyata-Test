package com.example.enyata.controller;

import com.example.enyata.dto.TransactionDto;
import com.example.enyata.dto.UserDto;
import com.example.enyata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class BillController {
    public final UserService userService;
    @Autowired
    public BillController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody UserDto userDto){
        userService.createNewUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> calculatePayment(@RequestBody TransactionDto transactionDto){
        System.out.println(transactionDto.getAmountToBePaid());
        BigDecimal amount = userService.calculatePayment(transactionDto);
        return new ResponseEntity<>("Amount to be paid is: "+ amount,HttpStatus.OK);
    }
}
