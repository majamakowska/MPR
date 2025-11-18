package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

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
}