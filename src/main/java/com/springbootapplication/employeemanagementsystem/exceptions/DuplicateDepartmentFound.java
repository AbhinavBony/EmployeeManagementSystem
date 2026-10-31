package com.springbootapplication.employeemanagementsystem.exceptions;

public class DuplicateDepartmentFound extends RuntimeException {
    public DuplicateDepartmentFound(String message) {
        super(message);
    }
}
