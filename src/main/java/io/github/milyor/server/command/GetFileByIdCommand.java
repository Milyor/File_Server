package io.github.milyor.server.command;

import io.github.milyor.server.storage.FileManager;

import java.util.Map;

public class GetFileByIdCommand implements Command {
    String id;
    FileManager fileManager;

    public GetFileByIdCommand(String id, FileManager fileManager) {
        this.id = id;
        this.fileManager = fileManager;
    }

    @Override
    public Map<String, Object> execute() {
        return fileManager.getFileById(id);
    }
}
