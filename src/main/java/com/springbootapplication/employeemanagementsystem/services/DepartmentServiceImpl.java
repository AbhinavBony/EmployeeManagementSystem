package com.springbootapplication.employeemanagementsystem.services;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentRequestDTO;
import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentResponseDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import com.springbootapplication.employeemanagementsystem.exceptions.DepartmentNotFound;
import com.springbootapplication.employeemanagementsystem.exceptions.DuplicateDepartmentFound;
import com.springbootapplication.employeemanagementsystem.interfaces.DepartmentServices;
import com.springbootapplication.employeemanagementsystem.repository.DepartmentRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentServices {
    private final DepartmentRepo repo;
    private final ModelMapper mapper;

    public DepartmentServiceImpl(DepartmentRepo repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        log.info("Fetching All Departments");
        List<DepartmentResponseDTO> list = repo.findAll().stream().map(department -> this.mapper.map(department, DepartmentResponseDTO.class)).toList();
        log.debug("Fetched {} departments(s)", list.size());
        return list;
    }

    @Override
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto) {
        String name = dto.getDepartment().trim();
        String location = dto.getLocation().trim();
        log.info("Attempting to add department: {}", name);

        if (repo.existsByDepartmentIgnoreCase(name)){
            log.warn("duplicate department creation attempts: {}", name);
            throw new DuplicateDepartmentFound("Department already exists with department name: "+ name);
        }

        Department department = this.mapper.map(dto, Department.class);
        department.setDepartment(name);
        department.setLocation(location);
        Department saved = this.repo.save(department);
        log.info("Department created successfully {}", saved.getId());
        return this.mapper.map(saved, DepartmentResponseDTO.class);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Integer Id, DepartmentRequestDTO dto) {
        log.info("Attempting to update department: {}", Id);
        Department exists = repo.findById(Id).orElseThrow(()-> {
            log.warn("Update failed - department id: {}", Id);
            return new DepartmentNotFound("Department not found with Id: "+ Id);
        });
        String name = dto.getDepartment().trim();
        String location = dto.getLocation().trim();
        if (repo.existsByDepartmentIgnoreCaseAndIdNot(name, Id)){
            log.warn("Update Failed- duplicate department name: {}", name);
            throw new DuplicateDepartmentFound("Department already exists with name: "+ name);
        }
        this.mapper.map(dto, exists);
        exists.setDepartment(name);
        exists.setLocation(location);
        Department updated = this.repo.save(exists);
        log.info("Department id {} updated successfully", updated.getId());
        return this.mapper.map(updated, DepartmentResponseDTO.class);
    }

    @Override
    public void deleteDepartment(Integer Id) {
        log.info("Attempting to delete department by Id: {}", Id);
        if (!repo.existsById(Id)){
            log.warn("Deletion failed - Department not found with Id:{}", Id);
            throw new DepartmentNotFound("Department not found with Id: "+ Id);
        }
        repo.deleteById(Id);
        log.info("Deletion successful with department Id: {}", Id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getById(Integer Id) {
        log.debug("Fetching department by Id: {}", Id);
        Department exists = repo.findById(Id).orElseThrow(()-> {
            log.warn("Fetching failed - Department not found with Id: {}", Id);
            return new DepartmentNotFound("Department not found with Id: "+ Id);
        });
        return this.mapper.map(exists, DepartmentResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentEmployeeCountDTO> getCountOfEmployeesByDepartment() {
        return repo.countEmployeeByDepartment();
    }
}
