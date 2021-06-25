package beans;

public class Comment {
	private int id;
	private RegisteredUser user; // user who left the comment
	private Manifestation manifestation;
	private String text;
	private int rating; // 1,2,3,4,5
	private CommentStatus status;

	public Comment() {
		super();
	}

	public Comment(int id, RegisteredUser user, Manifestation manifestation, String text, int rating,
			CommentStatus status) {
		super();
		this.id = id;
		this.user = user;
		this.manifestation = manifestation;
		this.text = text;
		this.rating = rating;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RegisteredUser getUser() {
		return user;
	}

	public void setUser(RegisteredUser user) {
		this.user = user;
	}

	public Manifestation getManifestation() {
		return manifestation;
	}

	public void setManifestation(Manifestation manifestation) {
		this.manifestation = manifestation;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Comment [user=" + user.getUsername() + ", manifestation=" + manifestation.getName() + ", text=" + text
				+ ", rating=" + rating + ", status=" + status + "]";
	}

}
