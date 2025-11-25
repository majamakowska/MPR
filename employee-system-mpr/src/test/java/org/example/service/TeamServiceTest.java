package org.example.service;

import org.assertj.core.api.SoftAssertions;
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
                .containsExactlyInAnyOrder(manager,developer2);
    }

    @Test
    void shouldThrowWhenRemovingFromNonExistingTeam() {
        TeamService teamService = new TeamService();
        Employee developer = new Employee("Anna", "Nowak", "anna@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> teamService.removeFromTeam("Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenRemovingNonTeamMemberFromTeam() {
        TeamService teamService = new TeamService();
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager));

        assertThatThrownBy(() -> teamService.removeFromTeam("Team A", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie znajduje się w zespole");
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

    @Test
    void shouldThrowWhenAddingToNonExistingTeam() {
        TeamService teamService = new TeamService();
        Employee developer = new Employee("Anna", "Nowak", "anna@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> teamService.addToTeam("Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenAddingEmployeeExceedsTeamSize() {
        TeamService teamService = new TeamService();
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, developer2, developer3, developer4, manager1));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", manager2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldThrowWhenAddingEmployeeAlreadyInTeam() {
        TeamService teamService = new TeamService();
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik jest już w zespole");
    }

    @Test
    void shouldTransferEmployeeBetweenTeams() {
        TeamService teamService = new TeamService();
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager1));
        teamService.createTeam("Team B", List.of(developer2, manager2));

        teamService.transferEmployee("Team A", "Team B", developer1);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(teamService.getTeamMembers("Team A"))
                .containsExactly(manager1);
        softly.assertThat(teamService.getTeamMembers("Team B"))
                .containsExactlyInAnyOrder(developer1, developer2, manager2);
        softly.assertAll();
    }

    @Test
    void shouldThrowWhenTransferringFromNonExistingTeam() {
        TeamService teamService = new TeamService();
        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team B", List.of(developer1, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team Nothing", "Team B", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenTransferringToNonExistingTeam() {
        TeamService teamService = new TeamService();
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenTransferringNonMemberEmployee() {
        TeamService teamService = new TeamService();

        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager1));
        teamService.createTeam("Team B", List.of(developer2, manager2));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie znajduje się w zespole");
    }
    @Test
    void shouldThrowWhenTransferringEmployeeExceedsTargetTeamSize() {
        TeamService teamService = new TeamService();

        Employee developer1 = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer2 = new Employee("Barbara", "Sosna", "barbara.sosna@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer3 = new Employee("Zuzanna", "Sobota", "zuzanna.sobota@test.com", "Firma X", Position.PROGRAMISTA);
        Employee developer4 = new Employee("Tomasz", "Problem", "tomasz.problem@test.com", "Firma X", Position.PROGRAMISTA);
        Employee manager1 = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee manager2 = new Employee("Piotr", "Nowak", "piotr.nowak@test.com", "Firma X", Position.MANAGER);

        Employee sourceDev = new Employee("X", "X", "x@test.com", "Firma X", Position.PROGRAMISTA);

        teamService.createTeam("Team A", List.of(developer1, manager1));
        teamService.createTeam("Team B", List.of(developer1, developer2, developer3, developer4, manager2));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldThrowWhenTransferringEmployeeWhoIsAlreadyInTargetTeam() {
        TeamService teamService = new TeamService();
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);;
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));
        teamService.createTeam("Team B", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik jest już w zespole");
    }

    @Test
    void shouldThrowWhenCreatingTeamWithNullMembers() {
        TeamService teamService = new TeamService();

        assertThatThrownBy(() -> teamService.createTeam("Team Null", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lista członków nie może być null");
    }

    @Test
    void shouldThrowWhenAddingNullEmployeeToTeam() {
        TeamService teamService = new TeamService();
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        teamService.createTeam("Team A", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }

    @Test
    void shouldThrowWhenRemovingNullEmployeeFromTeam() {
        TeamService teamService = new TeamService();
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        teamService.createTeam("Team A", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.removeFromTeam("Team A", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }

    @Test
    void shouldThrowWhenTransferringNullEmployee() {
        TeamService teamService = new TeamService();
        Employee manager = new Employee("Jan", "Kowalski", "jan.kowalski@test.com", "Firma X", Position.MANAGER);
        Employee developer = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        teamService.createTeam("Team A", List.of(manager, developer));
        teamService.createTeam("Team B", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }
}
