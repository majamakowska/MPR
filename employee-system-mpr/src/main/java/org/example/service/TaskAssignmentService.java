package org.example.service;

import org.example.model.Employee;
import org.example.model.Task;
import org.example.ports.AllocationRepository;
import org.example.ports.Calendar;
import org.example.ports.SkillsRepository;
import org.example.testdoubles.ConfigDummy;

import java.util.List;
import java.util.Set;

public class TaskAssignmentService {
    private final Calendar calendar;
    private final SkillsRepository skillsRepository;
    private final AllocationRepository allocationRepository;
    private final ConfigDummy configDummy;

    public TaskAssignmentService(Calendar calendar, SkillsRepository skillsRepo, AllocationRepository allocationRepo, ConfigDummy configDummy) {
        this.calendar = calendar;
        this.skillsRepository = skillsRepo;
        this.allocationRepository = allocationRepo;
        this.configDummy = configDummy;
    }

    public Employee assign(Task task){
        List<Employee> available = calendar.findAvailableEmployees();
        for(Employee e : available){
            Set<String> skills = skillsRepository.findSkillsFor(e);
            if(skills.containsAll(task.getRequiredSkills())){
                allocationRepository.recordAssignment(task, e);
                return e;
            }
        }
        return null;
    }
}