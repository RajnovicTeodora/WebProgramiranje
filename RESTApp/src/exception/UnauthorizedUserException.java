package exception;

import javax.ws.rs.ForbiddenException;

public class UnauthorizedUserException extends ForbiddenException {

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