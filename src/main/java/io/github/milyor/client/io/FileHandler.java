package io.github.milyor.client.io;

import java.io.*;
import java.util.Scanner;

public class FileHandler {
    private final static String myRoot = "C:\\Users\\Mille\\IdeaProjects\\File_Server\\src\\main\\java\\io\\github\\milyor\\client\\data\\";
    Scanner scanner = new Scanner(System.in);

    public File prepareFile(String filename) throws IOException {
        File file = new File(myRoot + filename);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (created) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    System.out.print("Enter file content: ");
                    String content = scanner.nextLine();
                    writer.write(content);
                }
            } else {
                throw new IOException("Failed to create file.");
            }
        }
        return file;

    }

    public byte[] readFileToByteArray(File file)throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
    public void readByteArrayToFile (byte[] data, String name) throws IOException {
        File file = new File(myRoot + File.separator + name);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }

}
