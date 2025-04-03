package io.github.milyor.server.command;

import io.github.milyor.server.storage.FileManager;

import java.io.IOException;
import java.util.Map;

public class PutFileCommand implements Command {
    String fileName;
    byte[] fileContent;
    FileManager fileManager;

    public PutFileCommand(String fileName, byte[] fileContent, FileManager fileManager) {
        this.fileManager = fileManager;
        this.fileContent = fileContent;
        this.fileName = fileName;
    }

    @Override
    public Map<String, Object> execute() {
        try {
            return fileManager.putFile(fileName,fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Map.of("error", "403");
    }
}
