package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class TeamServiceTest {
    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("teamCompositionData")
    void shouldValidateTeamComposition(List<Employee> teamMembers, boolean expectedValid) {
        TeamService teamService = new TeamService();

        boolean isTeamCompositionValid = teamService.isTeamCompositionValid(teamMembers);

        assertThat(isTeamCompositionValid).isEqualTo(expectedValid);
    }
    static Stream<Object[]> teamCompositionData() {
        Employee intern = new Employee("Krzysztof", "Nowicki", "krzysztof.nowicki@test.com","Firma X", Position.STAZYSTA);
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, manager1), true},
                new Object[]{List.of(developer1, developer2, manager1, intern), true},
                new Object[]{List.of(developer1, developer2, developer3, developer4, manager1), true},
                new Object[]{List.of(developer1, developer2, developer3, manager1, manager2), true},
                new Object[]{List.of(developer1, developer2, intern, manager1, manager2), true},
                new Object[]{List.of(developer1, developer2, developer3, developer4), false},
                new Object[]{List.of(developer1, developer2, intern), false},
                new Object[]{List.of(developer1, developer2), false},
                new Object[]{List.of(manager1, manager2, intern), false},
                new Object[]{List.of(manager1, manager2), false},
                new Object[]{List.of(manager1), false},
                new Object[]{List.of(developer1), false},
                new Object[]{List.of(intern), false}
        );
    }

    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("validTeamCompositionData")
    void shouldCreataValidTeam(List<Employee> teamMembers) {
        TeamService teamService = new TeamService();

        teamService.createTeam("Team A",  teamMembers);

        List<Employee> createdTeamMembers = teamService.getTeamMembers("Team A");
        assertThat(createdTeamMembers).containsExactlyInAnyOrderElementsOf(teamMembers);
    }
    static Stream<Object[]> validTeamCompositionData() {
        Employee intern = new Employee("Krzysztof", "Nowicki", "krzysztof.nowicki@test.com", "Firma X", Position.STAZYSTA);
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, manager1)},
                new Object[]{List.of(developer1, developer2, manager1, intern)},
                new Object[]{List.of(developer1, developer2, developer3, developer4, manager1)},
                new Object[]{List.of(developer1, developer2, developer3, manager1, manager2)},
                new Object[]{List.of(developer1, developer2, intern, manager1, manager2)}
        );
    }

    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("invalidTeamCompositionData")
    void shouldThrowExceptionForInvalidTeamComposition(List<Employee> teamMembers) {
        TeamService teamService = new TeamService();

        assertThatThrownBy(() -> teamService.createTeam("Team A", teamMembers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Niepoprawny skład zespołu");
    }
    static Stream<Object[]> invalidTeamCompositionData() {
        Employee intern = new Employee("Krzysztof", "Nowicki", "krzysztof.nowicki@test.com","Firma X", Position.STAZYSTA);
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, developer3, developer4)},
                new Object[]{List.of(developer1, developer2, intern)},
                new Object[]{List.of(developer1, developer2)},
                new Object[]{List.of(manager1, manager2, intern)},
                new Object[]{List.of(manager1, manager2)},
                new Object[]{List.of(manager1)},
                new Object[]{List.of(developer1)},
                new Object[]{List.of(intern)}
        );
    }

    @Test
    void shouldThrowExceptionWhenTeamSizeTooBig() {
        TeamService teamService = new TeamService();

        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        assertThatThrownBy(() -> teamService.createTeam("Team A", List.of(developer1, developer2, developer3, developer4, manager1, manager2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldRemoveEmployeeFromTeam() {
        TeamService teamService = new TeamService();
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, developer2, manager));

        teamService.removeFromTeam("Team A", developer1);

        assertThat(teamService.getTeamMembers("Team A"))
                .containsExactly(manager);
    }

    @Test
    void shouldAddEmployeeToTeam() {
        TeamService teamService = new TeamService();

        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);

        teamService.createTeam("Team A", List.of(developer1, manager));

        teamService.addToTeam("Team A", developer2);

        assertThat(teamService.getTeamMembers("Team A"))
                .containsExactlyInAnyOrder(developer1, manager, developer2);
    }
}
