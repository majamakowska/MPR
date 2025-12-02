package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.model.Task;
import org.example.testdoubles.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskAssignmentServiceTest {

    Employee createEmployee(String firstName, Set<String> skills) {
        Employee employee = new Employee(firstName, "Test", firstName.toLowerCase()+"@test", "Firma X", Position.PROGRAMISTA);
        employee.addSkills(skills);
        return employee;
    }

    /** Używa:
     * - CalendarStub, który zawsze zwraca przekazaną mu wcześniej listę pracowników,
     * - SkillsRepositoryFake przechowującego w pamięci przekazaną mu listę umiejętności pracowników i zwracającego pracownika z odpowiednimi kompetencjami,
     * - AllocationRepositorySpy zapisującego wszystkie operacje przypisania pracownika do zadania
     *
     * ConfigDummy nie jest w tym teście używany, jest tylko placeholderem, aby konstruktor był kompletny.
     *
     * Test sprawdza czy:
     * - znaleziono pierwszego pracownika z umiejętnością "java",
     * - opoeracja przypisania pracownika do zadania została wykonana (raz) */
    @Test
    void shouldAssignFirstAvailableEmployeeWithSkills() {
        Employee a = createEmployee("Anna", Set.of("java","sql"));
        Employee b = createEmployee("Bartosz", Set.of("python"));
        CalendarStub calendar = new CalendarStub(List.of(a,b));
        SkillsRepositoryFake skills = new SkillsRepositoryFake(Map.of(a, Set.of("java","sql"), b, Set.of("python")));
        AllocationRepositorySpy allocationSpy = new AllocationRepositorySpy();

        TaskAssignmentService service = new TaskAssignmentService(calendar, skills, allocationSpy, new ConfigDummy());

        Task task = new Task("t1", Set.of("java"), Duration.ofHours(8));
        Employee assigned = service.assign(task);

        assertNotNull(assigned);
        assertEquals(a, assigned);
        assertEquals(1, allocationSpy.getAssignments().size());
    }

    /** Używa:
     * - CalendarStub,
     * - SkillsRepositoryFake
     * - AllocationRepositorySpy
     *
     * Test sprawdza czy:
     * - brak pracownika z odpowiednimi kompetencjami daje wynik null,
     * - nie wykonano operacji przypisania */
    @Test
    void shouldReturnNullIfNoEmployeeHasSkills() {
        Employee a = createEmployee("Anna", Set.of("java"));
        CalendarStub calendar = new CalendarStub(List.of(a));
        SkillsRepositoryFake skills = new SkillsRepositoryFake(Map.of(a, Set.of("java")));
        AllocationRepositorySpy allocationSpy = new AllocationRepositorySpy();

        TaskAssignmentService service = new TaskAssignmentService(calendar, skills, allocationSpy, new ConfigDummy());
        Task task = new Task("t2", Set.of("python"), Duration.ofHours(8));
        Employee assigned = service.assign(task);
        assertNull(assigned);
        assertTrue(allocationSpy.getAssignments().isEmpty());
    }

    /** Używa:
     * - CalendarStub,
     * - SkillsRepositoryFake
     * - AllocationRepositorySpy
     *
     * Test sprawdza czy:
     * - zignorowano pracownika bez kompetencji i wybrano pracownika z umiejętnością "python",
     * - opoeracja przypisania pracownika do zadania została wykonana (raz) */
    @Test
    void assignsSecondAvailableEmployeeIfFirstLacksSkill() {
        Employee a = createEmployee("Anna", Set.of("c"));
        Employee b = createEmployee("Bartosz", Set.of("python"));
        CalendarStub calendar = new CalendarStub(List.of(a,b));
        SkillsRepositoryFake skills = new SkillsRepositoryFake(Map.of(a, Set.of("c"), b, Set.of("python")));
        AllocationRepositorySpy allocationSpy = new AllocationRepositorySpy();

        TaskAssignmentService service = new TaskAssignmentService(calendar, skills, allocationSpy, new ConfigDummy());
        Task task = new Task("t3", Set.of("python"), Duration.ofHours(4));
        Employee assigned = service.assign(task);

        assertNotNull(assigned);
        assertEquals(b, assigned);
        assertEquals(1, allocationSpy.getAssignments().size());
    }
}
