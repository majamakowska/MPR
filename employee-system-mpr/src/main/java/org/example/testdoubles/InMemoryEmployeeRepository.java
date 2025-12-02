package org.example.testdoubles;

import org.example.model.Employee;
import org.example.ports.EmployeeRepository;

import java.util.List;

/** Fake: przechowuje w pamięci przekazaną mu listy pracowników i zwraca ją.*/

public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final List<Employee> employees;

    public InMemoryEmployeeRepository(List<Employee> employees){
        this.employees = employees;
    }

    @Override
    public List<Employee> findAll(){ return employees; }
}