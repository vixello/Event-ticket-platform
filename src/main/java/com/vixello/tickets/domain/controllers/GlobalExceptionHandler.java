package com.vixello.tickets.domain.controllers;

import com.vixello.tickets.domain.dtos.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream().findFirst().map(error ->
                error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error occurred");

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errorMessage);

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Caught ConstraintViolationException", ex);

        String errorMessage = ex.getConstraintViolations().stream().findFirst()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .orElse("Constraint violation occurred");

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errorMessage);

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught exception", ex);

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unknown error occurred");

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
