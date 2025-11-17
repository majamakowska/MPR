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
        if (percent < 0) {
            throw new IllegalArgumentException("Procent podwyżki nie może być ujemny");
        }
        if (percent == 0) {
            throw new IllegalArgumentException("Procent podwyżki nie może być 0");
        }
        double newSalary = employee.getSalary() * (1 + percent / 100);
        newSalary = Math.round(newSalary * 100.0) / 100.0;


        Position employeePosition = employee.getPosition();
        if (employeePosition != Position.PREZES) {
            double maxSalary = Position.values()[employeePosition.ordinal() - 1].getBaseSalary();
            if (newSalary >= maxSalary) {
                throw new IllegalArgumentException("Pensja przekracza limit dla stanowiska " + employeePosition);
            }
        }
        employee.setSalary(newSalary);
    }
}