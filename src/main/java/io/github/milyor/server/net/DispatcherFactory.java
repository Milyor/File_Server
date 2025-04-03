package io.github.milyor.server.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@FunctionalInterface
public interface DispatcherFactory {
    Runnable create(Socket socket, ObjectInputStream input, ObjectOutputStream output);
}
