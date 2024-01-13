package com.example.seerbitjb.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Transaction {
    private BigDecimal amount;
    private Instant timeStamp;
}
