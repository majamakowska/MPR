package com.employees.employeesystemspringboot.model;

import com.employees.employeesystemspringboot.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void shouldAssignBaseSalariesCorrectly() {
        assertEquals(25000, Position.PREZES.getBaseSalary());
        assertEquals(18000, Position.WICEPREZES.getBaseSalary());
        assertEquals(12000, Position.MANAGER.getBaseSalary());
        assertEquals(8000, Position.PROGRAMISTA.getBaseSalary());
        assertEquals(3000, Position.STAZYSTA.getBaseSalary());
    }
}