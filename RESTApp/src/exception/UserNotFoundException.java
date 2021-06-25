package exception;

public class UserNotFoundException extends NullPointerException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
