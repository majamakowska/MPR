package org.example.model;

import java.time.Duration;
import java.util.Set;

public class Task {
    private String taskId;
    private Set<String> requiredSkills;
    private Duration estimatedDuration;
    public Task (String taskId, Set<String> requiredSkills, Duration estimatedDuration) {
        this.taskId = taskId;
        this.requiredSkills = requiredSkills;
        this.estimatedDuration = estimatedDuration;
    }
    public String getTaskId(){ return taskId; }
    public Set<String> getRequiredSkills(){ return requiredSkills; }
    public Duration getEstimatedDuration(){ return estimatedDuration; }
}