package beans;

public class Comment {
	private User commentUser; //user who left the comment
	private Manifestation manifestation;
	private String text;
	private int mark; //1,2,3,4,5
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(User commentUser, Manifestation manifestation, String text, int mark) {
		super();
		this.commentUser = commentUser;
		this.manifestation = manifestation;
		this.text = text;
		this.mark = mark;
	}

	public User getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(User commentUser) {
		this.commentUser = commentUser;
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

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	@Override
	public String toString() {
		return "Comment [commentUser=" + commentUser + ", manifestation=" + manifestation + ", text=" + text + ", mark="
				+ mark + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentUser == null) ? 0 : commentUser.hashCode());
		result = prime * result + ((manifestation == null) ? 0 : manifestation.hashCode());
		result = prime * result + mark;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (commentUser == null) {
			if (other.commentUser != null)
				return false;
		} else if (!commentUser.equals(other.commentUser))
			return false;
		if (manifestation == null) {
			if (other.manifestation != null)
				return false;
		} else if (!manifestation.equals(other.manifestation))
			return false;
		if (mark != other.mark)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	

}
