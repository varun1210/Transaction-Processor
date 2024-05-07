package com.transactionprocessor.utils.exceptions;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class Error {

    @NotBlank
    private String message;

    private String code;

    public Error(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public Error(String message) {
        this.message = message;
    }

}
