package io.github.milyor.server.command;

import java.util.Map;

public interface Command {
    Map<String, Object> execute();
}
