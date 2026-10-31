package com.springbootapplication.employeemanagementsystem.dto.departmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO {
    private Integer id;
    private String department, location;
}
