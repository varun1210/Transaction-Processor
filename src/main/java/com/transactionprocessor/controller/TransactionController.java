package com.transactionprocessor.controller;

import com.transactionprocessor.DTO.TransactionDTO;
import com.transactionprocessor.model.AuthorizationRequest;
import com.transactionprocessor.model.LoadRequest;
import com.transactionprocessor.model.Ping;
import com.transactionprocessor.service.TransactionService;
import com.transactionprocessor.utils.exceptions.CurrentApplicationExceptionHandler;
import com.transactionprocessor.utils.responses.AuthorizationResponse;
import com.transactionprocessor.utils.responses.LoadResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentApplicationExceptionHandler currentApplicationExceptionHandler;

    @Autowired
    public TransactionController(TransactionService transactionService, CurrentApplicationExceptionHandler currentApplicationExceptionHandler) {
        this.transactionService = transactionService;
        this.currentApplicationExceptionHandler = currentApplicationExceptionHandler;
    }

    @GetMapping(
            value = "/ping"
    )
    @ResponseBody
    public ResponseEntity ping() {
        try {
            return new ResponseEntity<Ping>(new Ping(), HttpStatus.OK);
        } catch (Exception e) {
            return currentApplicationExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping(
            value = "/authorization",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity authorizationTransaction(@RequestBody @Valid AuthorizationRequest authorizationRequest) {
        try {
            TransactionDTO transactionDTO = transactionService.generateTransactionDTO(authorizationRequest);
            AuthorizationResponse authorizationResponse = (AuthorizationResponse) transactionService.transactionHandler(transactionDTO);
            return new ResponseEntity<AuthorizationResponse>(authorizationResponse, HttpStatus.CREATED);
        } catch(Exception e) {
            return currentApplicationExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping(
            value = "/load",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity loadTransaction(@RequestBody @Valid LoadRequest loadRequest) {
        try {
            TransactionDTO transactionDTO = transactionService.generateTransactionDTO(loadRequest);
            LoadResponse loadResponse = (LoadResponse) transactionService.transactionHandler(transactionDTO);
            return new ResponseEntity<LoadResponse>(loadResponse, HttpStatus.CREATED);
        } catch(Exception e) {
            return currentApplicationExceptionHandler.exceptionHandler(e);
        }
    }

}
