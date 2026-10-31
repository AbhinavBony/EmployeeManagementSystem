package com.springbootapplication.employeemanagementsystem.services;

import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeResponseDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeUpdateRequestDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import com.springbootapplication.employeemanagementsystem.entity.Employee;
import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateEmployeeFound;
import com.springbootapplication.employeemanagementsystem.exceptions.EmployeeNotFound;
import com.springbootapplication.employeemanagementsystem.interfaces.EmployeeServices;
import com.springbootapplication.employeemanagementsystem.repository.DepartmentRepo;
import com.springbootapplication.employeemanagementsystem.repository.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeServices {
    private final EmployeeRepo repo;
    private final ModelMapper mapper;
    private final DepartmentRepo depRepo;

    public EmployeeServiceImpl(EmployeeRepo repo, ModelMapper mapper, DepartmentRepo depRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.depRepo = depRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return repo.findAllWithDepartment().stream().map(employee-> this.mapper.map(employee, EmployeeResponseDTO.class)).toList();
    }

    @Override
    public EmployeeResponseDTO addEmployeeDetails(EmployeeRequestDTO dto) {
        repo.findByEmployeeId(dto.getEmployeeId()).ifPresent((employee -> {
            throw new DuplicateEmployeeFound("Employee already exists with employee Id: "+ dto.getEmployeeId());
        }));
        Employee employee = this.mapper.map(dto, Employee.class);
        Employee saved = this.repo.save(employee);
        return this.mapper.map(saved, EmployeeResponseDTO.class);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO addEmployeeToDepartment(EmployeeRequestDTO dto, Integer depId) {
        repo.findByEmployeeId(dto.getEmployeeId()).ifPresent((employee)-> {
            throw new DuplicateEmployeeFound("Employee already exists with employee id: "+ dto.getEmployeeId());
        });
        Department department = depRepo.findById(depId).orElseThrow(()-> new DepartmentNotFound("Department not found with department id: "+ depId));
        Employee employee = this.mapper.map(dto, Employee.class);
        employee.setDepartment(department);
        Employee saved = this.repo.save(employee);
        return this.mapper.map(saved, EmployeeResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Integer depId) {
        if (!depRepo.existsById(depId)){
            throw new DepartmentNotFound("Department not found with department id: "+ depId);
        }
        return repo.findByDepartment_Id(depId).stream().map(employee -> this.mapper.map(employee, EmployeeResponseDTO.class)).toList();
    }

    @Override
    public EmployeeResponseDTO transferEmployeeDepartment(Long employeeId, Integer updateDepId) {
        Employee employee = repo.findByEmployeeId(employeeId).orElseThrow(()-> new EmployeeNotFound("Employee not found with employee id: "+ employeeId));
        Department updateDepartment = depRepo.findById(updateDepId).orElseThrow(()-> new DepartmentNotFound("Department not found with department id: "+ updateDepId));
        employee.setDepartment(updateDepartment);
        Employee updated = repo.save(employee);
        return this.mapper.map(updated, EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeResponseDTO updateEmployeeDetails(Long employeeId, EmployeeUpdateRequestDTO updateDto) {
        Employee found = repo.findByEmployeeId(employeeId).orElseThrow(()-> new EmployeeNotFound("Employee not found with employee Id: "+ employeeId));
        this.mapper.map(updateDto, found);
        Employee updated = repo.save(found);
        return this.mapper.map(updated, EmployeeResponseDTO.class);
    }

    @Override
    public void deleteEmployeeDetails(Long employeeId) {
        Employee exists = repo.findByEmployeeId(employeeId).orElseThrow(()-> new EmployeeNotFound("Employee not found with employee Id: "+ employeeId));
        this.repo.delete(exists);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long employeeId) {
        Employee found = repo.findByEmployeeId(employeeId).orElseThrow(()-> new EmployeeNotFound("Employee not found with employee Id: "+ employeeId));
        return this.mapper.map(found, EmployeeResponseDTO.class);
    }

}
