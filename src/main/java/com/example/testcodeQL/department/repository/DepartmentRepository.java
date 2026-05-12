package com.example.testcodeQL.department.repository;

import com.masoodali09.springbootdemoproject.department.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

}