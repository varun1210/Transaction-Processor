package com.transactionprocessor.utils.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadResponse extends Response {

    private String balance;

    public LoadResponse(String userID, String messageID, String balance) {
        super(userID, messageID);
        this.balance = balance;
    }

}
