
// Importing packages modules
package com.example.testcodeQL.department.controller;// Java Program to Illustrate DepartmentController.java File


// Importing required classes

import com.example.testcodeQL.department.entity.Department;
import com.example.testcodeQL.department.service.DepartmentService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

// Annotation
@RestController
// Class
public class DepartmentController {

    @Autowired private DepartmentService departmentService;
//test

    public static final String ACCOUNT_SID = "AC5d61f8e4be76ea62b60d293abf000e16";
    public static final String AUTH_TOKEN = "293b5a3d5566620dcdc86c1d29226663";
    // Save operation
    // ADD THIS METHOD TO TRIGGER CODEQL ALERT
    @GetMapping("/test-codeql")
    public String testCodeQL(@RequestParam String input) {
        // This will trigger SQL Injection alert
        String query = "SELECT * FROM departments WHERE name = '" + input + "'";

        // This will trigger Command Injection alert
        try {
            Runtime.getRuntime().exec("echo " + input);
        } catch (Exception e) {}

        // This will trigger XSS alert
        return "<script>alert('" + input + "')</script>";
    }
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
        @GetMapping("/twilio")

public String twilio()
    {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Call call = Call.creator(new com.twilio.type.PhoneNumber("+13365305464"),
                        new com.twilio.type.PhoneNumber("+13365715930"),
                        URI.create("http://demo.twilio.com/docs/voice.xml"))
                .create();

        System.out.println(call.getSid());
        return call.getSid();
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
    public String makeCall() throws URISyntaxException {
        String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String authToken = System.getenv("TWILIO_AUTH_TOKEN");
        String toNumber = System.getenv("TWILIO_TO_NUMBER");
        String fromNumber = System.getenv("TWILIO_FROM_NUMBER");

        // Initialize Twilio
        Twilio.init(accountSid, authToken);

        // Make a call
        Call call = Call.creator(
                new com.twilio.type.PhoneNumber(toNumber),
                new com.twilio.type.PhoneNumber(fromNumber),
                new URI("http://demo.twilio.com/docs/voice.xml")  // Your TwiML URL
        ).create();

        return "Call initiated: " + call.getSid();
    }


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
