package services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
import beans.RegisteredUser;
import beans.Ticket;
import beans.User;
import beans.UserRole;
import dao.RegisteredUserDAO;
import dto.RegistrationDTO;

@Path("/registration")
public class RegistrationService {

	@Context
	ServletContext ctx;

	public RegistrationService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("registeredUserDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("registeredUserDAO", new RegisteredUserDAO(contextPath));
		}
		List<Ticket> tickets = new ArrayList<Ticket>();
		User u = new RegisteredUser("cao", "cao", "cao", "cao", Gender.FEMALE, LocalDate.now(), UserRole.USER, CustomerKind.CHAMP, tickets, 0 , false); 
		ctx.setAttribute("registeredUser", u);
	}

	// Register new user into the system
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisteredUser addRegisteredUser(RegistrationDTO registrationDTO) {

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		if (dao.findByUsername(registrationDTO.getUsername()) != null)
			return null;
		String username = registrationDTO.getUsername().trim();
		String name = registrationDTO.getName().trim();
		String surname = registrationDTO.getLastName().trim();
		String password = registrationDTO.getPassword().trim();
		LocalDate date = null;
		if (name.equals("") || surname.equals("") || username.equals("") || password.equals("")
				|| registrationDTO.getBirthday().equals("") || registrationDTO.getGender().equals(""))
			return null;

		try {
			
			date = LocalDate.parse(registrationDTO.getBirthday());
			
		} catch ( DateTimeParseException e){
			
			throw e;
		}

		if (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()))
			return null;

		Gender gender = Gender.valueOf(registrationDTO.getGender().toUpperCase());
		List<Ticket> tickets = new ArrayList<Ticket>();
		RegisteredUser user = new RegisteredUser(username, password, name, surname, gender, date, UserRole.USER,
				CustomerKind.NEWBIE, tickets, 0, false);

		return dao.addRegisteredUser(user);

	}

	// Returns the currently registered user
	@GET
	@Path("/registeredUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getRegisteredUser() {
		RegisteredUser user = (RegisteredUser) ctx.getAttribute("registeredUser");
		return user;

	}
}