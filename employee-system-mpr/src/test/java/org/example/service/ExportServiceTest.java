package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.testdoubles.FileSystemMock;
import org.example.testdoubles.FileSystemSpy;
import org.example.testdoubles.FormatterStub;
import org.example.testdoubles.InMemoryEmployeeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExportServiceTest {

    Employee createEmployee(String firstName) {
        return new Employee(firstName, "Test", firstName.toLowerCase()+"@test", "Firma X", Position.PROGRAMISTA);
    }

    /** Używa:
     * - InMemoryEmployeeRepository (fake), który przechowuje w pamięci i zwraca wcześniej podaną mu listę pracowników.
     * - FormatterStub, który zawsze zwraca przekazaną mu z góry wartość ("CSV"),
     * - FileSystemSpy, rejestrującego wszystkie operacje zapisu, pozwalając sprawdzić ścieżkę i zawartość zapisanych danych
     *
     *  Test sprawdza czy:
     *  - ExportService wykonał dokładnie jeden zapis pliku,
     *  - zapisał właściwą ścieżkę,
     *  - zapisał jako treść wynik formattera */
    @Test
    void shouldExportFormattedContent() {
        Employee a = createEmployee("Anna");
        InMemoryEmployeeRepository repository = new InMemoryEmployeeRepository(List.of(a));
        FormatterStub formatter = new FormatterStub("CSV");
        FileSystemSpy fileSystemSpy = new FileSystemSpy();

        ExportService service = new ExportService(repository, formatter, fileSystemSpy);
        service.export("csv", "/out/employees.csv", true);

        assertEquals(1, fileSystemSpy.getWrites().size());
        assertEquals("/out/employees.csv", fileSystemSpy.getWrites().get(0).path);
        assertEquals("CSV", fileSystemSpy.getWrites().get(0).content);
    }

    /** Używa:
     * - InMemoryEmployeeRepository,
     * - FormatterStub, który zawsze zwraca "JSON",
     * - FileSystemMock weryfikujący czy wywołanie writeFile() miało oczekiwane parametry path i overwrite
     *
     *  Test sprawdza czy:
     * - ExportService wywołał operację zapisu dla poprawnie przekazanych parametrów (path = "/out/employees.json, overwrite = true) */
    @Test
    void shouldVerifyPathAndOverwriteSetTrue() {
        Employee a = createEmployee("Anna");
        InMemoryEmployeeRepository repository = new InMemoryEmployeeRepository(List.of(a));
        FormatterStub formatter = new FormatterStub("JSON");
        FileSystemMock fileSystemMock = new FileSystemMock();
        fileSystemMock.expectWrite("/out/employees.json", true);

        ExportService service = new ExportService(repository, formatter, fileSystemMock);
        service.export("json", "/out/employees.json", true);

        fileSystemMock.verify();
    }

    /** Używa:
     * - InMemoryEmployeeRepository,
     * - FormatterStub,
     * - FileSystemMock,
     *
     *  Test sprawdza czy:
     * - ExportService wywołał operację zapisu dla poprawnie przekazanych parametrów (path = "/out/employees.json, overwrite = false) */
    @Test
    void shouldVerifyPathAndOverwriteSetFalse() {
        Employee a = createEmployee("Anna");
        InMemoryEmployeeRepository repository = new InMemoryEmployeeRepository(List.of(a));
        FormatterStub formatter = new FormatterStub("JSON");
        FileSystemMock fileSystemMock = new FileSystemMock();
        fileSystemMock.expectWrite("/out/employees.json", false);

        ExportService service = new ExportService(repository, formatter, fileSystemMock);
        service.export("json", "/out/employees.json", false);

        fileSystemMock.verify();
    }
}
