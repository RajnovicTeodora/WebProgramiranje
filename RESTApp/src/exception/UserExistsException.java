package exception;

import javax.ws.rs.ForbiddenException;

public class UserExistsException extends ForbiddenException {

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