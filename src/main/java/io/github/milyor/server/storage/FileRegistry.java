package io.github.milyor.server.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FileRegistry {
    private static final String RESOURCE_NAME = "./file_registry.json";
    private static final String PERSISTENT_FILE_PATH = "./file_registry_fata.json";

    private int lastUsedId;
    private final Map<String, String> fileMap = new HashMap<>();

    public FileRegistry() {
        loadData();
    }

    private synchronized void loadData() {
        boolean loadedFromFile = tryLoadFromFile();
        if (!loadedFromFile) {
            boolean loadedFromResource = tryLoadFromResource();
            if (!loadedFromResource) {
                System.err.println("No existing registry found on disk or in resource.Initializing new registry.");
                lastUsedId = 0;
            }
        }
    }

    private boolean tryLoadFromFile() {
        try(Reader reader = new BufferedReader(new FileReader(PERSISTENT_FILE_PATH))) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = new Gson().fromJson(reader, type);
            parseLoadedData(data);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("No existing file registry found: " + PERSISTENT_FILE_PATH);
            return false;
        } catch (IOException e) {
            System.err.println("Error reading persistent file registry: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error parsing registry file from system: " + e.getMessage());
            return false;
        }
    }
    private boolean tryLoadFromResource() {
        System.out.println("Attempting to load registry from resource: " + RESOURCE_NAME);
        // Get the ClassLoader associated with this class
        ClassLoader classLoader = FileRegistry.class.getClassLoader();
        // Try to get the resource as an InputStream
        InputStream inputStream = classLoader.getResourceAsStream(RESOURCE_NAME);

        // IMPORTANT: Check if the resource was found!
        if (inputStream == null) {
            System.err.println("Resource not found in classpath: " + RESOURCE_NAME);
            return false;
        }
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = new Gson().fromJson(reader, type);
            parseLoadedData(data);
            System.out.println("Successfully loaded registry from classpath resource.");
            return true;
        } catch (IOException e) {
            // Handle potential errors reading the resource stream
            System.err.println("Error reading registry resource from classpath: " + e.getMessage());
            return false;
        } catch (Exception e) { // Catch potential parsing errors
            System.err.println("Error parsing registry resource from classpath: " + e.getMessage());
            return false;
        }
    }

    private void parseLoadedData(Map<String, Object> data) {
        if (data != null) {
            // Handle potential ClassCastException or NullPointerException more safely
            Object idObj = data.get("lastUsedId");
            if (idObj instanceof Number) { // Check type before casting
                lastUsedId = ((Number) idObj).intValue();
            } else {
                System.err.println("Warning: 'lastUsedId' missing or not a number in loaded data. Resetting to 0.");
                lastUsedId = 0; // Default if missing or wrong type
            }

            Object filesObj = data.get("files");
            if (filesObj instanceof Map) { // Check type
                // We need to ensure the map contains String keys and String values
                try {
                    Map<?, ?> rawFilesMap = (Map<?, ?>) filesObj;
                    fileMap.clear(); // Clear existing entries before putting new ones
                    for (Map.Entry<?, ?> entry : rawFilesMap.entrySet()) {
                        if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                            fileMap.put((String) entry.getKey(), (String) entry.getValue());
                        } else {
                            System.err.println("Warning: Non-string key/value found in 'files' map. Skipping entry.");
                        }
                    }
                } catch (ClassCastException e) {
                    System.err.println("Warning: Could not cast 'files' data to Map<String, String>. File map may be incomplete.");
                }

            } else if (filesObj != null) {
                System.err.println("Warning: 'files' data is not a Map. Ignoring.");
            }
        } else {
            System.err.println("Warning: Loaded data was null.");
            lastUsedId = 0; // Reset if data is null
            fileMap.clear();
        }
    }


    private synchronized void saveToDisk() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PERSISTENT_FILE_PATH))) {
            Map<String, Object> data = new HashMap<>();
            data.put("lastUsedId", lastUsedId);
            data.put("files", fileMap);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(data);//Convert Map to JSON
            writer.write(json);
            System.out.println("Successfully saved persistent file registry." + PERSISTENT_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Failed to save file to " + PERSISTENT_FILE_PATH);
        }
    }

    public synchronized String registerFile(String name) {
        String id = generateId();
        fileMap.put(id, name);
        saveToDisk();
        return id;
    }

    private synchronized String generateId() {
        lastUsedId++;
        return String.valueOf(lastUsedId);
    }

    public String getFileById(String id) {
        return fileMap.get(id);
    }

    public synchronized boolean deleteFileById(String id) {
        if (fileMap.containsKey(id)) {
            fileMap.remove(id);
            saveToDisk();
            return true;
        }
        return false;
    }
    public synchronized boolean deleteFileByName(String name) {
        String idToRemove = null;
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                idToRemove = entry.getKey();
                break;
            }
        }
        if (idToRemove != null) {
            fileMap.remove(idToRemove);
            saveToDisk();
            return true;
        }
        return false;
    }
}
