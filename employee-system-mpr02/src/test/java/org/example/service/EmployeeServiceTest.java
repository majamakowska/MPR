package org.example.service;

import org.example.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;

    private Employee employeeJanusz;
    private Employee employeeZuzanna;
    private Employee employeeAnna;
    private Employee eDuplicateEmail;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();

        employeeJanusz = new Employee("Janusz", "Kowal", "janusz@mail.com", "A", Position.PROGRAMISTA, 9000);
        employeeZuzanna = new Employee("Zuzanna", "Niedziela", "zuzanna@mail.com", "A", Position.STAZYSTA, 3000);
        employeeAnna = new Employee("Anna", "Nowak", "anna@mail.com", "B", Position.MANAGER, 12000);
        eDuplicateEmail = new Employee("Janusz", "Rybak", "janusz@mail.com", "C", Position.MANAGER, 12000);
    }

    @AfterEach
    void tearDown() {}

    @Test
    void shouldAddUniqueEmployee() {
        service.addEmployee(employeeJanusz);
        service.addEmployee(employeeAnna);

        List<Employee> employees = service.getAllEmployees();

        assertEquals(2, employees.size());
        assertTrue(employees.contains(employeeJanusz));
        assertTrue(employees.contains(employeeAnna));
    }

    @Test
    void shouldThrowWhenEmailIsDuplicated() {
        service.addEmployee(employeeJanusz);
        assertThrows(IllegalArgumentException.class, () -> service.addEmployee(eDuplicateEmail));
    }

    @Test
    void shouldFindByCompany() {
        service.addEmployee(employeeJanusz);
        service.addEmployee(employeeZuzanna);
        service.addEmployee(employeeAnna);

        List<Employee> companyAEmployees = service.findByCompany("A");

        assertEquals(2, companyAEmployees.size());
        assertTrue(companyAEmployees.contains(employeeJanusz));
        assertTrue(companyAEmployees.contains(employeeZuzanna));
    }

    @Test
    void shouldSortByLastNameAlphabetically() {
        Employee a = new Employee("Antek", "Jabłuszko", "jabłuszko@mail.com", "C", Position.PROGRAMISTA, 8000);
        Employee b = new Employee("Basia", "Borówka", "borówka@mail.com", "C", Position.PROGRAMISTA, 8000);
        Employee c = new Employee("Czesio", "Czosneczek", "czosneczek@mail.com", "C", Position.PROGRAMISTA, 8000);
        service.addEmployee(a);
        service.addEmployee(b);
        service.addEmployee(c);

        List<Employee> sorted = service.sortByLastName();

        assertEquals(List.of(b, c, a), sorted);
    }

    @Test
    void shouldGroupAndCountByPosition() {
        service.addEmployee(employeeJanusz);
        service.addEmployee(employeeZuzanna);
        service.addEmployee(employeeAnna);

        Map<Position, List<Employee>> grouped = service.groupByPosition();
        assertEquals(1, grouped.get(Position.PROGRAMISTA).size());
        assertEquals(1, grouped.get(Position.MANAGER).size());
        assertEquals(1, grouped.get(Position.STAZYSTA).size());

        Map<Position, Long> counts = service.countByPosition();
        assertEquals(1L, counts.get(Position.PROGRAMISTA));
        assertEquals(1L, counts.get(Position.MANAGER));
        assertEquals(1L, counts.get(Position.STAZYSTA));
    }

    @Test
    void shouldFindAverageAndHighestSalary() {
        assertEquals(0.0, service.averageSalary());
        assertTrue(service.highestSalaryEmployee().isEmpty());

        service.addEmployee(employeeJanusz);
        service.addEmployee(employeeAnna);
        service.addEmployee(employeeZuzanna);

        double avg = service.averageSalary();
        assertEquals((9000 + 12000 + 3000) / 3.0, avg);

        Optional<Employee> top = service.highestSalaryEmployee();
        assertTrue(top.isPresent());
        assertEquals(employeeAnna, top.get());
    }

    @Test
    void shouldHandleSingleEmployeeAndEqualSalaries() {
        service.addEmployee(employeeJanusz);
        assertEquals(9000.0, service.averageSalary());

        employeeAnna.setSalary(9000);
        employeeZuzanna.setSalary(9000);
        service.addEmployee(employeeAnna);
        service.addEmployee(employeeZuzanna);

        assertEquals(9000.0, service.averageSalary());
        Optional<Employee> topEarner = service.highestSalaryEmployee();
        assertTrue(topEarner.isPresent());
        assertEquals(9000.0, topEarner.get().getSalary());
    }

    @Test
    void shouldFindInconsistentSalaries() {
        Employee inconsistentSalaryEmployee = new Employee("Aleksander", "Niski", "niskiewynagrodzenie@mail.com",
                "B", Position.MANAGER, 1000);
        service.addEmployee(employeeJanusz);
        service.addEmployee(inconsistentSalaryEmployee);

        List<Employee> inconsistent = service.validateSalaryConsistency();
        assertEquals(1, inconsistent.size());
        assertTrue(inconsistent.contains(inconsistentSalaryEmployee));
    }

    @Test
    void shouldGenerateCorrectCompanyStatistics() {
        service.addEmployee(employeeJanusz);
        service.addEmployee(employeeZuzanna);
        service.addEmployee(employeeAnna);

        Map<String, CompanyStatistics> stats = service.getCompanyStatistics();

        assertTrue(stats.containsKey("A"));
        CompanyStatistics companyAStats = stats.get("A");
        assertEquals(2, companyAStats.getEmployeeCount());
        assertEquals(6000.0, companyAStats.getAverageSalary());
        assertEquals("Janusz Kowal", companyAStats.getTopEarnerFullName());

        assertTrue(stats.containsKey("B"));
        CompanyStatistics statsCompanyB = stats.get("B");
        assertEquals(1, statsCompanyB.getEmployeeCount());
        assertEquals(12000.0, statsCompanyB.getAverageSalary());
        assertEquals("Anna Nowak", statsCompanyB.getTopEarnerFullName());
    }

    @Test
    void shouldReturnCorrectDefaultsForEmptyData() {
        assertEquals(0.0, service.averageSalary());
        assertTrue(service.highestSalaryEmployee().isEmpty());
        assertTrue(service.groupByPosition().isEmpty());
        assertTrue(service.countByPosition().isEmpty());
    }

    @Test
    void shouldWorkWithLargeNumberOfEmployees() {
        service.addEmployee(employeeJanusz);

        assertEquals(1, service.getAllEmployees().size());
        assertTrue(service.highestSalaryEmployee().isPresent());

        for (int i = 0; i < 100; i++) {
            service.addEmployee(new Employee("X"+i, "Y"+i, "x" + i + "@mail.com",
                    "Test", Position.PROGRAMISTA, 8000 + i));
        }
        assertTrue(service.getAllEmployees().size() >= 101);
        assertTrue(service.averageSalary() > 0);
    }
}
