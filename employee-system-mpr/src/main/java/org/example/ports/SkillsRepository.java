package org.example.ports;

import org.example.model.Employee;

import java.util.Set;

public interface SkillsRepository {
    Set<String> findSkillsFor(Employee e);
}