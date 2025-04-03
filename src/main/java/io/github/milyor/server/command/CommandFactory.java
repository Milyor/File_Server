package io.github.milyor.server.command;

import io.github.milyor.server.storage.FileManager;

import java.util.Map;

@FunctionalInterface
public interface CommandFactory {
    Command create(String identifier, FileManager fileManager, Map<String, Object> extraParams);
}
