package com.example.SmartCart.common.Handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ApiError(
        HttpStatus status,
        String message,
        Instant timeStamp,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<ApiErrorField> errors
) {
    public ApiError(HttpStatus status,String message){
        this(status,message,Instant.now(),null);

    }

    public ApiError(HttpStatus status,String message,List<ApiErrorField> errors){
        this(status,message,Instant.now(),errors);
    }

}


