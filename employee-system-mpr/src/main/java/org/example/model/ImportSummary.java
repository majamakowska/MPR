package org.example.model;

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
        return "Zaimportowano " + importedCount + " pracowników\nBłędy:\n" + String.join("\n", errors);
    }
}