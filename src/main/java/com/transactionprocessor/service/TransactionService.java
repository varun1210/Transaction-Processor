package com.transactionprocessor.service;

import com.transactionprocessor.event_sourcing.*;
import com.transactionprocessor.utils.responses.Response;
import com.transactionprocessor.utils.responses.ResponseCode;
import com.transactionprocessor.DTO.TransactionDTO;
import com.transactionprocessor.model.AuthorizationRequest;
import com.transactionprocessor.model.DebitCredit;
import com.transactionprocessor.model.LoadRequest;
import com.transactionprocessor.model.Request;
import com.transactionprocessor.utils.exceptions.BadRequestException;
import com.transactionprocessor.utils.exceptions.CurrentApplicationExceptionHandler;
import com.transactionprocessor.utils.responses.AuthorizationResponse;
import com.transactionprocessor.utils.responses.LoadResponse;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class TransactionService {

    private final EventService eventService;
    private final CurrentApplicationExceptionHandler currentApplicationExceptionHandler;

    private final HashMap<String, BigDecimal> userBalances = new HashMap<String, BigDecimal>();

    @Autowired
    public TransactionService(EventService eventService, CurrentApplicationExceptionHandler currentApplicationExceptionHandler) {
        this.eventService = eventService;
        this.currentApplicationExceptionHandler = currentApplicationExceptionHandler;
    }

    public TransactionDTO generateTransactionDTO(Request request) throws BadRequestException {
        try {
            BigDecimal amount;
            String debitOrCredit;
            String eventType;
            TransactionDTO transactionDTO = null;
            amount = new BigDecimal(request.getTransactionAmount().getAmount().trim());
            if ( (request instanceof LoadRequest && request.getTransactionAmount().getDebitOrCredit().toUpperCase().equals(DebitCredit.DEBIT.toString()))
            || (request instanceof AuthorizationRequest && request.getTransactionAmount().getDebitOrCredit().toUpperCase().equals(DebitCredit.CREDIT.toString())) ) {
                throw new BadRequestException("Invalid request format! Check debitOrCredit!");
            }
            debitOrCredit = request.getTransactionAmount().getDebitOrCredit().trim();
            String userID = request.getUserId().trim();
            String messageID = request.getMessageId().trim();
            String currency = request.getTransactionAmount().getCurrency().trim();
            if(request instanceof LoadRequest) {
                eventType = EventType.LOAD.toString();
            } else {
                eventType = EventType.AUTHORIZATION.toString();
            }
            transactionDTO = new TransactionDTO(
                    userID,
                    messageID,
                    eventType,
                    amount,
                    currency,
                    debitOrCredit
            );
            return transactionDTO;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid amount format!");
        } catch (ValidationException e) {
            throw new BadRequestException("Invalid request format!");
        }
    }

    @EventListener
    @Async
    public void authorizationEventHandler(AuthorizationEvent authorizationEvent) throws BadRequestException{
        String userId = authorizationEvent.getUserId();
        String messageId = authorizationEvent.getMessageId();
        BigDecimal amount = authorizationEvent.getAmount();
        if (!userBalances.containsKey(userId)) {
            System.out.println("User not found!");
        } else {
            BigDecimal currentBalance = userBalances.get(userId);
            if (currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) > 0) {
                currentBalance = currentBalance.subtract(amount);
                userBalances.put(userId, currentBalance);
            }
            userBalances.put(userId, currentBalance);
        }
    }

    @EventListener
    @Async
    public void loadEventHandler(LoadEvent loadEvent) throws BadRequestException{
        String userId = loadEvent.getUserId();
        String messageId = loadEvent.getMessageId();
        BigDecimal amount = loadEvent.getAmount();
        BigDecimal currentBalance;
        if (userBalances.containsKey(userId)) {
            currentBalance = userBalances.get(userId);
            currentBalance = currentBalance.add(amount);
        } else {
            currentBalance = amount;
        }
        userBalances.put(userId, currentBalance);
    }

    public Response processTransaction(TransactionDTO transactionDTO) throws BadRequestException {
        String userId = transactionDTO.getUserID();
        String messageId = transactionDTO.getMessageID();
        BigDecimal amount = transactionDTO.getAmount();
        BigDecimal currentBalance;

        if(transactionDTO.getEventType().equals("AUTHORIZATION")) {
            if(!userBalances.containsKey(userId)) {
                Event authorizationEvent = eventService.createEvent(transactionDTO);
                eventService.pushEvent(authorizationEvent);
                throw new BadRequestException("User not found!");
            } else {
                currentBalance = userBalances.get(userId);
                String responseCode;
                if (currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) > 0) {
                    currentBalance = currentBalance.subtract(amount);
                    responseCode = ResponseCode.APPROVED.toString();
                } else {
                    responseCode = ResponseCode.DECLINED.toString();
                }
                return new AuthorizationResponse(userId, messageId, responseCode, String.valueOf(currentBalance));
            }
        } else {
            if(userBalances.containsKey(userId)) {
                currentBalance = userBalances.get(userId);
                currentBalance = currentBalance.add(amount);
            } else {
                currentBalance = amount;
            }
            userBalances.put(userId, currentBalance);
            return new LoadResponse(userId, messageId, String.valueOf(currentBalance));
        }
    }
    public Response transactionHandler(TransactionDTO transactionDTO) throws BadRequestException {
        Event event = eventService.createEvent(transactionDTO);
        Response response = processTransaction(transactionDTO);
        eventService.pushEvent(event);
        return response;
    }

}
