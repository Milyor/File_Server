# File Server Project

A simple client-server application written in Java using Gradle for managing files. Allows clients to upload (PUT), download (GET), and delete files stored on the server, identifying them either by name or by a server-assigned ID.

## Features

* **Client-Server Architecture:** Clear separation between client and server logic.
* **Core File Operations:** Supports uploading (PUT), downloading (GET), and deleting (DELETE) files.
* **Dual File Identification:** Files can be referenced by server-assigned ID or by filename.
* **Command-Line Client:** Provides a simple interactive command-line interface for users.
* **Persistent Server State:** The server maintains a registry of files (`FileRegistry`) and persists its state to a JSON file (`file_registry_data.json`).
* **Command Pattern:** The server utilizes the Command pattern for handling client requests.
* **Organized Structure:** Follows standard Gradle project layout and uses packages for organization (e.g., `io.github.milyor.client`, `io.github.milyor.server`).

## Technology Stack

* **Language:** Java (Requires JDK 11 or later - *21
* **Build Tool:** Gradle
* **Libraries:**
    * Gson (for JSON serialization/deserialization in `FileRegistry`)
