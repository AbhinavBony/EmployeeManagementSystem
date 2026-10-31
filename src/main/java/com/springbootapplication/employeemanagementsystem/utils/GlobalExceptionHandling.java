package com.springbootapplication.employeemanagementsystem.utils;

import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateDepartmentFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateEmployeeFound;
import com.springbootapplication.employeemanagementsystem.exceptions.EmployeeNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(DuplicateEmployeeFound.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateEmployeeFound e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(EmployeeNotFound.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(DuplicateDepartmentFound.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDepartment(DuplicateDepartmentFound e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(DepartmentNotFound.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        response.put("statusCode", HttpStatus.BAD_REQUEST);
        response.put("message", errors);
        response.put("timeStamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now()));
    }
}
