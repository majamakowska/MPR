package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;

public class PromotionService {
    public PromotionService() {}

    public void promote(Employee employee, Position newPosition) {
        employee.setPosition(newPosition);
    }
}