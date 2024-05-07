package com.transactionprocessor.utils.responses;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthorizationResponse extends Response{

    private String responseCode;
    private String balance;

    public AuthorizationResponse(String userID, String messageID, String responseCode, String balance) {
        super(userID, messageID);
        this.responseCode = responseCode;
        this.balance = balance;
    }

}
