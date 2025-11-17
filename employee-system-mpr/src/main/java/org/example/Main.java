package org.example;

import org.example.service.*;
import org.example.model.*;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeService();
        ImportService importService = new ImportService(employeeService);

        ImportSummary importSummary = importService.importFromCsv("src/mpr-employee-file.txt");

        System.out.println("\n" + importSummary.toString());

        System.out.println("\nWszyscy pracownicy:");
        employeeService.getAllEmployees().forEach(System.out::println);

        System.out.println("\nPracownicy w firmie Klimawent:");
        employeeService.findByCompany("Klimawent").forEach(System.out::println);

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
}