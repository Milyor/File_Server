package io.github.milyor.server.command;

import java.util.Map;

public class CommandRegistry {
    public static final Map<String, CommandFactory> COMMAND_MAP = Map.of(
            "PUT", (fileName, fileManager, params) -> {
                byte[] content = (byte[]) params.get("content");
                return new PutFileCommand(fileName, content, fileManager);
            },
            "GET", (fileName, fileManager, params) -> {
                if (params.containsKey("id")) {
                    return new GetFileByIdCommand((String) params.get("id"), fileManager);
                } else {
                    return new GetFileCommand((String) params.get("filename"), fileManager);
                }
            },
            "DELETE", (fileName, fileManager, params) -> {
                if (params.containsKey("id")) {
                    return new DeleteFileByIdCommand((String) params.get("id"), fileManager);
                }
                return new DeleteFileCommand((String) params.get("filename"), fileManager);
            }
    );
}
