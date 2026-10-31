package com.springbootapplication.employeemanagementsystem.dto.departmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEmployeeCountDTO {
    private Integer departmentId;
    private String departmentName;
    private Long employeeCount;
}
