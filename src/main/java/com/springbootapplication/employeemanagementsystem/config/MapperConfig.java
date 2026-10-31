package com.springbootapplication.employeemanagementsystem.config;

import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeRequestDTO;
import com.springbootapplication.employeemanagementsystem.entity.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper mapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(EmployeeRequestDTO.class, Employee.class).addMappings(m-> m.skip(Employee::setId));
        return mapper;
    }
}
