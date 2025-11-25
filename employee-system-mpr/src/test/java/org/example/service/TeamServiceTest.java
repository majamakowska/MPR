package org.example.service;

import org.assertj.core.api.SoftAssertions;
import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class TeamServiceTest {

    TeamService teamService;

    @BeforeEach
    void setUp() {
        teamService = new TeamService();
    }

    private static Employee createEmployee(String firstName, String lastName, Position position) {
        return new Employee(firstName, lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@test.com",
                "Firma X", position);
    }

    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("teamCompositionData")
    void shouldValidateTeamComposition(List<Employee> teamMembers, boolean expectedValid) {
        boolean isTeamCompositionValid = teamService.isTeamCompositionValid(teamMembers);

        if (expectedValid) {
            assertThat(isTeamCompositionValid).isTrue();
        } else {
            assertThat(isTeamCompositionValid).isFalse();
        }

    }
    static Stream<Object[]> teamCompositionData() {
        Employee intern = createEmployee("Krzysztof", "Nowicki", Position.STAZYSTA);
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, manager1), true},
                new Object[]{List.of(developer1, developer2, developer3, manager1), true},
                new Object[]{List.of(developer1, developer2, manager1, intern), true},
                new Object[]{List.of(developer1, developer2, developer3, intern, manager1), true},
                new Object[]{List.of(developer1, developer2, developer3, developer4, manager1), true},

                new Object[]{List.of(developer1, developer2, developer3, manager1, manager2), false},
                new Object[]{List.of(developer1, developer2, intern, manager1, manager2), false},

                new Object[]{List.of(developer1, developer2, developer3, developer4), false},
                new Object[]{List.of(developer1, developer2, intern), false},
                new Object[]{List.of(developer1, developer2), false},

                new Object[]{List.of(manager1), false},
                new Object[]{List.of(manager1, intern), false},

                new Object[]{List.of(intern), false},
                new Object[]{List.of(developer1), false}
        );
    }

    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("validTeamCompositionData")
    void shouldCreateValidTeam(List<Employee> teamMembers) {
        teamService.createTeam("Team A",  teamMembers);

        List<Employee> createdTeamMembers = teamService.getTeamMembers("Team A");

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(createdTeamMembers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(teamMembers.size())
                .containsExactlyInAnyOrderElementsOf(teamMembers);
        softly.assertThat(createdTeamMembers)
                .extracting(Employee::getPosition)
                .containsAnyOf(Position.MANAGER, Position.PROGRAMISTA);
        softly.assertThat(createdTeamMembers)
                .usingRecursiveComparison()
                .isEqualTo(teamMembers);

        softly.assertAll();
    }
    static Stream<Object[]> validTeamCompositionData() {
        Employee intern = createEmployee("Krzysztof", "Nowicki", Position.STAZYSTA);
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, manager)},
                new Object[]{List.of(developer1, developer2, developer3, manager)},
                new Object[]{List.of(developer1, developer2, manager, intern)},
                new Object[]{List.of(developer1, developer2, developer3, intern, manager)},
                new Object[]{List.of(developer1, developer2, developer3, developer4, manager)}
        );
    }

    @ParameterizedTest(name = "teamMembers={0} => isValid={1}")
    @MethodSource("invalidTeamCompositionData")
    void shouldThrowExceptionForInvalidTeamComposition(List<Employee> teamMembers) {
        assertThatThrownBy(() -> teamService.createTeam("Team A", teamMembers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Niepoprawny skład zespołu");
    }
    static Stream<Object[]> invalidTeamCompositionData() {
        Employee intern = createEmployee("Krzysztof", "Nowicki", Position.STAZYSTA);
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        return Stream.of(
                new Object[]{List.of(developer1, developer2, developer3, manager1, manager2)},
                new Object[]{List.of(developer1, developer2, intern, manager1, manager2)},

                new Object[]{List.of(developer1, developer2, developer3, developer4)},
                new Object[]{List.of(developer1, developer2, intern)},
                new Object[]{List.of(developer1, developer2)},

                new Object[]{List.of(manager1)},
                new Object[]{List.of(manager1, intern)},

                new Object[]{List.of(intern)},
                new Object[]{List.of(developer1)}
        );
    }

    @Test
    void shouldThrowExceptionWhenTeamSizeTooBig() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        assertThatThrownBy(() -> teamService.createTeam("Team A", List.of(developer1, developer2, developer3, developer4, manager1, manager2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldRemoveEmployeeFromTeam() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, developer2, manager));

        teamService.removeFromTeam("Team A", developer1);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(teamService.getTeamMembers("Team A")).isNotNull().isNotEmpty().hasSize(2);
        softly.assertThat(teamService.getTeamMembers("Team A"))
                .containsExactlyInAnyOrder(manager,developer2). doesNotContain(developer1);
        softly.assertAll();
    }

    @Test
    void shouldThrowWhenRemovingFromNonExistingTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        assertThatThrownBy(() -> teamService.removeFromTeam("Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenRemovingNonTeamMemberFromTeam() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager));

        assertThatThrownBy(() -> teamService.removeFromTeam("Team A", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie znajduje się w zespole");
    }

    @Test
    void shouldAddEmployeeToTeam() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager));

        teamService.addToTeam("Team A", developer2);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(teamService.getTeamMembers("Team A")).isNotNull().isNotEmpty().hasSize(3);
        softly.assertThat(teamService.getTeamMembers("Team A"))
                .containsExactlyInAnyOrder(developer1, manager, developer2);
        softly.assertAll();
    }

    @Test
    void shouldThrowWhenAddingToNonExistingTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        assertThatThrownBy(() -> teamService.addToTeam("Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenAddingEmployeeExceedsTeamSize() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, developer2, developer3, developer4, manager1));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", manager2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldThrowWhenAddingEmployeeAlreadyInTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik jest już w zespole");
    }

    @Test
    void shouldTransferEmployeeBetweenTeams() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

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
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team B", List.of(developer1, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team Nothing", "Team B", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenTransferringToNonExistingTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team Nothing", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zespół nie istnieje");
    }

    @Test
    void shouldThrowWhenTransferringNonMemberEmployee() {
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer1, manager1));
        teamService.createTeam("Team B", List.of(developer2, manager2));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie znajduje się w zespole");
    }
    @Test
    void shouldThrowWhenTransferringEmployeeExceedsTargetTeamSize() {
        Employee intern = createEmployee("Krzysztof", "Nowicki", Position.STAZYSTA);
        Employee developer1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee developer2 = createEmployee("Barbara", "Sosna", Position.PROGRAMISTA);
        Employee developer3 = createEmployee("Zuzanna", "Sobota", Position.PROGRAMISTA);
        Employee developer4 = createEmployee("Tomasz", "Problem", Position.PROGRAMISTA);
        Employee manager1 = createEmployee("Piotr", "Kowal", Position.MANAGER);
        Employee manager2 = createEmployee("Jan", "Dudek", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer4, manager1));
        teamService.createTeam("Team B", List.of(intern, developer1, developer2, developer3, manager2));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Za duży rozmiar zespołu");
    }

    @Test
    void shouldThrowWhenTransferringEmployeeWhoIsAlreadyInTargetTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(developer, manager));
        teamService.createTeam("Team B", List.of(developer, manager));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", developer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik jest już w zespole");
    }

    @Test
    void shouldThrowWhenCreatingTeamWithNullMembers() {
        assertThatThrownBy(() -> teamService.createTeam("Team Null", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lista członków nie może być null");
    }

    @Test
    void shouldThrowWhenAddingNullEmployeeToTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.addToTeam("Team A", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }

    @Test
    void shouldThrowWhenRemovingNullEmployeeFromTeam() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.removeFromTeam("Team A", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }

    @Test
    void shouldThrowWhenTransferringNullEmployee() {
        Employee developer = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee manager = createEmployee("Piotr", "Kowal", Position.MANAGER);

        teamService.createTeam("Team A", List.of(manager, developer));
        teamService.createTeam("Team B", List.of(manager, developer));

        assertThatThrownBy(() -> teamService.transferEmployee("Team A", "Team B", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pracownik nie może być null");
    }
}
