package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import beans.Comment;
import beans.CommentStatus;

public class CommentDAO {

	private Map<Integer, Comment> comments = new HashMap<>();

	public CommentDAO() {

	}

	public CommentDAO(String contextPath) {
		// TODO loadComments(contextPath);
		Comment c1 = new Comment(1, null, null, "Some text", 5, CommentStatus.WAITING);
		Comment c2 = new Comment(2, null, null, "Again some text", 4, CommentStatus.APPROVED);
		Comment c3 = new Comment(3, null, null, "Just text", 3, CommentStatus.REJECTED);

		addComment(c1);
		addComment(c2);
		addComment(c3);
	}

	public Comment findById(int id) {
		if (!comments.containsKey(id)) {
			return null;
		}
		return comments.get(id);
	}

	public Comment addComment(Comment comment) {
		comments.put(comment.getId(), comment);
		return comment;
	}

	public Collection<Comment> findAll() {
		return comments.values();
	}

	public ArrayList<Comment> findAllList() {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		for (Comment comment : findAll()) {
			commentList.add(comment);
		}
		return commentList;
	}
	/*
	 * private void loadComments(String contextPath) { // TODO }
	 */

	public int findId() {
		int i = 1;

		while (true) {
			if (comments.containsKey(i)) {
				i++;
			} else {
				return i;
			}
		}
	}
}
