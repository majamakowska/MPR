package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class CareerHistoryServiceTest {

    CareerHistoryService careerService;

    @BeforeEach
    void setUp() {
        careerService = new CareerHistoryService();
    }

    @ParameterizedTest(name = "hirDate={0} => expectedYearsWorked={1}")
    @MethodSource("regularDatesData")
    void shouldCalculateYearsWorkedForRegularDates(LocalDate hireDate, int expectedYearsWorked) {
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

    @ParameterizedTest(name = "hireDate={0} => expectedYearsWorked={1}")
    @MethodSource("leapYearData")
    void shouldCalculateYearsWorkedForLeapYears(LocalDate hireDate, int expectedYearsWorked) {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        employee.setHireDate(hireDate);

        int yearsWorked = careerService.calculateYearsWorked(employee);

        assertThat(yearsWorked).isEqualTo(expectedYearsWorked);
    }
    static Stream<Object[]> leapYearData() {
        LocalDate now = LocalDate.now();
        return Stream.of(
                new Object[]{LocalDate.of(2020, 2, 29), now.getYear() - 2020},
                new Object[]{LocalDate.of(2016, 2, 29), now.getYear() - 2016},
                new Object[]{LocalDate.of(2000, 2, 29), now.getYear() - 2000}
        );
    }

    @ParameterizedTest(name = "futureHireDate={0} => expectedYearsWorked={1}")
    @MethodSource("futureDateData")
    void shouldReturnZeroOrNegativeForFutureHireDates(LocalDate hireDate, int expectedYearsWorked) {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        employee.setHireDate(hireDate);

        int yearsWorked = careerService.calculateYearsWorked(employee);

        assertThat(yearsWorked).isEqualTo(expectedYearsWorked);
    }
    static Stream<Object[]> futureDateData() {
        LocalDate now = LocalDate.now();
        return Stream.of(
                new Object[]{now.plusYears(1), -1},
                new Object[]{now.plusMonths(6), 0},
                new Object[]{now.plusDays(10), 0}
        );
    }

    @Test
    void shouldFindEmployeesWithFullYearsOfService() {
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

    @Test
    void shouldReturnZeroWhenHireDateIsNull() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        int yearsWorked = careerService.calculateYearsWorked(employee);

        assertThat(yearsWorked).isEqualTo(0);
    }

    @Test
    void shouldNotIncludeEmployeesWithNullHireDateInAnniversary() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        careerService.addEmployee(employee);

        List<Employee> anniversaryEmployees = careerService.getEmployeesWithAnniversary();
        assertThat(anniversaryEmployees).doesNotContain(employee);
    }
}
