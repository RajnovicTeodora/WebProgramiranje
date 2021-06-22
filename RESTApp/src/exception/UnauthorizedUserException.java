package exception;

public class UnauthorizedUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedUserException() {
        super();
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }

    public UnauthorizedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}