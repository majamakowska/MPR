package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamService {
    private static final int maxTeamSize = 5;

    private final Map<String, List<Employee>> teams = new HashMap<>();

    public void createTeam(String teamName, List<Employee> members) {
        if (members.size() > maxTeamSize) {
            throw new IllegalArgumentException("Za duży rozmiar zespołu");
        }
        if (!isTeamCompositionValid(members)) {
            throw new IllegalArgumentException("Niepoprawny skład zespołu");
        }
        teams.put(teamName, new ArrayList<>(members));
    }

    public boolean isTeamCompositionValid(List<Employee> members) {
        boolean hasManager = members.stream().anyMatch(e -> e.getPosition() == Position.MANAGER);
        boolean hasDeveloper = members.stream().anyMatch(e -> e.getPosition() == Position.PROGRAMISTA);
        return hasManager && hasDeveloper;
    }

    public List<Employee> getTeamMembers(String teamName) {
        List<Employee> members = teams.get(teamName);
        if (members == null) return new ArrayList<>();
        return new ArrayList<>(members);
    }

    public void removeFromTeam(String teamName, Employee employee) {
        List<Employee> teamMembers = teams.get(teamName);
        if (teamMembers != null) {
            teamMembers.remove(employee);
        }
    }

    public void addToTeam(String teamName, Employee employee) {
        List<Employee> teamMembers = teams.get(teamName);
        if (teamMembers != null) {
            teamMembers.add(employee);
        }
    }
}
