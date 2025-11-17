package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;

public class PromotionService {
    public PromotionService() {}

    public void promote(Employee employee, Position newPosition) {
        if (employee.getPosition() == newPosition) {
            throw new IllegalArgumentException(
                    "Pracownik już ma stanowisko " + newPosition);
        }
        if (!canPromote(employee.getPosition(), newPosition)) {
            throw new IllegalArgumentException(
                    "Nie można awansować z " + employee.getPosition() + " na " + newPosition);
        }

        employee.setPosition(newPosition);
        employee.setSalary(newPosition.getBaseSalary());
    }

    private boolean canPromote(Position currentPosition, Position newPosition) {
        return newPosition.ordinal() < currentPosition.ordinal();
    }

    public void giveRaise(Employee employee, double percent) {
        employee.setSalary(employee.getSalary() * (1 + percent/100));
    }
}