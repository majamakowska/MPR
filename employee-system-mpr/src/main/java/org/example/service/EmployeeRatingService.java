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

    public double getAverageRating(Employee employee) {
        List<Integer> ratings = employeeRatings.get(employee);
        double sum = ratings.stream().mapToDouble(Integer::doubleValue).sum();
        return sum/ratings.size();
    }

    public List<Employee> getBestEmployees() {
        Map<Employee, Double> employeeAverages = new HashMap<>();
        employeeRatings.forEach((employee, rates) ->
                employeeAverages.put(employee, getAverageRating(employee)));

        double maxAverage = employeeAverages.values().stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);

        return employeeAverages.entrySet().stream()
                .filter(e -> e.getValue() == maxAverage)
                .map(Map.Entry::getKey)
                .toList();
    }
}