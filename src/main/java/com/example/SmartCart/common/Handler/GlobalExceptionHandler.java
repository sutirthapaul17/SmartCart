package com.example.SmartCart.common.Handler;


import com.example.SmartCart.common.Exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,ex.getMessage());
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage());
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ApiError> handleInvalidJwt(InvalidJwtException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage());
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInputValidationError(MethodArgumentNotValidException ex){
        List<ApiErrorField> errors= ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        new ApiErrorField(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                ).toList();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"Input Validation Failed",errors);
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.status()).body(apiError);
    }


//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex){
//        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"Username not found with username: "+ex.getMessage());
//        log.error(apiError.toString(),ex);
//        return ResponseEntity.status(apiError.status()).body(apiError);
//    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        HttpStatus.CONFLICT,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        HttpStatus.UNAUTHORIZED,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError(
                        HttpStatus.FORBIDDEN,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(
                        HttpStatus.BAD_REQUEST,
                        ex.getMostSpecificCause().getMessage()
                ));
    }

//    private ApiErrorField mapToApiErrorField(FieldError fieldError) {
//
//        return new ApiErrorField(
//                fieldError.getField(),
//                fieldError.getDefaultMessage()
//        );
//    }

}
