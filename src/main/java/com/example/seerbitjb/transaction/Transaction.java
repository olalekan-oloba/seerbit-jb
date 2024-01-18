package com.example.seerbitjb.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("NullAway.Init")
public class Transaction {
    private BigDecimal amount;
    private Instant timeStamp;
}
