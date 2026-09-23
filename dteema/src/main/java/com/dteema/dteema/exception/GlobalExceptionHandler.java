package com.dteema.dteema.exception;


import com.dteema.dteema.dto.common.ErrorResponse;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException exception){
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();



        return ResponseEntity.badRequest().body(ErrorResponse.builder().error(String.join(",", errors)).status(HttpStatus.BAD_REQUEST).build());
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException exception){
        return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.builder().error(exception.getMessage()).status(exception.getStatus()).build());
    }
}
