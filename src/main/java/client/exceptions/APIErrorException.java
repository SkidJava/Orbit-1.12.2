package client.exceptions;

public class APIErrorException extends Exception {
    private final String errorMessage;

    public APIErrorException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public final String getErrorMessage() {
        return this.errorMessage;
    }
}