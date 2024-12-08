package com.denizkpln.user_service.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(exception.getMessage())
                .errorCode(exception.getErrorCode())
                .build(),
                HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        //return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(exception.getMessage())
                .errorCode("400")
                .build(),
                HttpStatus.valueOf(String.valueOf(HttpStatus.BAD_REQUEST)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @NotNull HttpHeaders httpHeaders,
                                                                  @NotNull HttpStatusCode statusCode,
                                                                  @NotNull WebRequest webRequest) {

        Map<String, String> errors = new HashMap<>();
        StringBuilder message=new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.append(fieldName+ " " +errorMessage+" ");
            errors.put(fieldName, errorMessage);

        });

        //return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(String.valueOf(message))
                .errorCode("400")
                .build(), HttpStatus.BAD_REQUEST);
    }
}