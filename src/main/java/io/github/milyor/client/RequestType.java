package io.github.milyor.client;


public enum RequestType {
    GET,
    PUT,
    DELETE,
    EXIT;

    public static RequestType fromString(String value) {
        return switch (value) {
            case "1" -> GET;
            case "2" -> PUT;
            case "3" -> DELETE;
            default -> EXIT;
        };
    }
}