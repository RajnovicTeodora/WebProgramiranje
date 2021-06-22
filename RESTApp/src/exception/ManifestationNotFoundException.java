package exception;

public class ManifestationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ManifestationNotFoundException() {
        super();
    }

    public ManifestationNotFoundException(String message) {
        super(message);
    }

    public ManifestationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}