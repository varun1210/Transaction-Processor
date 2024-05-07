package com.transactionprocessor.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {

    private String userID;
    private String messageID;
    private String eventType;
    private BigDecimal amount;
    private String currency;
    private String debitOrCredit;

}
