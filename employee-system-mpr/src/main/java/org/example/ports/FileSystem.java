package org.example.ports;

public interface FileSystem {
    void writeFile(String path, String content, boolean overwrite);
}