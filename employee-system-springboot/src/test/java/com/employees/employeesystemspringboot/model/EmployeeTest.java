package com.employees.employeesystemspringboot.model;

import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.Position;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void shouldCompareEmployeesByEmail() {
        Employee e1 = new Employee("Jacek", "Jackowski", "test@mail.com", "Firma1", Position.PROGRAMISTA, 9000);
        Employee e2 = new Employee("Jacek", "Inny", "test@mail.com", "Firma2", Position.STAZYSTA, 3200);

        assertEquals(e1, e2, "Powinny być równe bo email jest taki sam");
        assertEquals(e1.hashCode(), e2.hashCode(), "hashCode powinien być oparty o email");
    }

    @Test
    void shouldCapitalizeNames() {
        Employee e = new Employee("mArIa joana", "riBeiRo", "test@mail.com", "firMA", Position.MANAGER, 15500);
        assertEquals("Maria Joana", e.getFirstName());
        assertEquals("Ribeiro", e.getLastName());
        assertEquals("Firma", e.getCompanyName());
    }

    @Test
    void shouldReturnFullName() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.MANAGER, 12000);
        assertEquals("Anna Nowak", e.getFullName());
    }

    @Test
    void shouldThrowWhenSalaryIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Maciej", "Biedak", "test@mail.com", "Firma", Position.PREZES, -1));
        assertTrue(ex.getMessage().contains("Pensja nie może być ujemna"));
    }

    @Test
    void shouldThrowWhenFiledIsNullOrEmpty() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new Employee(null, "Kowalski", "test@mail.com", "Firma", Position.PROGRAMISTA, 8000)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new Employee("Janusz", "  ", "test@mail.com", "Firma", Position.PROGRAMISTA, 8000)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new Employee("Janusz", "Kowalski", null, "Firma", Position.PROGRAMISTA, 8000))
        );
    }

    @Test
    void shouldThrowWhenPositionIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "test@mail.com", "Frima", null, 0));
        assertTrue(ex.getMessage().contains("Stanowisko nie może być null"));
    }

    @Test
    void shouldIncludeDataInToString() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.STAZYSTA, 3000);
        String s = e.toString();
        assertTrue(s.contains("Anna N"));
        assertTrue(s.contains("test@mail.com"));
        assertTrue(s.contains("Firma"));
        assertTrue(s.contains("STAZYSTA"));
    }
}