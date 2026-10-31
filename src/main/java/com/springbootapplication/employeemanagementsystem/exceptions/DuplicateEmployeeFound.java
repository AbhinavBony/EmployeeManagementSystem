package com.springbootapplication.employeemanagementsystem.exceptions;

public class DuplicateEmployeeFound extends RuntimeException {
    public DuplicateEmployeeFound(String message) {
        super(message);
    }
}
