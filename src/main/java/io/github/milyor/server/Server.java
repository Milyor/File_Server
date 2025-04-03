package io.github.milyor.server;
import io.github.milyor.server.net.RequestDispatcher;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final int PORT = 23456;
    private static volatile boolean isRunning = true;

    public static void main(String[] args) {

        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (isRunning) {
                Session session = new Session(server.accept(), RequestDispatcher::new);
                session.start();
                session.join();

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static void shutdownServer() {
        isRunning = false;
    }
}
