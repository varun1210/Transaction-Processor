package com.transactionprocessor.utils.exceptions;

import com.transactionprocessor.utils.responses.ResponseCode;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CurrentApplicationExceptionHandler {

    public Error errorGenerator(Exception exception) {

        if(exception instanceof BadRequestException) {
            return new Error(exception.getMessage(), ResponseCode.DECLINED.toString());
        } else if (exception instanceof ValidationException){
            return new Error("Invalid request format!", ResponseCode.DECLINED.toString());
        } else if(exception instanceof NoResourceFoundException) {
            return new Error("No resource found!", ResponseCode.DECLINED.toString());
        } else {
            return new Error("Server error!", ResponseCode.DECLINED.toString());
        }
    }

    @ExceptionHandler
    public ResponseEntity<Error> exceptionHandler(Exception e) {
        System.out.println(e.toString());
        Error error = errorGenerator(e);
        HttpStatus httpStatusCode;
        if (e instanceof BadRequestException) {
            httpStatusCode = HttpStatus.BAD_REQUEST;
        } else if (e instanceof NoResourceFoundException) {
            httpStatusCode = HttpStatus.NOT_FOUND;
        } else {
            httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ResponseEntity<Error> errorResponse = new ResponseEntity<Error>(error, httpStatusCode);
        return errorResponse;
    }

}
