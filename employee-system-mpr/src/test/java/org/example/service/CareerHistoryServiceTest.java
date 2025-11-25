package org.example.service;

import org.assertj.core.api.SoftAssertions;
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
    
    private static Employee createEmployee(String firstName, String lastName, Position position) {
        return new Employee(firstName, lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@test.com",
                "Firma X", position);
    }

    @Test
    void testAddNullEmployeeThrowsException() {
        assertThatThrownBy(() -> careerService.addEmployee(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie można dodać null");
    }

    @ParameterizedTest(name = "hirDate={0} => expectedYearsWorked={1}")
    @MethodSource("regularDatesData")
    void shouldCalculateYearsWorkedForRegularDates(LocalDate hireDate, int expectedYearsWorked) {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
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
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
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
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
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
        Employee employee1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        employee1.setHireDate(LocalDate.now().minusYears(5));
        Employee employee2 = createEmployee("Jan", "Dudek", Position.MANAGER);
        employee2 .setHireDate(LocalDate.now().minusYears(3));
        Employee employee3 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(10));
        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> anniversaryEmployees = careerService.getEmployeesWithAnniversary();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(anniversaryEmployees).isNotNull().isNotEmpty().hasSize(2);
        softly.assertThat(anniversaryEmployees).containsExactlyInAnyOrder(employee1, employee3).doesNotContain(employee2);
        softly.assertAll();
    }

    @Test
    void shouldFilterEmployeesByYearsWorked() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        employee1.setHireDate(LocalDate.now().minusYears(5));
        Employee employee2 = createEmployee("Jan", "Dudek", Position.MANAGER);
        employee2 .setHireDate(LocalDate.now().minusYears(3));
        Employee employee3 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(10));
        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> filtered = careerService.getEmployeesByYearsWorked(4, 10);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(filtered).isNotNull().isNotEmpty().hasSize(2);
        softly.assertThat(filtered).containsExactlyInAnyOrder(employee1, employee3)
                .doesNotContain(employee2);
        softly.assertAll();
        softly.assertAll();
    }

    @Test
    void shouldReturnZeroWhenHireDateIsNull() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        int yearsWorked = careerService.calculateYearsWorked(employee);

        assertThat(yearsWorked).isEqualTo(0);
    }

    @Test
    void shouldNotIncludeEmployeesWithNullHireDateInAnniversary() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        careerService.addEmployee(employee);

        assertThat(careerService.getEmployeesWithAnniversary())
                .doesNotContain(employee)
                .isEmpty();
    }

    @Test
    void shouldSortEmployeesByExperienceAscending() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.STAZYSTA);
        employee1.setHireDate(LocalDate.now().minusYears(1));
        Employee employee2 = createEmployee("Jan", "Dudek", Position.PROGRAMISTA);
        employee2.setHireDate(LocalDate.now().minusYears(5));
        Employee employee3 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(3));

        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> sorted = careerService.getEmployeesSortedByExperience();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sorted).isNotNull().isNotEmpty().hasSize(3);
        softly.assertThat(sorted).containsExactly(employee1, employee3, employee2);
        softly.assertAll();
    }

    @Test
    void shouldReturnEmployeesWithExactYearsWorked() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.STAZYSTA);
        employee1.setHireDate(LocalDate.now().minusYears(2));
        Employee employee2 = createEmployee("Jan", "Dudek", Position.PROGRAMISTA);
        employee2.setHireDate(LocalDate.now().minusYears(3));
        Employee employee3 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        employee3.setHireDate(LocalDate.now().minusYears(3));

        careerService.addEmployee(employee1);
        careerService.addEmployee(employee2);
        careerService.addEmployee(employee3);

        List<Employee> exact3Years = careerService.getEmployeesWithExactYearsWorked(3);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(exact3Years).isNotNull().isNotEmpty().hasSize(2);
        softly.assertThat(exact3Years).containsExactlyInAnyOrder(employee2, employee3);
        softly.assertAll();
    }
}
