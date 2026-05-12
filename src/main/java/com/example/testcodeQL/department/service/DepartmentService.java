// Importing packages
package com.example.testcodeQL.department.service;// Java Program to Illustrate DepartmentService.java File

// Importing required classes
import com.masoodali09.springbootdemoproject.department.entity.Department;

import java.util.List;

// Class
public interface DepartmentService {

    // Save operation
    Department saveDepartment(Department department);

    // Read operation
    List<Department> fetchDepartmentList();

    // Update operation
    Department updateDepartment(Department department,
                                Long departmentId);

    // Delete operation
    void deleteDepartmentById(Long departmentId);
}