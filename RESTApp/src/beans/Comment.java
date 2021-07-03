package beans;

public class Comment {
	private int id;
	private RegisteredUser user; // user who left the comment
	private Manifestation manifestation;
	private String text;
	private int rating; // 1,2,3,4,5
	private CommentStatus status;
	private Boolean deleted;

	public Comment() {
		super();
	}

	public Comment(int id, RegisteredUser user, Manifestation manifestation, String text, int rating,
			CommentStatus status, Boolean deleted) {
		super();
		this.id = id;
		this.user = user;
		this.manifestation = manifestation;
		this.text = text;
		this.rating = rating;
		this.status = status;
		this.deleted = deleted;
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Comment [user=" + user.getUsername() + ", manifestation=" + manifestation.getName() + ", text=" + text
				+ ", rating=" + rating + ", status=" + status + "]";
	}

	public String toCsvString() {
		String[] elems = { String.valueOf(this.id), user.getUsername(), String.valueOf(manifestation.getId()),
				this.text, String.valueOf(this.rating), String.valueOf(this.status.ordinal()) };
		return String.join(";", elems) + "\n";
	}

}
