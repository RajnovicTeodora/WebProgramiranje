package exception;

public class UserExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserExistsException() {
        super();
    }

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}