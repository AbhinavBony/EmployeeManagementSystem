package com.springbootapplication.employeemanagementsystem.interfaces;

import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeResponseDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeUpdateRequestDTO;

import java.util.List;

public interface EmployeeServices {
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO addEmployeeDetails(EmployeeRequestDTO dto);
    EmployeeResponseDTO updateEmployeeDetails(Long employeeId, EmployeeUpdateRequestDTO updateDto);
    void deleteEmployeeDetails(Long employeeId);
    EmployeeResponseDTO getEmployeeById(Long employeeId);
    EmployeeResponseDTO addEmployeeToDepartment(EmployeeRequestDTO dto, Integer depId);
    List<EmployeeResponseDTO> getEmployeesByDepartment(Integer depId);
    EmployeeResponseDTO transferEmployeeDepartment(Long employeeId, Integer updateDepId);
}
