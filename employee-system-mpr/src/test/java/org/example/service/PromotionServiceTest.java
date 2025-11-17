package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class PromotionServiceTest {

    @Test
    void shouldPromoteEmployee() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.promote(employee, Position.MANAGER);

        assertEquals(Position.MANAGER, employee.getPosition());
    }

    @Test
    void shouldUpdateSalaryWithPromotion() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.promote(employee, Position.MANAGER);
        assertThat(employee.getSalary(), is(equalTo(12000.00)));
    }

    @Test
    void shouldThrowExceptionWhenPromotingToSamePosition() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.MANAGER);

        assertThatThrownBy(() -> promotionService.promote(employee, Position.MANAGER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik już ma stanowisko");
    }

    @Test
    void shouldThrowExceptionWhenDemotingEmployee() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.MANAGER);

        assertThatThrownBy(() -> promotionService.promote(employee, Position.PROGRAMISTA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie można awansować");
    }

    @Test
    void shouldRaiseSalary() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        promotionService.giveRaise(employee, 10.0);

        assertThat(employee.getSalary()).isEqualTo(Position.PROGRAMISTA.getBaseSalary() * 1.1);
    }

    @Test
    void shouldThrowExceptionWhenRaisePercentageBelowZero() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, -15))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Procent podwyżki nie może być ujemny");
    }

    @Test
    void shouldThrowExceptionWhenRaisePercentageIsZero() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Procent podwyżki nie może być 0");
    }

    @Test
    void shouldThrowExceptionWhenRaiseIsOverLimit() {
        PromotionService promotionService = new PromotionService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> promotionService.giveRaise(employee, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pensja przekracza limit dla stanowiska");
    }

}