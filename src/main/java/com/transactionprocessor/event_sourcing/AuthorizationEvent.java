package com.transactionprocessor.event_sourcing;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@DiscriminatorValue("AUTHORIZATION")
@Getter
public class AuthorizationEvent extends Event{

    @Column(name = "user_Id")
    private final String userId;

    @Column(name = "message_Id")
    private final String messageId;

    @Column(name = "amount")
    private final BigDecimal amount;

    public AuthorizationEvent(String userId, String messageId, BigDecimal amount) {
        super(EventType.AUTHORIZATION, new Date());
        this.userId = userId;
        this.messageId = messageId;
        this.amount = amount;
    }
}
