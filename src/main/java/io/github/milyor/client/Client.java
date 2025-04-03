package io.github.milyor.client;

import io.github.milyor.client.ui.UserInputHandler;

public class Client {
    public static void main(String[] args) {
        UserInputHandler inputHandler = new UserInputHandler();
        ClientService client = new ClientService(inputHandler, "127.0.0.1", 23456);
        client.runService();
    }
}
