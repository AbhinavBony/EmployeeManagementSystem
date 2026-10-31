package com.springbootapplication.employeemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;
    @Column(name = "employee_name", nullable = false)
    private String employeeName;
    @Column(name = "employee_salary", nullable = false)
    private Double employeeSalary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
