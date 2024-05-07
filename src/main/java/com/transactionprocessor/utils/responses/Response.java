package com.transactionprocessor.utils.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Response {

    private String userID;
    private String messageID;

    public Response(String userID, String messageID) {
        this.userID = userID;
        this.messageID = messageID;
    }

}
