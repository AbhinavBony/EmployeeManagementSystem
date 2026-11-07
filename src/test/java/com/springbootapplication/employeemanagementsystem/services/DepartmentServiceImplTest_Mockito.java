package com.springbootapplication.employeemanagementsystem.services;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateDepartmentFound;
import com.springbootapplication.employeemanagementsystem.repository.DepartmentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest_Mockito {
    @Mock
    private DepartmentRepo depRepo;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    DepartmentServiceImpl departmentService;

    Department department;
    DepartmentRequestDTO requestDTO;
    DepartmentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1);
        department.setDepartment("Information Technology");
        department.setLocation("Noida");

        requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartment("Information Technology");
        requestDTO.setLocation("Noida");

        responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(1);
        responseDTO.setDepartment("Information Technology");
        responseDTO.setLocation("Noida");
    }
    @Test
    void addDepartment_shouldSaveAndReturn(){
        when(depRepo.existsByDepartmentIgnoreCase(requestDTO.getDepartment())).thenReturn(false);
        when(mapper.map(requestDTO, Department.class)).thenReturn(department);
        when(depRepo.save(department)).thenReturn(department);
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO saved = departmentService.addDepartment(requestDTO);

        assertNotNull(saved);
        assertEquals(responseDTO.getId(), saved.getId());
        assertEquals(responseDTO.getDepartment(), saved.getDepartment());
        assertEquals(responseDTO.getLocation(), saved.getLocation());
        verify(depRepo, times(1)).save(department);
    }
    @Test
    void addDepartment_shouldThrowException_whenDuplicateDepartmentExists(){
        when(depRepo.existsByDepartmentIgnoreCase(requestDTO.getDepartment())).thenReturn(true);

        assertThrows(DuplicateDepartmentFound.class, ()-> departmentService.addDepartment(requestDTO));
        verify(depRepo, never()).save(any());
    }
    @Test
    void getAllDepartments_shouldReturnList(){
        when(depRepo.findAll()).thenReturn(List.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();

        assertEquals(1, list.size());
        assertTrue(list.stream().anyMatch(name-> name.getDepartment().equals(responseDTO.getDepartment())));
        assertTrue(list.stream().anyMatch(name-> name.getLocation().equals(responseDTO.getLocation())));

        verify(depRepo, times(1)).findAll();
    }
    @Test
    void getAllDepartments_whenListIsEmpty(){
        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();

        assertTrue(list.isEmpty());
    }
    @Test
    void updateDepartment_shouldReturn_whenDepartmentExists(){
        when(depRepo.findById(department.getId())).thenReturn(Optional.of(department));
        when(depRepo.existsByDepartmentIgnoreCaseAndIdNot(requestDTO.getDepartment(), department.getId())).thenReturn(false);
        doNothing().when(mapper).map(requestDTO, department);
        when(depRepo.save(department)).thenReturn(department);
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO updated = departmentService.updateDepartment(department.getId(), requestDTO);

        assertNotNull(updated);
        assertEquals(requestDTO.getDepartment(), department.getDepartment());
        assertEquals(responseDTO.getLocation(), department.getLocation());
        verify(mapper, times(1)).map(requestDTO, department);
        verify(depRepo, times(1)).save(department);
    }
    @Test
    void updateDepartment_shouldThrow_whenNameBelongsToAnotherDepartment(){
        when(depRepo.findById(department.getId())).thenReturn(Optional.of(department));
        when(depRepo.existsByDepartmentIgnoreCaseAndIdNot(requestDTO.getDepartment(), department.getId())).thenReturn(true);

        assertThrows(DuplicateDepartmentFound.class, ()-> departmentService.updateDepartment(department.getId(), requestDTO));
        verify(mapper, never()).map(any(), any(Department.class));
        verify(depRepo, never()).save(any());
    }
    @Test
    void updateDepartment_shouldThrow_whenDepartmentNotFound(){
        assertThrows(DepartmentNotFound.class, ()-> departmentService.updateDepartment(999, requestDTO));
        verify(depRepo, never()).existsByDepartmentIgnoreCaseAndIdNot(any(), any());
        verify(depRepo, never()).save(any());
    }
    @Test
    void deleteDepartment_shouldDelete_whenIdExists(){
        when(depRepo.existsById(department.getId())).thenReturn(true);
        departmentService.deleteDepartment(department.getId());

        assertThrows(DepartmentNotFound.class, ()-> departmentService.getById(department.getId()));
        verify(depRepo, times(1)).deleteById(department.getId());
    }
    @Test
    void deleteDepartment_shouldThrow_whenIdNotFound(){
        assertThrows(DepartmentNotFound.class, ()-> departmentService.deleteDepartment(999));
        verify(depRepo, never()).deleteById(any());
    }
    @Test
    void getById_shouldReturn_whenFound(){
        when(depRepo.findById(department.getId())).thenReturn(Optional.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO found = departmentService.getById(department.getId());

        assertNotNull(found);
        assertEquals(responseDTO.getDepartment(), found.getDepartment());
        verify(depRepo, times(1)).findById(department.getId());
    }
    @Test
    void getById_shouldThrow_whenNotFound(){
        when(depRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFound.class, ()-> departmentService.getById(999));
        verify(mapper, never()).map(any(Department.class), any(DepartmentResponseDTO.class));
    }
}
