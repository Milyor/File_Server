package io.github.milyor.client.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Scanner;

public class ResponseHandler {
    public void response(ObjectInputStream inputStream, Map<String, Object> request) {
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandle = new FileHandler();
        try {
            if (request.get("action").equals("EXIT")) {
                return;
            }
            Map<String, Object> answer = (Map<String, Object>) inputStream.readObject();
            String message = (String) answer.get("message");
            if (request.get("action").equals("GET")) {
                if (answer.get("code").equals("403")) {
                    System.out.println("The response says that " + message);
                    return;
                }
                System.out.print("The file was downloaded! Specify a name for it: ");
                String name = scanner.nextLine();
                byte[] content = (byte[]) answer.get("content");
                fileHandle.readByteArrayToFile(content, name);
                System.out.println("File saved on hard drive!");
                return;
            } else if (request.get("action").equals("DELETE")) {
                System.out.println("The response says that " + message);
                return;

            }
            System.out.println("Response says that " + message);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
