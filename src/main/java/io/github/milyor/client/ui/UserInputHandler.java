package io.github.milyor.client.ui;

import io.github.milyor.client.RequestType;
import io.github.milyor.client.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserInputHandler {

    FileHandler fileHandler = new FileHandler();
    Scanner scanner = new Scanner(System.in);

    public Map<String, Object> getFileManagementInput() {
        HashMap<String, Object> data = new HashMap<>();

        String action = promptForAction();

        RequestType requestType = RequestType.fromString(action);
        data.put("action", requestType.toString());

        if (requestType == RequestType.EXIT) {
            return data;
        }

        switch (action) {
            case "1":
                case "3":
                    getIdentificationInput(data);
                    break;
                    case "2":
                    handleCreateFileInput(data);
                    break;
        }
        return data;
    }

    private String promptForAction() {
        while (true) {
            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file or Exit): ");
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                return "EXIT";
            }
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return input;
            }
            System.out.println("Invalid input. Please enter 1, 2, 3, or Exit.");
        }
    }

    private void handleCreateFileInput(Map<String, Object> data) {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        data.put("filename", filename);

        try {
            File file = fileHandler.prepareFile(filename);
            byte[] fileContent = fileHandler.readFileToByteArray(file);
            data.put("content", fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Enter name of the file to be saved on server: ");
        String serverFileName = scanner.nextLine();
        if (serverFileName.isEmpty()) {
            data.put("newName", "no");
        } else {
            data.put("newName", serverFileName);
        }
    }

    private void getIdentificationInput(Map<String, Object> data) {
        while (true) {
            System.out.print("Identify file by name or by ID? (1 - name, 2 - id): ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                System.out.print("Enter filename: ");
                String fileName = scanner.nextLine().trim(); // Read filename
                data.put("filename", fileName); // Use "filename" key
                return; // Input captured, exit helper method
            } else if (choice.equals("2")) {
                System.out.print("Enter id: ");
                String fileID = scanner.nextLine().trim(); // Read ID
                data.put("id", fileID); // Use "id" key
                return; // Input captured, exit helper method
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }
}
