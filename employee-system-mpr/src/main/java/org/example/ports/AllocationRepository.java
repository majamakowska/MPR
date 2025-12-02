package org.example.ports;

import org.example.model.Employee;
import org.example.model.Task;

public interface AllocationRepository {
    void recordAssignment(Task task, Employee employee);
}
