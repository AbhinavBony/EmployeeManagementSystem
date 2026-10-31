package com.springbootapplication.employeemanagementsystem.repository;

import com.springbootapplication.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(Long employeeId);
    @EntityGraph(attributePaths = "department")
    List<Employee> findByDepartment_Id(Integer depId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.department")
    List<Employee> findAllWithDepartment();
}
