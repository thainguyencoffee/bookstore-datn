package com.bookstore.backend.core.exception;

import com.bookstore.backend.core.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomNoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoResultException(CustomNoResultException e) {
        ApiError.ErrorInfo errorInfo = new ApiError.ErrorInfo();
        errorInfo.setMessage(e.getMessage());
        errorInfo.setEntity(e.getClazz().getSimpleName());
        return new ApiError(List.of(errorInfo));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<ApiError.ErrorInfo> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = ((FieldError) error);
            errors.add(new ApiError.ErrorInfo(error.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), error.getDefaultMessage()));
        });
        return new ResponseEntity<>(new ApiError(errors), HttpStatus.BAD_REQUEST);
    }

}