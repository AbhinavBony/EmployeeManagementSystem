package com.springbootapplication.employeemanagementsystem.services;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateDepartmentFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class DepartmentServiceImplTest {
    @Autowired
    private DepartmentServiceImpl service;

    DepartmentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartment("Information Technology");
        requestDTO.setLocation("Noida");
    }
    @Test
    void addDepartment_shouldSaveAndReturn(){
        DepartmentResponseDTO savedDepartment = service.addDepartment(requestDTO);

        assertNotNull(savedDepartment);
        assertEquals(requestDTO.getDepartment(), savedDepartment.getDepartment());
        assertEquals(requestDTO.getLocation(), savedDepartment.getLocation());
    }
    @Test
    void addDepartment_shouldThrowException_whenDuplicateDepartmentExists(){
        service.addDepartment(requestDTO);

        assertThrows(DuplicateDepartmentFound.class, ()-> service.addDepartment(requestDTO));
    }
    @Test
    void getAllDepartments_shouldReturnList_whenListIsNotEmpty(){
        service.addDepartment(requestDTO);

        var secondDepartment = new DepartmentRequestDTO();
        secondDepartment.setDepartment("Human Resource");
        secondDepartment.setLocation("Bangalore");

        DepartmentResponseDTO saved = service.addDepartment(secondDepartment);
        List<DepartmentResponseDTO> list = service.getAllDepartments();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(department-> department.getDepartment().equals(requestDTO.getDepartment())));
        assertTrue(list.stream().anyMatch(department-> department.getDepartment().equals(secondDepartment.getDepartment())));
    }
    @Test
    void getAllDepartments_whenListIsEmpty(){
        List<DepartmentResponseDTO> list = service.getAllDepartments();

        assertTrue(list.isEmpty());
    }
    @Test
    void updateDepartment_whenDepartmentExists(){
        var saved = service.addDepartment(requestDTO);
        DepartmentRequestDTO updateDepartment = new DepartmentRequestDTO();
        updateDepartment.setDepartment("DevOps Team");
        updateDepartment.setLocation("Bangalore");

        var updated = service.updateDepartment(saved.getId(), updateDepartment);

        assertNotNull(updated);
        assertEquals(saved.getId(), updated.getId());
        assertEquals(updateDepartment.getDepartment(), updated.getDepartment());
        assertNotEquals(saved.getDepartment(), updated.getDepartment());
        assertEquals(updateDepartment.getLocation(), updated.getLocation());
    }
    @Test
    void updateDepartment_shouldThrowException_whenDepartmentNotFound(){

        assertThrows(DepartmentNotFound.class, ()-> service.updateDepartment(999, requestDTO));
    }
    @Test
    void updateDepartment_shouldThrowException_whenNameMatchesAnotherDepartment(){
        service.addDepartment(requestDTO); // Information Technology.
        var hrDto = new DepartmentRequestDTO();
        hrDto.setDepartment("Human Resource");
        hrDto.setLocation("Bangalore");
        var hr = service.addDepartment(hrDto);

        var update = new DepartmentRequestDTO();
        update.setDepartment("information technology");
        update.setLocation("bangalore");

        assertThrows(DuplicateDepartmentFound.class, ()-> service.updateDepartment(hr.getId(), update));
    }
    @Test
    void deleteDepartment_whenDepartmentFound(){
        var saved = service.addDepartment(requestDTO);
        service.deleteDepartment(saved.getId());
        List<DepartmentResponseDTO> list = service.getAllDepartments();

        assertTrue(list.isEmpty());
    }
    @Test
    void deleteDepartment_shouldThrow_whenDepartmentNotFound(){

        assertThrows(DepartmentNotFound.class, ()-> service.deleteDepartment(9999));
    }
    @Test
    void getDepartmentById_shouldReturn_whenExists(){
        var saved = service.addDepartment(requestDTO);
        DepartmentResponseDTO found = service.getById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getDepartment(), found.getDepartment());
        assertEquals(saved.getLocation(), found.getLocation());
    }
    @Test
    void getDepartmentById_shouldThrowException_whenNotFound(){

        assertThrows(DepartmentNotFound.class, ()-> service.getById(999));
    }
    @Test
    void getCountOfEmployees_shouldReturnList(){
        service.addDepartment(requestDTO);
        var secondDepartment = new DepartmentRequestDTO();
        secondDepartment.setDepartment("Human Resource");
        secondDepartment.setLocation("Bangalore");

        DepartmentResponseDTO saved = service.addDepartment(secondDepartment);

        List<DepartmentEmployeeCountDTO> count = service.getCountOfEmployeesByDepartment();

        assertEquals(2, count.size());
    }
}