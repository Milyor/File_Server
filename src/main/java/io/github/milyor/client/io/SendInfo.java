package io.github.milyor.client.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SendInfo {
    public void sendRequest(ObjectOutputStream outputStream, Map<String, Object> requestData) {
        try {
            outputStream.writeObject(requestData);
            System.out.println("The request was sent.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
