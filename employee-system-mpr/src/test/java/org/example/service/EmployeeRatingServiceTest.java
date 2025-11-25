package org.example.service;

import org.assertj.core.api.SoftAssertions;
import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class EmployeeRatingServiceTest {

    EmployeeRatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new EmployeeRatingService();
    }

    private static Employee createEmployee(String firstName, String lastName, Position position) {
        return new Employee(firstName, lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@test.com",
                "Firma X", position);
    }

    @Test
    void shouldAddRatingForEmployee() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        ratingService.addRating(employee, 4);
        List<Integer> ratings = ratingService.getRatings(employee);

        assertThat(ratings).isNotEmpty().hasSize(1).
                containsExactly(4).allMatch(r -> r > 0 && r <= 5);
    }
    @Test
    void shouldAddMultipleRatingsForEmployee() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        ratingService.addRating(employee, 4);
        ratingService.addRating(employee, 5);
        ratingService.addRating(employee, 1);
        List<Integer> ratings = ratingService.getRatings(employee);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(ratings).hasSize(3);
        softly.assertThat(ratings).containsExactlyInAnyOrder(1, 4, 5);
        softly.assertThat(ratings).allMatch(r -> r > 0 && r <= 5);
        softly.assertAll();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, 10, -1})
    void shouldThrowExceptionForInvalidRating(int invalidRating) {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        assertThatThrownBy(() -> ratingService.addRating(employee, invalidRating))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ocena musi być w przedziale 1-5");
    }

    @Test
    void shouldReturnEmptyListWhenNoRatings() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        List<Integer> ratings = ratingService.getRatings(employee);

        assertThat(ratings).isEmpty();
    }

    @ParameterizedTest(name = "ratings={0} => expectedAvg={1}")
    @MethodSource("ratingData")
    void shouldCalculateAverageRating(List<Integer> ratings, double expectedAvg) {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        ratings.forEach(r -> ratingService.addRating(employee, r));

        double avg = ratingService.getAverageRating(employee);

        assertThat(avg).isCloseTo(expectedAvg, within(0.01));
    }
    static Stream<Arguments> ratingData() {
        return Stream.of(
                Arguments.of(List.of(5), 5.0),
                Arguments.of(List.of(3, 4, 5), 4.0),
                Arguments.of(List.of(1, 1, 1, 1), 1.0),
                Arguments.of(List.of(2, 3, 4, 5, 5), 3.8)
        );
    }

    @Test
    void shouldReturnEmployeesWithHighestAverage() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee employee2 = createEmployee("Piotr", "Kowal", Position.PROGRAMISTA);

        ratingService.addRating(employee1, 3);
        ratingService.addRating(employee1, 4);
        ratingService.addRating(employee2, 5);
        ratingService.addRating(employee2, 5);

        List<Employee> bestEmployees = ratingService.getBestEmployees();

        assertThat(bestEmployees).isNotEmpty().hasSize(1).containsExactly(employee2).doesNotContain(employee1);
    }

    @Test
    void shouldReturnZeroAverageWhenNoRatings() {
        Employee employee = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);

        double avg = ratingService.getAverageRating(employee);

        assertThat(avg).isEqualTo(0.0);
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployees() {
        List<Employee> best = ratingService.getBestEmployees();

        assertThat(best).isEmpty();
    }

    @Test
    void shouldReturnAllEmployeesWithSameHighestAverage() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee employee2 = createEmployee("Piotr", "Kowal", Position.PROGRAMISTA);
        Employee employee3 = createEmployee("Jan", "Dudek", Position.PROGRAMISTA);

        ratingService.addRating(employee1, 5);
        ratingService.addRating(employee1, 4);

        ratingService.addRating(employee2, 4);
        ratingService.addRating(employee2, 5);

        ratingService.addRating(employee3, 3);
        ratingService.addRating(employee3, 5);

        List<Employee> bestEmployees = ratingService.getBestEmployees();

        assertThat(bestEmployees).isNotEmpty().hasSize(2).
                containsExactlyInAnyOrder(employee1, employee2).doesNotContain(employee3);
    }

    @Test
    void shouldNotMixRatingsBetweenEmployees() {
        Employee employee1 = createEmployee("Anna", "Nowak", Position.PROGRAMISTA);
        Employee employee2 = createEmployee("Piotr", "Kowal", Position.PROGRAMISTA);

        ratingService.addRating(employee1, 3);
        ratingService.addRating(employee2, 5);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(ratingService.getRatings(employee1)).containsExactly(3);
        softly.assertThat(ratingService.getRatings(employee2)).containsExactly(5);
        softly.assertAll();
    }
}