package exception;

public class TicketNotFoundEception  extends NullPointerException {

    private static final long serialVersionUID = 1L;

    public TicketNotFoundEception() {
        super();
    }

    public TicketNotFoundEception(String message) {
        super(message);
    }

}