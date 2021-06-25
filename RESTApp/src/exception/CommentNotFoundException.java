package exception;

public class CommentNotFoundException extends NullPointerException {

	private static final long serialVersionUID = 1L;

	public CommentNotFoundException() {
		super();
	}

	public CommentNotFoundException(String message) {
		super(message);
	}

}