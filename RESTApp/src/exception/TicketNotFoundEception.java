package exception;

public class TicketNotFoundEception  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TicketNotFoundEception() {
        super();
    }

    public TicketNotFoundEception(String message) {
        super(message);
    }

    public TicketNotFoundEception(String message, Throwable cause) {
        super(message, cause);
    }
}