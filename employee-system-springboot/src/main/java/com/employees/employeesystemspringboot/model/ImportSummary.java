package com.employees.employeesystemspringboot.model;

import java.util.List;

public class ImportSummary {
    private int importedCount;
    private List<String> errors;

    public ImportSummary(int importedCount, List<String> errors) {
        this.importedCount = importedCount;
        this.errors = errors;
    }

    @Override
    public String toString() {
        String errorText = errors.isEmpty() ? "brak" : String.join("\n", errors);
        return "Zaimportowano " + importedCount + " pracowników.\nBłędy:\n" + errorText;
    }
}