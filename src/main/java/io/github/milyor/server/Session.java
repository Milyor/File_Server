package io.github.milyor.server;

import io.github.milyor.server.net.DispatcherFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Session extends Thread {
    private final Socket socket;
    private final DispatcherFactory dispatcherFactory;

    public Session(Socket socketForClient, DispatcherFactory dispatcherFactory) {
        this.socket = socketForClient;
        this.dispatcherFactory = dispatcherFactory;
    }

    public void run() {
        try (
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())
        ) {
            Runnable dispatcher = dispatcherFactory.create(socket, input, output);
            Thread client = new Thread(dispatcher);
            client.start();
            client.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
