package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class EmployeeRatingServiceTest {
    @Test
    void shouldAddRatingForEmployee() {
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        ratingService.addRating(employee, 4);
        List<Integer> ratings = ratingService.getRatings(employee);

        assertThat(ratings).containsExactly(4);
    }
    @Test
    void shouldAddMultipleRatingsForEmployee() {
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        ratingService.addRating(employee, 4);
        ratingService.addRating(employee, 5);
        ratingService.addRating(employee, 1);
        List<Integer> ratings = ratingService.getRatings(employee);

        assertThat(ratings).containsExactly(4, 5, 1);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, 10, -1})
    void shouldThrowExceptionForInvalidRating(int invalidRating) {
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        assertThatThrownBy(() -> ratingService.addRating(employee, invalidRating))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ocena musi być w przedziale 1-5");
    }

    @Test
    void shouldReturnEmptyListWhenNoRatings() {
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

        List<Integer> ratings = ratingService.getRatings(employee);

        assertThat(ratings).isEmpty();
    }

    @ParameterizedTest(name = "ratings={0} => expectedAvg={1}")
    @MethodSource("ratingData")
    void shouldCalculateAverageRating(List<Integer> ratings, double expectedAvg) {
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee = new Employee("Anna", "Nowak", "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);

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
        EmployeeRatingService ratingService = new EmployeeRatingService();
        Employee employee1 = new Employee("Anna", "Nowak",
                "anna.nowak@test.com", "Firma X", Position.PROGRAMISTA);
        Employee employee2 = new Employee("Józef", "K",
                "jozef.k@test.com", "Firma Y", Position.PROGRAMISTA);

        ratingService.addRating(employee1, 3);
        ratingService.addRating(employee1, 4);
        ratingService.addRating(employee2, 5);
        ratingService.addRating(employee2, 5);

        List<Employee> bestEmployees = ratingService.getBestEmployees();

        assertThat(bestEmployees)
                .containsExactly(employee2);
    }
}