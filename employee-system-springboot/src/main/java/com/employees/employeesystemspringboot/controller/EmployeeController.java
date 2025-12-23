package com.employees.employeesystemspringboot.controller;

import com.employees.employeesystemspringboot.dto.EmployeeDTO;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.EmploymentStatus;
import com.employees.employeesystemspringboot.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getEmployeesByCompanyName(@RequestParam(required = false) String company) {
        if(company == null) return employeeService.getAllEmployees().stream().map(Employee::toDTO).toList();
        return employeeService.findByCompany(normalizeCompanyParam(company)).stream().map(Employee::toDTO).toList();
    }

    @GetMapping("/{email}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable String email) {
        Employee employee = employeeService.findByEmail(email);
        return ResponseEntity.ok(employee.toDTO());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.addEmployee(Employee.fromDto(employeeDTO));
        return ResponseEntity.created(URI.create("/api/employees/" + employeeDTO.email())).body(employeeDTO);
    }

    @PutMapping("/{email}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable String email, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(email, Employee.fromDto(employeeDTO));
        return ResponseEntity.ok(employeeDTO);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity deleteEmployee(@PathVariable String email) {
        employeeService.removeByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}/status")
    public ResponseEntity<EmployeeDTO> updateEmployeeStatus(@PathVariable String email, @RequestBody EmploymentStatus status) {
        Employee employee = employeeService.findByEmail(email);
        employee.setStatus(status);
        return ResponseEntity.ok(employee.toDTO());
    }

    @GetMapping("/status/{status}")
    public List<EmployeeDTO> getEmployees(@PathVariable EmploymentStatus status) {
        return employeeService.groupByStatus().get(status).stream().map(Employee::toDTO).toList();
    }

    public static String normalizeCompanyParam(String param) {
        if (param == null || param.isBlank()) {
            return param;
        }
        return param.replace("-", " ").trim();
    }
}