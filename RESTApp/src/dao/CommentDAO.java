package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import beans.Comment;
import beans.CommentStatus;
import beans.Manifestation;
import beans.RegisteredUser;

public class CommentDAO {

	private Map<Integer, Comment> comments = new HashMap<>();

	public CommentDAO() {

	}

	public CommentDAO(String contextPath) {
		loadAll(contextPath);
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
	
	private void loadAll(String contextPath) {
		BufferedReader bufferedReader = null;
		try {

			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\comments.csv"); 
			bufferedReader = new BufferedReader(reader);
			String line;
			
			line =  bufferedReader.readLine();
			
			while (line != null){
				
				if ( line.charAt(0) == '#') {
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] st = line.split(";");
				
				int id = Integer.parseInt(st[0].trim());
				RegisteredUser user = ToiToiDAO.getUser(contextPath, st[1].trim()); 
				Manifestation manifestation = ToiToiDAO.getManifestation(contextPath, Integer.parseInt(st[2].trim()));
				String text = st[3].trim();
				int rating = Integer.valueOf(st[4]);
				CommentStatus status = CommentStatus.values()[Integer.parseInt(st[5].trim())];

				Comment comment = new Comment(id, user, manifestation, text, rating, status); 
				addComment(comment);
				
				line = bufferedReader.readLine();
			}			
			reader.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
