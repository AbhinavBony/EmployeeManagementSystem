package com.springbootapplication.employeemanagementsystem.controller;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import com.springbootapplication.employeemanagementsystem.interfaces.DepartmentServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Department")
@Validated
public class DepartmentController {
    private final DepartmentServices service;

    public DepartmentController(DepartmentServices service) {
        this.service = service;
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<DepartmentResponseDTO>> getAll(){
        List<DepartmentResponseDTO> list = this.service.getAllDepartments();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping(value = "/")
    public ResponseEntity<DepartmentResponseDTO> add(@Valid @RequestBody DepartmentRequestDTO dto){
        DepartmentResponseDTO saved = this.service.addDepartment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping(value = "/{Id}")
    public ResponseEntity<DepartmentResponseDTO> update(@Positive(message = "Department id can't be negative / zero.") @PathVariable Integer Id, @Valid @RequestBody DepartmentRequestDTO dto){
        DepartmentResponseDTO updated = this.service.updateDepartment(Id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(value = "/{Id}")
    public ResponseEntity<?> delete(@Positive(message = "Department id can't be negative / zero.") @PathVariable Integer Id){
        this.service.deleteDepartment(Id);
        return ResponseEntity.ok("Department Deleted Successfully!!");
    }
    @GetMapping(value ="/{Id}")
    public ResponseEntity<DepartmentResponseDTO> getById(@Positive(message = "Department id can't be negative / zero.")@PathVariable Integer Id){
        DepartmentResponseDTO found = this.service.getById(Id);
        return ResponseEntity.ok(found);
    }
    @GetMapping(value = "/employee-count")
    public ResponseEntity<List<DepartmentEmployeeCountDTO>> getCountOfEmployees(){
        List<DepartmentEmployeeCountDTO> listOfCount = this.service.getCountOfEmployeesByDepartment();
        return ResponseEntity.ok(listOfCount);
    }
}
