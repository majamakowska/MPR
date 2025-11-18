package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class CareerHistoryServiceTest {

    @ParameterizedTest(name = "hirDate={0} => expectedYearsWorked={1}")
    @MethodSource("regularDatesData")
    void shouldCalculateYearsWorkedForRegularDates(LocalDate hireDate, int expectedYearsWorked) {
        CareerHistoryService careerService = new CareerHistoryService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        employee.setHireDate(hireDate);

        int yearsWorked = careerService.calculateYearsWorked(employee);

        assertThat(yearsWorked).isEqualTo(expectedYearsWorked);
    }
    static Stream<Object[]> regularDatesData() {
        LocalDate now = LocalDate.now();
        return Stream.of(
                new Object[]{now.minusYears(5), 5},
                new Object[]{now.minusYears(10).minusDays(1), 10},
                new Object[]{now.minusYears(10).plusDays(1), 9},
                new Object[]{now, 0}
        );
    }

    @Test
    void shouldFindEmployeesWithFullYearsOfService() {
        CareerHistoryService careerService = new CareerHistoryService();

        Employee employee1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        employee1.setHireDate(LocalDate.now().minusYears(5));
        Employee employee2 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma Y", Position.MANAGER);
        employee2 .setHireDate(LocalDate.now().minusYears(3));
        Employee employee3 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma Z", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(10));

        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> anniversaryEmployees = careerService.getEmployeesWithAnniversary();

        assertThat(anniversaryEmployees).containsExactlyInAnyOrder(employee1, employee3)
                .doesNotContain(employee2);
    }

    @Test
    void shouldFilterEmployeesByYearsWorked() {
        CareerHistoryService careerService = new CareerHistoryService();

        Employee employee1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        employee1.setHireDate(LocalDate.now().minusYears(5));
        Employee employee2 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma Y", Position.MANAGER);
        employee2 .setHireDate(LocalDate.now().minusYears(3));
        Employee employee3 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma Z", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(10));

        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> filtered = careerService.getEmployeesByYearsWorked(4, 10);

        assertThat(filtered).containsExactlyInAnyOrder(employee1, employee3)
                .doesNotContain(employee2);
    }
}
