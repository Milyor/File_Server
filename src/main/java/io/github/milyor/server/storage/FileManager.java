package io.github.milyor.server.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    FileRegistry fileRegistry = new FileRegistry();
    //for testing purposes
    private final static String absoluteRoot = "C:\\Users\\Mille\\IdeaProjects\\File_Server\\src\\main\\java\\io\\github\\milyor\\server\\data\\";

    public Map<String,Object> putFile(String fileName, byte[] content) throws IOException {

        File file = saveFile(content, fileName);
        Map<String, Object> data = new HashMap<>();
        if (file.exists()) {
            String id = fileRegistry.registerFile(fileName);
            data.put("message", "file is saved! ID = " + id);
            data.put("code", "200");
            return data;
        }
        data.put("code", "403");
        return data;
    }

    public File saveFile(byte[] content, String filename) throws IOException {
        //replace to root for final test
        File file = new File(absoluteRoot + filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
        return file;
    }

    public Map<String, Object> getFileById(String id) {
        String fileName = fileRegistry.getFileById(id);
        File file = new File(absoluteRoot + fileName);
        Map<String, Object> data = new HashMap<>();
        if (file.exists()) {
            data.put("code", "200");
            //delete later just for testing purposes.
            byte[] content = fileToByteArray(file);
            data.put("content", content);
            data.put("message", "The file was found");
            return data;
        }
        data.put("code", "403");
        data.put("message", "this file is not found!");
        return data;
    }

    public Map<String, Object> getFile(String fileName) {
        File file = new File(absoluteRoot +  fileName);
        Map<String, Object> data = new HashMap<>();
        if (file.exists()) {
            byte[] content = fileToByteArray(file);
            data.put("code", "200");
            data.put("content", content);
            return data;
        }
        data.put("code", "403");
        data.put("message", "this file is not found!");
        return data;
    }

    public  Map<String, Object> deleteFileById(String id) {
        Map<String, Object> data = new HashMap<>();
        String filename = fileRegistry.getFileById(id);
        File file = new File(absoluteRoot + filename);

        if (file.exists() && file.delete()) {
            fileRegistry.deleteFileById(id);
            data.put("code", "200");
            data.put("message", "this file was deleted successfully!");
            return data;
        }
        data.put("code", "403");
        data.put("message", "this file was not found!");
        return data;
    }

    public Map<String, Object> deleteFile(String fileName) {
        Map<String, Object> data = new HashMap<>();
        File file = new File(absoluteRoot + fileName);

        if (file.exists()) {
            // Implement multiple deletion attempts with a short delay
            for (int attempt = 0; attempt < 5; attempt++) {
                try {
                    // Explicit garbage collection to release any file handles
                    System.gc();

                    // Attempt to delete the file
                    boolean deleted = file.delete();

                    if (deleted) {
                        // Verify the file is actually deleted
                        if (!file.exists()) {
                            fileRegistry.deleteFileByName(fileName);
                            data.put("code", "200");
                            data.put("message", "This file was deleted successfully!");
                            return data;
                        }
                    }

                    // If not deleted, wait a short time before retrying
                    try {
                        Thread.sleep(100); // 100 milliseconds between attempts
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (Exception e) {
                    // Log the specific deletion error if needed
                    System.err.println("Deletion attempt " + (attempt + 1) + " failed: " + e.getMessage());
                }
            }

            // If all attempts fail
            data.put("code", "403");
            data.put("message", "Failed to delete the file after multiple attempts!");
        } else {
            data.put("code", "403");
            data.put("message", "This file was not found!");
        }

        return data;
    }

    private static byte[] fileToByteArray(File file) {
        try {
            return new FileInputStream(file).readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
