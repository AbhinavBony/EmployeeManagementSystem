package com.springbootapplication.employeemanagementsystem.repository;

import com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO;
import com.springbootapplication.employeemanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    boolean existsByDepartmentIgnoreCase(String department);
    boolean existsByDepartmentIgnoreCaseAndIdNot(String department, Integer id);
    @Query("SELECT new com.springbootapplication.employeemanagementsystem.dto.departmentDto.DepartmentEmployeeCountDTO(" +
            "d.id, d.department, COUNT(e)) " +
            "FROM Department d LEFT JOIN d.employees e " +
            "GROUP BY d.id, d.department")
    List<DepartmentEmployeeCountDTO> countEmployeeByDepartment();
}
