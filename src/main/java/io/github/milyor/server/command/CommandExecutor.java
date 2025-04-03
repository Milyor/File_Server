package io.github.milyor.server.command;

import java.util.Map;

public class CommandExecutor {
    public Map<String, Object> execute(Command command) {
        return command.execute();
    }
}
