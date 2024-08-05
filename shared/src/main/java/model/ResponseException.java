package model;

public class ResponseException extends Exception {
    public ResponseException(int statusCode, String message) {
        super("Error " + statusCode + ": " + message);
    }
}