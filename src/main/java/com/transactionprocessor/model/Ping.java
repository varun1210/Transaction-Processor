package com.transactionprocessor.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Ping {

    String serverTime;

    public Ping() {
        this.serverTime = LocalDateTime.now().toString();
    }

}
