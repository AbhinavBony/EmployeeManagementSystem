package com.springbootapplication.employeemanagementsystem.dto.departmentDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {
    @NotBlank(message = "Department can't be null / blank!!")
    private String department;
    @NotBlank(message = "Location can't be null / blank!!")
    private String location;
}
