package com.transactionprocessor.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Amount {

    @NotBlank
    private String amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String debitOrCredit;

}
