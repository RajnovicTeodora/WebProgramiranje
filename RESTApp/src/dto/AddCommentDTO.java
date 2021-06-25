package dto;

public class AddCommentDTO {

	private String text;
	private int rating; // 1,2,3,4,5
	private int manifestation;

	public AddCommentDTO() {
		super();
	}

	public AddCommentDTO(String text, int rating, int manifestation) {
		super();
		this.text = text;
		this.rating = rating;
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

	public int getManifestation() {
		return manifestation;
	}

	public void setManifestation(int manifestation) {
		this.manifestation = manifestation;
	}

}
