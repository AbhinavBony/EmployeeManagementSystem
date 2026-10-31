package com.springbootapplication.employeemanagementsystem.dto.employeeDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequestDTO {
    @NotBlank(message="employee name can't be null / blank!!")
    private String employeeName;
    @Positive(message = "employee salary can't be negative / zero!!")
    private Double employeeSalary;
}
