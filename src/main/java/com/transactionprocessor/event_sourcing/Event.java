package com.transactionprocessor.event_sourcing;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "event_Type")
@Table(name = "events")
@Getter
public abstract class Event {
    @Id
    @Column(name = "event_ID", updatable = false)
    @GeneratedValue(generator = "uuid")
    private UUID eventID;

    @Column(name = "created_Timestamp", updatable = false)
    private Date createdTimestamp;

    @Column(name = "event_Type", insertable = false, updatable = false)
    private String eventType;
    public Event(EventType eventType, Date createdTimestamp) {
        this.eventType = eventType.toString();
        this.createdTimestamp = createdTimestamp;
    }

}
