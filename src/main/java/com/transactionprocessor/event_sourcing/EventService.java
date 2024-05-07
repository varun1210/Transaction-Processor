package com.transactionprocessor.event_sourcing;

import com.transactionprocessor.DTO.TransactionDTO;
import com.transactionprocessor.repository.EventRepository;
import com.transactionprocessor.utils.exceptions.CurrentApplicationExceptionHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentApplicationExceptionHandler currentApplicationExceptionHandler;

    @Autowired
    public EventService(EventRepository eventRepository, ApplicationEventPublisher eventPublisher, CurrentApplicationExceptionHandler currentApplicationExceptionHandler) {
        this.eventRepository = eventRepository;
        this.eventPublisher = eventPublisher;
        this.currentApplicationExceptionHandler = currentApplicationExceptionHandler;
    }


    public Event createEvent(TransactionDTO transactionDTO) {
        if(transactionDTO.getEventType().equals(EventType.AUTHORIZATION.toString())) {
            return new AuthorizationEvent(
                    transactionDTO.getUserID(),
                    transactionDTO.getMessageID(),
                    transactionDTO.getAmount()
            );
        } else {
            return new LoadEvent(
                    transactionDTO.getUserID(),
                    transactionDTO.getMessageID(),
                    transactionDTO.getAmount()
            );
        }
    }

    @Transactional
    public void pushEvent(Event event) {
        try {
            eventRepository.save(event);
            eventPublisher.publishEvent(event);
//            System.out.println("Event published!!!");
        } catch (Exception e) {
            currentApplicationExceptionHandler.exceptionHandler(e);
        }
    }
}
