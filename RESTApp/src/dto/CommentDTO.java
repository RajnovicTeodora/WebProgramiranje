package dto;

import beans.Comment;
import beans.CommentStatus;

public class CommentDTO {

	private int id;
	private String user; // user who left the comment
	private String text;
	private int rating; // 1,2,3,4,5
	private CommentStatus status;

	public CommentDTO() {
		super();
	}

	public CommentDTO(Comment comment) {

		this.id = comment.getId();
		this.user = comment.getUser().getUsername();
		this.text = comment.getText();
		this.rating = comment.getRating();
		this.status = comment.getStatus();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

}
