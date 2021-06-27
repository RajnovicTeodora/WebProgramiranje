package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.CommentStatus;
import beans.Manifestation;
import beans.RegisteredUser;
import beans.Ticket;
import beans.TicketStatus;
import beans.User;
import beans.UserRole;
import dao.CommentDAO;
import dao.ManifestationDAO;
import dto.AddCommentDTO;
import dto.CommentDTO;
import exception.CommentNotFoundException;
import exception.InvalidInputException;
import exception.ManifestationNotFoundException;
import exception.UnauthorizedUserException;
import exception.UserNotFoundException;

import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

@Path("/comments")
public class CommentService {

	@Context
	ServletContext ctx;

	public CommentService() {

	}

	@PostConstruct
	public void init() {

	}

	@GET
	@Path("/isCommentingAllowed/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isCommentingAllowed(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.USER)
			throw new UnauthorizedUserException("Unauthorized action");

		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		int intId = -1;
		try {
			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			return false;
		}

		Manifestation manifestation = dao.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");

		for (Ticket t : ((RegisteredUser) user).getTickets()) {
			if (t.getManifestation().equals(manifestation) && t.getStatus() == TicketStatus.RESERVED)
				return true;
		}

		return false;
	}

	@GET
	@Path("/list/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentDTO> getComments(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");

		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");

		int intId = -1;
		try {
			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			throw new NumberFormatException();
		}
		
		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		Manifestation manifestation = manifestationDAO.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");

		List<CommentDTO> comments = new ArrayList<CommentDTO>();

		if (user == null || user.getRole() == UserRole.USER) {
			for (Comment comment : commentDAO.findAllList()) {
				if (comment.getStatus() == CommentStatus.APPROVED
						&& comment.getManifestation().getId() == manifestation.getId()) {
					comments.add(new CommentDTO(comment));
				}
			}
		} else {
			for (Comment comment : commentDAO.findAllList()) {
				if (comment.getManifestation().getId() == manifestation.getId()) {
					comments.add(new CommentDTO(comment));
				}
			}
		}

		return comments;
	}

	@GET
	@Path("/reject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CommentDTO rejectComment(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.VENDOR)
			throw new UnauthorizedUserException("Unauthorized action");

		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");

		int intId = -1;
		try {

			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			throw new NumberFormatException();
		}

		Comment comment = commentDAO.findById(intId);
		if (comment == null)
			throw new CommentNotFoundException("Comment with the id " + id + " not found");

		comment.setStatus(CommentStatus.REJECTED);
		commentDAO.addComment(comment);
		commentDAO.writeAllComments();
		return new CommentDTO(comment);
	}

	@GET
	@Path("/approve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CommentDTO approveComment(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.VENDOR)
			throw new UnauthorizedUserException("Unauthorized action");

		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");

		int intId = -1;
		try {

			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			throw new NumberFormatException();
		}

		Comment comment = commentDAO.findById(intId);
		if (comment == null)
			throw new CommentNotFoundException("Comment with the id " + id + " not found");

		comment.setStatus(CommentStatus.APPROVED);
		commentDAO.addComment(comment);
		commentDAO.writeAllComments();
		return new CommentDTO(comment);
	}

	// Add new comment
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommentDTO addNewComment(AddCommentDTO commentDTO) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.USER)
			throw new UnauthorizedUserException("Unauthorized action");

		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");

		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		Manifestation manifestation = manifestationDAO.findById(commentDTO.getManifestation());
		if (manifestation == null)
			throw new ManifestationNotFoundException(
					"Manifestation with the id " + commentDTO.getManifestation() + " not found");

		if (commentDTO.getRating() < 1 || commentDTO.getRating() > 5 || commentDTO.getText().trim() == "")
			throw new InvalidInputException("Invalid inpu");

		Comment comment = new Comment(commentDAO.findId(), (RegisteredUser) user, manifestation, commentDTO.getText(),
				commentDTO.getRating(), CommentStatus.WAITING);
		commentDAO.writeComment(comment);
		return new CommentDTO(commentDAO.addComment(comment));
	}
}
