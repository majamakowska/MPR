package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class PromotionServiceTest {

    PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
    }

    @Test
    void shouldPromoteEmployee() {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.promote(employee, Position.MANAGER);

        assertEquals(Position.MANAGER, employee.getPosition());

        assertThat(employee.getPosition()).isNotEqualTo(Position.PROGRAMISTA);
        assertThat(employee.getPosition()).isEqualTo(Position.MANAGER);
        assertThat(employee.getPosition()).isNotNull();
    }

    @Test
    void shouldUpdateSalaryWithPromotion() {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.promote(employee, Position.MANAGER);

        assertThat(employee.getSalary()).isGreaterThan(11000.0).isLessThan(13000.0);
        assertThat(employee.getSalary(), is(equalTo(12000.0)));
    }

    @Test
    void shouldThrowExceptionWhenPromotingToSamePosition() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.MANAGER);

        assertThatThrownBy(() -> promotionService.promote(employee, Position.MANAGER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik już ma stanowisko");
    }

    @Test
    void shouldThrowExceptionWhenDemotingEmployee() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.MANAGER);

        assertThatThrownBy(() -> promotionService.promote(employee, Position.PROGRAMISTA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie można awansować");
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNullForPromotion() {
        assertThatThrownBy(() -> promotionService.promote(null, Position.MANAGER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }

    @Test
    void shouldThrowExceptionWhenNewPositionIsNull() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        assertThatThrownBy(() -> promotionService.promote(employee, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nowe stanowisko nie może być null");
    }

    @Test
    void shouldRaiseSalary() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.giveRaise(employee, 10.0);

        assertThat(employee.getSalary()).isEqualTo(Position.PROGRAMISTA.getBaseSalary() * 1.1);
    }

    @Test
    void shouldThrowExceptionWhenRaisePercentageBelowZero() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, -15))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Procent podwyżki nie może być ujemny");
    }

    @Test
    void shouldThrowExceptionWhenRaisePercentageIsZero() {
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Procent podwyżki nie może być 0");
    }

    @Test
    void shouldThrowExceptionWhenRaiseIsOverLimit() {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pensja przekracza limit dla stanowiska");
    }

    @Test
    void shouldRaiseSalaryWithNoLimit() {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PREZES, 100000.0);

        promotionService.giveRaise(employee, 9000);

        assertThat(employee.getSalary()).isEqualTo(9100000.0);
    }

    @Test
    void shouldRaiseSalaryBySmallPercentage() {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA, 8000.0);

        promotionService.giveRaise(employee, 0.1);

        assertThat(employee.getSalary()).isEqualTo(8008.0);
    }

    @Test
    void shouldRaiseSalaryCloseToLimit() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA, 11000.0);

        promotionService.giveRaise(employee, 9);

        assertThat(employee.getSalary()).isEqualTo(11990.0);
    }

    @ParameterizedTest(name = "{index} => position={0}, percent={1}, expectedSalary={2}")
    @CsvSource({
            "STAZYSTA, 5, 3150.0",
            "STAZYSTA, 10, 3300.0",
            "STAZYSTA, 15, 3450.0",

            "PROGRAMISTA, 5, 8400.0",
            "PROGRAMISTA, 10, 8800.0",
            "PROGRAMISTA, 20, 9600.0",

            "MANAGER, 5, 12600.0",
            "MANAGER, 10, 13200.0",
            "MANAGER, 20, 14400.0",

            "WICEPREZES, 5, 18900.0",
            "WICEPREZES, 10, 19800.0",
            "WICEPREZES, 20, 21600.0",

            "PREZES, 5, 26250.0",
            "PREZES, 10, 27500.0",
            "PREZES, 20, 30000.0"
    })
    void shouldRaiseSalaryByGivenPercentage(String positionName, double percent, double expectedSalary) {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.valueOf(positionName));

        promotionService.giveRaise(employee, percent);

        assertThat(employee.getSalary()).isEqualTo(expectedSalary);
    }

    @ParameterizedTest(name = "{index} => position={0}, currentSalary={1}, percent={2}, expectedSalary={3}")
    @CsvSource({
            "STAZYSTA, 3100.0, 5, 3255.0",
            "STAZYSTA, 3300.0, 10, 3630.0",
            "STAZYSTA, 3200.0, 15, 3680.0",

            "PROGRAMISTA, 8500.0, 5, 8925.0",
            "PROGRAMISTA, 9000.0, 10, 9900.0",
            "PROGRAMISTA, 8750.0, 15, 10062.5",

            "MANAGER, 12500.0, 5, 13125.0",
            "MANAGER, 13000.0, 10, 14300.0",
            "MANAGER, 12600.0, 15, 14490.0",

            "WICEPREZES, 18500.0, 5, 19425.0",
            "WICEPREZES, 19000.0, 10, 20900.0",
            "WICEPREZES, 18900.0, 15, 21735.0",

            "PREZES, 26000.0, 5, 27300.0",
            "PREZES, 27000.0, 10, 29700.0",
            "PREZES, 30000.0, 15, 34500.0"
    })
    void shouldRaiseNonBaseSalaryByGivenPercentage(String positionName, double currentSalary, double percent, double expectedSalary) {
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.valueOf(positionName), currentSalary);

        promotionService.giveRaise(employee, percent);

        assertThat(employee.getSalary()).isEqualTo(expectedSalary);
    }
}