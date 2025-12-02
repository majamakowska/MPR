package org.example.testdoubles;

import org.example.model.Employee;
import org.example.ports.Calendar;

import java.util.List;

/** Stub kalendarza: zawsze zwraca przekazaną mu wcześniej listę pracowników.*/

public class CalendarStub implements Calendar {
    private final List<Employee> available;

    public CalendarStub(List<Employee> available){
        this.available = available;
    }

    @Override
    public List<Employee> findAvailableEmployees(){
        return available;
    }
}