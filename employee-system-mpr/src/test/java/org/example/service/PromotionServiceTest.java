package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

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
}