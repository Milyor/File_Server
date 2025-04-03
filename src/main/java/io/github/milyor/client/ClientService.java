package io.github.milyor.client;

import io.github.milyor.client.io.ResponseHandler;
import io.github.milyor.client.io.SendInfo;
import io.github.milyor.client.ui.UserInputHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ClientService {
    private final UserInputHandler inputHandler;
    private final SendInfo sendInfo;
    private final ResponseHandler responseHandler;
    private final String serverAddress;
    private final int serverPort;

    ClientService(UserInputHandler inputHandler, String serverAddress, int serverPort) {
        this.inputHandler = inputHandler;
        this.sendInfo =  new SendInfo();
        this.responseHandler = new ResponseHandler();
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
    }
    public void runService() {
        Map<String, Object> requestData = inputHandler.getFileManagementInput();
        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            sendInfo.sendRequest(output, requestData);
            responseHandler.response(input, requestData);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
