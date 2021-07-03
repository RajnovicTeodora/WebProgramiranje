package exception;

public class ManifestationExistsException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ManifestationExistsException() {
        super();
    }

    public ManifestationExistsException(String message) {
        super(message);
    }

    public ManifestationExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}