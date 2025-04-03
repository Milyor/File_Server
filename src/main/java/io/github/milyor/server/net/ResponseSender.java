package io.github.milyor.server.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseSender implements Runnable {
    private final HashMap<String, Object> response;
    private final ObjectOutputStream outputStream;

    public ResponseSender(ObjectOutputStream outputStream, Map<String, Object> response) {
        this.response = (HashMap<String,Object>) response;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try  {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
