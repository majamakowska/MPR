package org.example.testdoubles;

import org.example.model.Employee;
import org.example.ports.Formatter;

import java.util.List;

/** Stub: zawsze zwraca przekazaną mu z góry wartość.*/

public class FormatterStub implements Formatter {
    private final String formatted;

    public FormatterStub(String formatted){
        this.formatted = formatted;
    }

    @Override
    public String formatEmployeesAs(String format, List<Employee> employees) {
        return formatted;
    }
}
