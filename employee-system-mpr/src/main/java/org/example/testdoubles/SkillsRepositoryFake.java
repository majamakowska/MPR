package org.example.testdoubles;

import org.example.model.Employee;
import org.example.ports.SkillsRepository;

import java.util.Map;
import java.util.Set;

/** Fake: przechowuje w pamięci przekazaną mu listę umiejętności pracowników i zwraca pracownika z odpowiednimi kompetencjami.*/


public class SkillsRepositoryFake implements SkillsRepository {
    private final Map<Employee, Set<String>> skillsMap;

    public SkillsRepositoryFake(Map<Employee, Set<String>> skillsMap){
        this.skillsMap = skillsMap;
    }

    @Override
    public Set<String> findSkillsFor(Employee e) {
        return skillsMap.getOrDefault(e, Set.of());
    }
}