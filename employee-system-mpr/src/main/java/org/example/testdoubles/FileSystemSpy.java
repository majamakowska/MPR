package org.example.testdoubles;

import org.example.ports.FileSystem;

import java.util.ArrayList;
import java.util.List;

/** Spy: rejestruje wszystkie operacje zapisu, pozwalając sprawdzić ścieżkę i zawartość zapisanych danych.*/

public class FileSystemSpy implements FileSystem {

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

    @Override
    public void writeFile(String path, String content, boolean overwrite){
        writes.add(new WriteOperation(path, content, overwrite));
    }
    public List<WriteOperation> getWrites(){
        return List.copyOf(writes);
    }
}