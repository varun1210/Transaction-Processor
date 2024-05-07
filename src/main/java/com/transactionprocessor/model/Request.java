package com.transactionprocessor.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Request {

    @NotBlank
    private String userId;

    @NotBlank
    private String messageId;

    private Amount transactionAmount;


}
