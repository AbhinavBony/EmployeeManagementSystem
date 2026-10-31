package com.springbootapplication.employeemanagementsystem.controller;

import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeResponseDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeUpdateRequestDTO;
import com.springbootapplication.employeemanagementsystem.interfaces.EmployeeServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/Employee")
@Validated
public class EmployeeController {
    private final EmployeeServices service;

    public EmployeeController(EmployeeServices service) {
        this.service = service;
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll(){
        List<EmployeeResponseDTO> list = this.service.getAllEmployees();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = "/department/{depId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployeesByDep(@Positive(message = "Department id can't be zero / negative")@PathVariable Integer depId){
        return ResponseEntity.ok(this.service.getEmployeesByDepartment(depId));
    }
    @PostMapping(value = "/")
    public ResponseEntity<EmployeeResponseDTO> add(@Valid @RequestBody EmployeeRequestDTO dto){
        EmployeeResponseDTO saved = this.service.addEmployeeDetails(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PostMapping(value = "/department/{depId}")
    public ResponseEntity<EmployeeResponseDTO> addEmpToDep(@Valid @RequestBody EmployeeRequestDTO dto, @Positive(message = "Department id can't be negative / zero!!") @PathVariable Integer depId){
        EmployeeResponseDTO savedToDep = this.service.addEmployeeToDepartment(dto, depId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToDep);
    }
    @PutMapping(value = "/{employeeId}/transfer/{depId}")
    public ResponseEntity<EmployeeResponseDTO> transferEmployeeDpe(@Positive(message = "employee id can't be negative / zero!!") @PathVariable Long employeeId, @Positive(message = "department id can't be negative / zero!!")@PathVariable Integer depId){
        EmployeeResponseDTO transferredEmployee = this.service.transferEmployeeDepartment(employeeId, depId);
        return ResponseEntity.ok(transferredEmployee);
    }
    @PutMapping(value = "/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> update(@Positive(message = "employee id can't be negative / zero!!") @PathVariable Long employeeId, @Valid @RequestBody EmployeeUpdateRequestDTO dto){
        EmployeeResponseDTO updated = this.service.updateEmployeeDetails(employeeId, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(value = "/{employeeId}")
    public ResponseEntity<?> delete(@Positive(message = "employee id can't be negative / zero!!") @PathVariable Long employeeId){
        this.service.deleteEmployeeDetails(employeeId);
        return ResponseEntity.ok("Employee Details Deleted Successfully!!!");
    }
    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getById(@Positive(message = "employee id can't be negative / zero!!") @PathVariable Long employeeId){
        EmployeeResponseDTO foundById = this.service.getEmployeeById(employeeId);
        return ResponseEntity.status(HttpStatus.FOUND).body(foundById);
    }
}
