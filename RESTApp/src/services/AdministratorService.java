package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.CustomerKind;
import beans.Gender;
import beans.Location;
import beans.Manifestation;
import beans.ManifestationStatus;
import beans.ManifestationType;
import beans.RegisteredUser;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.User;
import beans.UserRole;
import dao.ManifestationDAO;
import dao.RegisteredUserDAO;
import dao.TicketDAO;
import dto.RegistrationDTO;
import dto.TicketDTO;
import dto.UserDTO;
import dto.UserProfileDTO;
import exception.UnauthorizedUserException;

@Path("/administrator")
public class AdministratorService {

	@Context
	ServletContext ctx;

	public AdministratorService() {

	}

	@PostConstruct
	public void init() {

	}

	// Returns the currently registered users
	@GET
	@Path("/registeredUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UserDTO> getRegisteredUser() {
		
		User user = (User) ctx.getAttribute("registeredUser");
		
		if(user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("User doesn not have permission to view system users");
		
		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
	    
	    ArrayList<UserDTO> users = new ArrayList<UserDTO>();
	    for(User u : dao.findAllList()) {
	    	if(!u.getUsername().equals(user.getUsername()))
	    		users.add(new UserDTO(u));
	    }
	    return users;
	}
}
