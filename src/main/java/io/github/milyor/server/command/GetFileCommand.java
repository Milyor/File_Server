package io.github.milyor.server.command;

import io.github.milyor.server.storage.FileManager;

import java.util.Map;

public class GetFileCommand implements Command {
    String fileName;
    FileManager fileManager;

    public GetFileCommand(String fileName, FileManager fileManager) {
        this.fileManager = fileManager;
        this.fileName = fileName;
    }

    @Override
    public Map<String, Object> execute() {
        return fileManager.getFile(fileName);
    }
}
