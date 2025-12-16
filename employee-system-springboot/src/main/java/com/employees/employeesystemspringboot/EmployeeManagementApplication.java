package com.employees.employeesystemspringboot;

import com.employees.employeesystemspringboot.model.CompanyStatistics;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.ImportSummary;
import com.employees.employeesystemspringboot.service.APIService;
import com.employees.employeesystemspringboot.service.EmployeeService;
import com.employees.employeesystemspringboot.service.ImportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.*;

@SpringBootApplication
@ImportResource("classpath:employees-beans.xml")
public class EmployeeManagementApplication implements CommandLineRunner {

    private final EmployeeService employeeService;
    private final ImportService importService;
    private final List<Employee> predefinedEmployees;

    public EmployeeManagementApplication(
            EmployeeService employeeService,
            ImportService importService,
            @Qualifier("xmlEmployees") List<Employee> xmlEmployees) {

        this.employeeService = employeeService;
        this.importService = importService;
        this.predefinedEmployees = xmlEmployees;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\nXML");
        for (Employee pEmployee : predefinedEmployees) {
            try {
                employeeService.addEmployee(pEmployee);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        showItWorks();

        employeeService.deleteAllEmployees();

        System.out.println("\nCSV");
        ImportSummary csvImportSummary = importService.importFromCsv(employeeService);

        printImportThing(csvImportSummary);
        showItWorks();

        employeeService.deleteAllEmployees();

        System.out.println("\nAPI");
        ImportSummary apiImportSummary = importService.importFromApi(employeeService);

        printImportThing(apiImportSummary);
        showItWorks();
    }

    public void showItWorks () {
        System.out.println("\nWszyscy pracownicy:");
        employeeService.getAllEmployees().forEach(System.out::println);

        System.out.println("\nPracownicy posortowani alfabetycznie:");
        employeeService.sortByLastName().forEach(System.out::println);

        System.out.println("\nPracownicy według stanowiska:");
        employeeService.groupByPosition().forEach((position, list) -> System.out.println(position + ": " + list));

        System.out.println("\nLiczba pracowników na stanowiskach:");
        employeeService.countByPosition().forEach((position, count) -> System.out.println(position + ": " + count));

        System.out.println("\nŚrednie wynagrodzenie:");
        System.out.println(employeeService.averageSalary());

        System.out.println("\nPracownik z najwyższym wynagrodzeniem:");
        employeeService.highestSalaryEmployee().ifPresent(System.out::println);

        List<Employee> inconsistentSalaryEmployees = employeeService.validateSalaryConsistency();
        System.out.println("\nPracownicy z wynagrodzeniem niższym niż bazowa stawka ich stanowiska:");
        if (!inconsistentSalaryEmployees.isEmpty()) {
            inconsistentSalaryEmployees.forEach(System.out::println);
        } else System.out.println("Brak");

        Map<String, CompanyStatistics> companyStatistics = employeeService.getCompanyStatistics();
        System.out.println("\nStatystyki firm:");
        companyStatistics.forEach((company, statistics)
                -> {System.out.println(company + " - " +  statistics);});
    }

    public void printImportThing(ImportSummary importSummary) {
        System.out.println("*** Podsumowanie Importu ***");
        System.out.println(importSummary.toString());
    }
}
