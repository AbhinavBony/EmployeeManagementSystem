package com.springbootapplication.employeemanagementsystem.services;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeResponseDTO;
import com.springbootapplication.employeemanagementsystem.dto.employeeDto.EmployeeUpdateRequestDTO;
import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateEmployeeFound;
import com.springbootapplication.employeemanagementsystem.exceptions.EmployeeNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class EmployeeServiceImplTest {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private DepartmentServiceImpl departmentService;

    private DepartmentResponseDTO departmentResponseDTO;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        departmentResponseDTO = departmentService.addDepartment(new DepartmentRequestDTO("Information Technology", "Noida"));
        requestDTO = new EmployeeRequestDTO();
        requestDTO.setEmployeeId(776109L);
        requestDTO.setEmployeeName("Abhinav Srivastava");
        requestDTO.setEmployeeSalary(45188.78);


    }
    @Test
    void addEmployee_shouldSaveAndReturn_withoutDuplicateEmployee(){
        var saved = employeeService.addEmployeeDetails(requestDTO);

        assertNotNull(saved);
        assertEquals(requestDTO.getEmployeeId(), saved.getEmployeeId());
        assertEquals(requestDTO.getEmployeeName(), saved.getEmployeeName());
    }
    @Test
    void addEmployee_shouldThrowException_whenDuplicateEmployeeExists(){
        employeeService.addEmployeeDetails(requestDTO);

        assertThrows(DuplicateEmployeeFound.class, ()-> employeeService.addEmployeeDetails(requestDTO));
    }
    @Test
    void addEmployeeToDepartment_shouldSaveAndReturn_whenEmployeeAndDepartmentExists(){
        var savedEmployee = employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId());

        assertNotNull(savedEmployee);
        assertEquals(requestDTO.getEmployeeName(), savedEmployee.getEmployeeName());
        assertEquals(departmentResponseDTO.getId(), savedEmployee.getDepartment().getId());
        assertEquals(departmentResponseDTO.getDepartment(), savedEmployee.getDepartment().getDepartment());
        assertEquals(requestDTO.getEmployeeId(), savedEmployee.getEmployeeId());
    }
    @Test
    void addEmployeeToDepartment_shouldThrowException_whenDuplicateEmployeeExists(){
        employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId());
        assertThrows(DuplicateEmployeeFound.class, ()-> employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId()));
    }
    @Test
    void addEmployeeToDepartment_shouldThrowException_whenDepartmentIdNotFound(){
        assertThrows(DepartmentNotFound.class, ()-> employeeService.addEmployeeToDepartment(requestDTO, 9999));
    }
    @Test
    void getAllEmployee_shouldReturnEmployeesList(){
        employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId());
        var emp2 = new EmployeeRequestDTO();
        emp2.setEmployeeId(987811L);
        emp2.setEmployeeName("Riya Surbhi");
        emp2.setEmployeeSalary(97898.48);
        var saved = employeeService.addEmployeeToDepartment(emp2, departmentResponseDTO.getId());

        List<EmployeeResponseDTO> list = employeeService.getAllEmployees();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(name-> name.getEmployeeName().equals(emp2.getEmployeeName())));
        assertTrue(list.stream().anyMatch(name-> name.getEmployeeId().equals(emp2.getEmployeeId())));
    }
    @Test
    void getAllEmployee_whenListIsEmpty(){
        List<EmployeeResponseDTO> list = employeeService.getAllEmployees();

        assertTrue(list.isEmpty());
    }
    @Test
    void getEmployeeByDepartmentId_shouldReturnEmployeeList_whenDepartmentIdExists(){
        employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId());
        var emp2 = new EmployeeRequestDTO();
        emp2.setEmployeeId(987811L);
        emp2.setEmployeeName("Riya Surabhi");
        emp2.setEmployeeSalary(97898.48);
        var saved = employeeService.addEmployeeToDepartment(emp2, departmentResponseDTO.getId());

        List<EmployeeResponseDTO> list = employeeService.getEmployeesByDepartment(departmentResponseDTO.getId());

        assertEquals(2, list.size());
        assertEquals(requestDTO.getEmployeeName(), list.getFirst().getEmployeeName());
        assertEquals(emp2.getEmployeeName(), list.getLast().getEmployeeName());
    }
    @Test
    void getEmployeeByDepartmentId_shouldThrowException_whenDepartmentNotFound(){
        assertThrows(DepartmentNotFound.class, ()-> employeeService.getEmployeesByDepartment(999));
    }
    @Test
    void transferEmployeeToDifferentDepartment_whenEmployeeAndDepartmentExists(){
        var savedEmployee = employeeService.addEmployeeToDepartment(requestDTO, departmentResponseDTO.getId());
        departmentResponseDTO = departmentService.addDepartment(new DepartmentRequestDTO("Digital Marketing", "Bangalore"));
        var transferEmployee = employeeService.transferEmployeeDepartment(savedEmployee.getEmployeeId(), departmentResponseDTO.getId());

        assertNotNull(transferEmployee);
        assertEquals(departmentResponseDTO.getDepartment(), transferEmployee.getDepartment().getDepartment());
        assertEquals(departmentResponseDTO.getId(), transferEmployee.getDepartment().getId());
        assertEquals(departmentResponseDTO.getLocation(), transferEmployee.getDepartment().getLocation());
        assertEquals(savedEmployee.getEmployeeName(), transferEmployee.getEmployeeName());
        assertEquals(savedEmployee.getEmployeeId(), transferEmployee.getEmployeeId());
    }
    @Test
    void updateEmployee_shouldUpdateAndReturn_whenEmployeeIdExists(){
        var saved = employeeService.addEmployeeDetails(requestDTO);
        var updateDetails = new EmployeeUpdateRequestDTO();
        updateDetails.setEmployeeName("Abhinav Kumar Srivastava");
        updateDetails.setEmployeeSalary(55897.55);

        var updatedEmployee = employeeService.updateEmployeeDetails(saved.getEmployeeId(), updateDetails);

        assertNotNull(updatedEmployee);
        assertEquals(updateDetails.getEmployeeName(), updatedEmployee.getEmployeeName());
        assertEquals(updateDetails.getEmployeeSalary(), updatedEmployee.getEmployeeSalary());
    }
    @Test
    void updateEmployee_shouldThrowException_whenEmployeeNotFound(){
        var updateDetails = new EmployeeUpdateRequestDTO();
        assertThrows(EmployeeNotFound.class, ()-> employeeService.updateEmployeeDetails(99999L, updateDetails));
    }
    @Test
    void deleteEmployeeDetails_whenEmployeeIdExists(){
        var saved = employeeService.addEmployeeDetails(requestDTO);
        employeeService.deleteEmployeeDetails(saved.getEmployeeId());

        assertThrows(EmployeeNotFound.class, ()-> employeeService.getEmployeeById(saved.getEmployeeId()));
    }
    @Test
    void getEmployeeById_shouldReturnEmployee_whenIdExists(){
        var saved = employeeService.addEmployeeDetails(requestDTO);
        var found = employeeService.getEmployeeById(saved.getEmployeeId());

        assertNotNull(found);
        assertEquals(saved.getEmployeeId(), found.getEmployeeId());
        assertEquals(saved.getEmployeeName(), found.getEmployeeName());
    }
    @Test
    void getEmployeeById_shouldThrowException_whenIdNotExists(){
        assertThrows(EmployeeNotFound.class, ()-> employeeService.getEmployeeById(9999L));
    }
}