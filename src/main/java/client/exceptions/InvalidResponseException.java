package client.exceptions;

public class InvalidResponseException extends Exception {
    private final String response;

    public InvalidResponseException(final String response) {
        super(response);
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }
}