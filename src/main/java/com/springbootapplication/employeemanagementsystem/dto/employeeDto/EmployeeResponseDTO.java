package com.springbootapplication.employeemanagementsystem.dto.employeeDto;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {
    private Integer id;
    private Long employeeId;
    private String employeeName;
    private Double employeeSalary;
    private DepartmentResponseDTO department;
}
