package org.example.testdoubles;

import org.example.model.Employee;
import org.example.model.Task;
import org.example.ports.AllocationRepository;

import java.util.ArrayList;
import java.util.List;

/** Spy: rejestrujący wszystkie operacje przypisania pracownika do zadania.*/

public class AllocationRepositorySpy implements AllocationRepository {

    public static class Assignment {
        public final Task task;
        public final Employee employee;

        public Assignment(Task task, Employee employee){
            this.task = task;
            this.employee = employee;
        }
    }

    private final List<Assignment> assignments = new ArrayList<>();

    @Override
    public void recordAssignment(Task task, Employee employee){
        assignments.add(new Assignment(task, employee));
    }
    public List<Assignment> getAssignments(){
        return List.copyOf(assignments);
    }
}