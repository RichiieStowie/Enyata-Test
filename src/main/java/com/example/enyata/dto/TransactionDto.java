package com.example.enyata.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class TransactionDto {
    private String email;
    private BigDecimal amountToBePaid;
    private String productCategory;
}
