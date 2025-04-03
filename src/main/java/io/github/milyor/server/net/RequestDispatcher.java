package io.github.milyor.server.net;

import io.github.milyor.server.Server;
import io.github.milyor.server.command.Command;
import io.github.milyor.server.command.CommandExecutor;
import io.github.milyor.server.command.CommandFactory;
import io.github.milyor.server.command.CommandRegistry;
import io.github.milyor.server.storage.FileManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestDispatcher implements Runnable {
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Map<String, CommandFactory> commandMap = CommandRegistry.COMMAND_MAP;
    FileManager fileManager = new FileManager();
    CommandExecutor commandExecutor = new CommandExecutor();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public RequestDispatcher(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    @Override
    public void run() {
        try {
            Map<String, Object> receivedData = readClientRequest();
            if (receivedData.get("action").equals("EXIT")) {
                Server.shutdownServer();
                return;
            }
            Map<String, Object> response = processRequest(receivedData);
            sendResponse(response);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> processRequest(Map<String, Object> receivedData) {
        String action = (String) receivedData.get("action");
        String identifier;

        // Check if the key exist filename or id
        if (receivedData.containsKey("filename")) {
            identifier = (String) receivedData.get("filename");
        } else {
            identifier = (String) receivedData.get("id");
        }

        if (action.equals("PUT")) {
            identifier = (String) receivedData.get("newName");
            if (identifier.equals("no")) {
                identifier = generateName((String) receivedData.get("filename"));
            }
        }

        CommandFactory factory = commandMap.get(action);

        if (factory == null) {
            return Map.of("status", "ERROR", "message", "Invalid action" + action);
        }
        Command command = factory.create(identifier, fileManager, receivedData);

        Future<Map<String, Object>> futureResult = executorService.submit(() -> commandExecutor.execute(command));
        try {
            return futureResult.get();
        } catch (Exception e) {
            return Map.of("status", "ERROR", "message", "Error processing command: " + e.getMessage());
        }

    }


    private void sendResponse(Map<String, Object> response) throws InterruptedException {
        if (response.isEmpty()) {
            System.exit(0);
        }
        Thread responseSender = new Thread(new ResponseSender(outputStream, response));
        responseSender.start();
        responseSender.join();
    }

    private Map<String, Object> readClientRequest() throws IOException, ClassNotFoundException {
        return (Map<String, Object>) inputStream.readObject();

    }

    private String generateName(String filename) {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Pattern pattern = Pattern.compile("\\.(\\w+)$");
        Matcher matcher = pattern.matcher(filename);

        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randIndex = rand.nextInt(candidateChars.length());
            res.append(candidateChars.charAt(randIndex));
        }
        if (matcher.find()) {
            return  res.toString() + "." + matcher.group(1);
        }
        return res.toString();
    }

}
