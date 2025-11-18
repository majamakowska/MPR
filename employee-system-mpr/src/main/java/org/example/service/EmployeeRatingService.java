package org.example.service;

import org.example.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeRatingService {

    private final Map<Employee, List<Integer>> employeeRatings = new HashMap<>();

    public void addRating(Employee employee, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Ocena musi być w przedziale 1-5");
        }
        if (!employeeRatings.containsKey(employee)) {
            employeeRatings.put(employee, new ArrayList<>());
        }
        employeeRatings.get(employee).add(rating);
    }

    public List<Integer> getRatings(Employee employee) {
        return new ArrayList<>(employeeRatings.getOrDefault(employee, new ArrayList<>()));
    }
}