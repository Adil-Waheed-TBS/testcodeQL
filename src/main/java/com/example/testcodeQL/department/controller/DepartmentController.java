
// Importing packages modules
package com.example.testcodeQL.department.controller;// Java Program to Illustrate DepartmentController.java File


// Importing required classes

import com.masoodali09.springbootdemoproject.department.entity.Department;
import com.masoodali09.springbootdemoproject.department.service.DepartmentService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Annotation
@RestController
// Class
public class DepartmentController {

    @Autowired private DepartmentService departmentService;
//test

    // Save operation
    @PostMapping("/departments")

    public Department saveDepartment(
             @RequestBody Department department)
    {
        return departmentService.saveDepartment(department);
    }

    // Read operation
    @GetMapping("/departments")

    public List<Department> fetchDepartmentList()
    {
        return departmentService.fetchDepartmentList();
    }

    // Update operation
    @PutMapping("/departments/{id}")

    public Department
    updateDepartment(@RequestBody Department department,
                     @PathVariable("id") Long departmentId)
    {
        return departmentService.updateDepartment(
                department, departmentId);
    }

    @GetMapping("/twilio")



    // Delete operation
    @DeleteMapping("/departments/{id}")

    public String deleteDepartmentById(@PathVariable("id")
                                       Long departmentId)
    {
        departmentService.deleteDepartmentById(
                departmentId);
        return "Deleted Successfully";
    }
}