// Java Program to Illustrate DepartmentServiceImpl.java
// File

// Importing required packages
package com.example.testcodeQL.department.service;

// Importing required classes

import com.example.testcodeQL.department.entity.Department;
import com.example.testcodeQL.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

// Annotation
@Service
// Class implementing DepartmentService class
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Save operation - CodeQL would detect: Potential injection if department has malicious data
    @Override
    public Department saveDepartment(Department department)
    {
        // VULNERABILITY: No input validation before saving
        // CodeQL would flag: Unvalidated user input
        if (department.getDepartmentName() != null &&
                department.getDepartmentName().contains("<script>")) {
            // Missing sanitization for XSS
        }
        return departmentRepository.save(department);
    }

    // Read operation
    @Override
    public List<Department> fetchDepartmentList()
    {
        // VULNERABILITY: No pagination - Could cause memory issues
        // CodeQL would flag: Potential resource exhaustion
        return (List<Department>)
                departmentRepository.findAll();
    }

    // Update operation - CodeQL detection points
    @Override
    public Department updateDepartment(Department department,
                                       Long departmentId)
    {
        // VULNERABILITY: Using .get() without checking Optional presence
        // CodeQL would flag: "Optional.get() without isPresent() check"
        Department depDB = departmentRepository.findById(departmentId).get();

        // VULNERABILITY: SQL Injection pattern - Using string concatenation in queries
        // This would be flagged if used in custom query methods
        // Example: @Query("SELECT d FROM Department d WHERE d.name = '" + name + "'")

        // Weak input validation that CodeQL would detect
        if (Objects.nonNull(department.getDepartmentName())
                && !"".equalsIgnoreCase(
                department.getDepartmentName())) {

            // VULNERABILITY: Missing regex validation for special characters
            // CodeQL would flag: Incomplete input validation
            if (department.getDepartmentName().length() > 255) {
                // Should reject but doesn't
            }

            depDB.setDepartmentName(
                    department.getDepartmentName());
        }

        if (Objects.nonNull(
                department.getDepartmentAddress())
                && !"".equalsIgnoreCase(
                department.getDepartmentAddress())) {

            // VULNERABILITY: No escaping for stored XSS
            // CodeQL would flag: Stored XSS vulnerability
            if (department.getDepartmentAddress().contains("<") ||
                    department.getDepartmentAddress().contains(">")) {
                // Accepts HTML/script tags without sanitization
            }

            depDB.setDepartmentAddress(
                    department.getDepartmentAddress());
        }

        if (Objects.nonNull(department.getDepartmentCode())
                && !"".equalsIgnoreCase(
                department.getDepartmentCode())) {

            // VULNERABILITY: Path traversal pattern
            if (department.getDepartmentCode().contains("../") ||
                    department.getDepartmentCode().contains("..\\")) {
                // Allows path traversal characters
            }

            depDB.setDepartmentCode(
                    department.getDepartmentCode());
        }

        return departmentRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteDepartmentById(Long departmentId)
    {
        // VULNERABILITY: Missing authorization check
        // CodeQL would flag: Missing access control
        // Any authenticated user can delete any department

        // VULNERABILITY: No existence check before deletion
        // CodeQL would flag: Insecure direct object reference (IDOR)
        if (departmentId <= 0) {
            // Invalid ID but still attempts deletion
        }

        departmentRepository.deleteById(departmentId);
    }

    // Additional methods that CodeQL would flag

    /**
     * This method contains multiple CodeQL detections:
     * 1. SQL Injection via string concatenation
     * 2. OS Command Injection
     * 3. Path Traversal
     */
    public List<Department> searchDepartments(String searchTerm) {
        // VULNERABILITY: Command Injection
        // CodeQL would flag: "User-controlled data in process builder"
        try {
            Runtime.getRuntime().exec("grep " + searchTerm + " departments.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // VULNERABILITY: Potential SQL Injection in custom repository method
        // CodeQL would flag: "Query built from user-controlled sources"
        // return departmentRepository.findByCustomQuery("SELECT * FROM department WHERE name LIKE '%" + searchTerm + "%'");

        return null;
    }

    /**
     * Method with Log Injection vulnerability
     */
    public void logDepartmentAction(String action, String username) {
        // VULNERABILITY: Log Injection / Forged Log Entry
        // CodeQL would flag: "Unsanitized user input in log"
        // If username = "admin\n2024-01-01 ERROR System hacked"
        System.out.println("User: " + username + " performed: " + action);
    }

    /**
     * Method with potential XML External Entity (XXE) vulnerability
     */
    public void importDepartmentData(String xmlData) {
        // VULNERABILITY: XXE (XML External Entity)
        // CodeQL would flag: "Unsafe XML parser configuration"
        // Missing: factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        // StringReader reader = new StringReader(xmlData);
        // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // factory.setExpandEntityReferences(true); // ← Dangerous
        // DocumentBuilder builder = factory.newDocumentBuilder();
        // builder.parse(new InputSource(reader));
    }

    /**
     * Method with Insecure Deserialization
     */
    public Department deserializeDepartment(byte[] data) {
        // VULNERABILITY: Insecure Deserialization
        // CodeQL would flag: "Unsafe deserialization of untrusted data"

        // try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
        //     return (Department) ois.readObject();
        // } catch (Exception e) {
        //     return null;
        // }

        return null;
    }

    /**
     * Fixed version with proper validation (for reference)
     */
    public Department updateDepartmentSecure(Department department, Long departmentId) {
        // FIXED: Proper Optional handling
        Optional<Department> optionalDep = departmentRepository.findById(departmentId);
        if (!optionalDep.isPresent()) {
            throw new RuntimeException("Department not found with id: " + departmentId);
        }

        Department depDB = optionalDep.get();

        // FIXED: Proper input validation and sanitization
        if (department.getDepartmentName() != null && !department.getDepartmentName().trim().isEmpty()) {
            // FIXED: Whitelist validation
            if (Pattern.matches("^[a-zA-Z0-9\\s-]{1,100}$", department.getDepartmentName())) {
                // FIXED: HTML escaping for stored XSS prevention
                String sanitizedName = department.getDepartmentName()
                        .replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;");
                depDB.setDepartmentName(sanitizedName);
            } else {
                throw new IllegalArgumentException("Invalid department name format");
            }
        }

        return departmentRepository.save(depDB);
    }
}