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

        assertAll(
                () -> assertEquals(e1.getEmail(), e2.getEmail()),
                () -> assertEquals(e1, e2, "Powinny być równe bo email jest taki sam"),
                () -> assertEquals(e1.hashCode(), e2.hashCode(), "hashCode powinien być oparty o email")
        );
    }

    @Test
    void shouldCapitalizeFirstName() {
        Employee e = new Employee("anna", "Nowak", "test@mail.com", "Firma", Position.MANAGER, 12000);
        assertEquals("Anna", e.getFirstName());
    }

    @Test
    void shouldCapitalizeLastName() {
        Employee e = new Employee("Anna", "nowak", "test@mail.com", "Firma", Position.MANAGER, 12000);
        assertEquals("Nowak", e.getLastName());
    }

    @Test
    void shouldCapitalizeCompanyName() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "firma", Position.MANAGER, 12000);
        assertEquals("Firma", e.getCompanyName());
    }

    @Test
    void shouldCapitalizeMultipleNames() {
        Employee e = new Employee("mArIa joana", "sIlva FonsEca riBeiRo", "test@mail.com", "faJna firMA", Position.MANAGER, 15500);
        assertAll(
                () -> assertEquals("Maria Joana", e.getFirstName()),
                () -> assertEquals("Silva Fonseca Ribeiro", e.getLastName()),
                () -> assertEquals("Fajna Firma", e.getCompanyName())
        );
    }

    @Test
    void shouldSetPosition() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.STAZYSTA);
        e.setPosition(Position.PROGRAMISTA);

        assertEquals(Position.PROGRAMISTA, e.getPosition());
    }

    @Test
    void shouldNotSetNullPosition() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.STAZYSTA);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> e.setPosition(null));
        assertTrue(ex.getMessage().contains("Stanowisko nie może być null"));
    }

    @Test
    void shouldSetSalary() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.PROGRAMISTA);
        e.setSalary(10000);

        assertEquals(10000, e.getSalary());
    }

    @Test
    void shouldNotSetSalaryBelowZero() {
        Employee e = new Employee("Anna", "Nowak", "test@mail.com", "Firma", Position.PROGRAMISTA);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> e.setSalary(-2));
        assertTrue(ex.getMessage().contains("Pensja nie może być ujemna"));
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
    void shouldThrowWhenFieldIsNullOrEmpty() {
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
    void shouldThrowWhenFirstNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee(null, "Nowak", "test@mail.com", "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Wartość 'imię' nie może być null"));
    }

    @Test
    void shouldThrowWhenLastNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", null, "test@mail.com", "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Wartość 'nazwisko' nie może być null"));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", null, "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Wartość 'email' nie może być null"));
    }

    @Test
    void shouldThrowWhenCompanyNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "test@mail.com", null, Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Wartość 'nazwa firmy' nie może być null"));
    }

    @Test
    void shouldThrowWhenPositionIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "test@mail.com", "Frima", null, 0));
        assertTrue(ex.getMessage().contains("Stanowisko nie może być null"));
    }

    @Test
    void shouldThrowWhenFirstNameIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("", "Nowak", "test@mail.com", "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Pole 'imię' nie może być puste"));
    }

    @Test
    void shouldThrowWhenLastNameIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "", "test@mail.com", "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Pole 'nazwisko' nie może być puste"));
    }

    @Test
    void shouldThrowWhenEmailIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "", "Frima", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Pole 'email' nie może być puste"));
    }

    @Test
    void shouldThrowWhenCompanyNameIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "test@mail.com", "", Position.PROGRAMISTA, 0));
        assertTrue(ex.getMessage().contains("Pole 'nazwa firmy' nie może być puste"));
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