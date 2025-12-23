package com.employees.employeesystemspringboot.controller;

import com.employees.employeesystemspringboot.dto.CompanyStatisticsDTO;
import com.employees.employeesystemspringboot.dto.EmployeeDTO;
import com.employees.employeesystemspringboot.model.EmploymentStatus;
import com.employees.employeesystemspringboot.model.Position;
import com.employees.employeesystemspringboot.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.employees.employeesystemspringboot.controller.EmployeeController.normalizeCompanyParam;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final EmployeeService employeeService;

    public StatisticsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/salary/average")
    public Map<String, Double> getAverageSalaryByCompany(@RequestParam(required = false) String company) {
        if (company == null) return employeeService.getAverageSalaries();
        return Map.of("avarageSalary", employeeService.getAverageSalaryByCompanyName(normalizeCompanyParam(company)));
    }

    @GetMapping("/company/{companyName}")
    public CompanyStatisticsDTO getCompanyStatistics(@PathVariable String companyName) {
       return employeeService.getCompanyStatisticsByCompanyName(normalizeCompanyParam(companyName)).toDto();
    }

    @GetMapping("/positions")
    public Map<Position, Long> countByPositionInCompany() {
        return employeeService.countByPosition();
    }

    @GetMapping("/status")
    public Map<EmploymentStatus, Long> countByStatusInCompany() {
        return employeeService.countByStatus();
    }
}
