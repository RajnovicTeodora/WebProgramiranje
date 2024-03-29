package exception;


public class ManifestationNotFoundException extends NullPointerException {

    private static final long serialVersionUID = 1L;

    public ManifestationNotFoundException() {
        super();
    }

    public ManifestationNotFoundException(String message) {
        super(message);
    }

}