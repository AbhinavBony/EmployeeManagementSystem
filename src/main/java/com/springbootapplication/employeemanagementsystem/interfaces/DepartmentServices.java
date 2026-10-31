package com.springbootapplication.employeemanagementsystem.interfaces;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentServices {
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto);
    DepartmentResponseDTO updateDepartment(Integer Id, DepartmentRequestDTO dto);
    void deleteDepartment(Integer Id);
    DepartmentResponseDTO getById(Integer Id);
    List<DepartmentEmployeeCountDTO> getCountOfEmployeesByDepartment();
}
