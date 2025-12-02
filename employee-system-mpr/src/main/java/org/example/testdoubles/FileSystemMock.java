package org.example.testdoubles;

import org.example.ports.FileSystem;

import java.util.ArrayList;
import java.util.List;

/** Mock: pozwala ustawić oczekiwaną ścieżkę i trybu zapisu i zweryfikować czy wywołanie writeFile() miało te parametry.*/

public class FileSystemMock implements FileSystem {

    public static class WriteOperation {
        public final String path;
        public final String content;
        public final boolean overwrite;

        public WriteOperation(String path, String content, boolean overwrite){
            this.path = path;
            this.content = content;
            this.overwrite = overwrite;
        }
    }

    private final List<WriteOperation> writes = new ArrayList<>();
    private String expectedPath;
    private Boolean expectedOverwrite;

    public void expectWrite(String path, Boolean overwrite){
        this.expectedPath = path;
        this.expectedOverwrite = overwrite;
    }

    @Override
    public void writeFile(String path, String content, boolean overwrite){
        writes.add(new WriteOperation(path, content, overwrite));
    }

    public void verify(){
        if(expectedPath != null) {
            if(writes.isEmpty()) throw new AssertionError("Expected writes, but no writes found");
            WriteOperation writeOperation = writes.get(0);
            if(!writeOperation.path.equals(expectedPath)) {
                throw new AssertionError("Expected: " + expectedPath + "\nActual: " + writeOperation.path);
            }
            if (expectedOverwrite != null && writeOperation.overwrite != expectedOverwrite) {
                throw new AssertionError("Expected: " + expectedOverwrite + "\nActual: " + writeOperation.overwrite);
            }
        }
    }
}