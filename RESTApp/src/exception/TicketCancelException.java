package exception;

import javax.ws.rs.ForbiddenException;

public class TicketCancelException extends ForbiddenException {

	private static final long serialVersionUID = 1L;

	public TicketCancelException() {
		super();
	}

	public TicketCancelException(String message) {
		super(message);
	}

}