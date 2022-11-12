package com.increff.Assure.controller;

import com.increff.Assure.model.data.MessageData;
import com.increff.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {


    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        e.printStackTrace();
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable e) {
        MessageData data = new MessageData();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        e.printStackTrace();
        return data;
    }

    @ResponseStatus(HttpStatus.CREATED)
    public MessageData handle() {
        MessageData data = new MessageData();
        data.setMessage("Successfully Added");
        return data;
    }

}

