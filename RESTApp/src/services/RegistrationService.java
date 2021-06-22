package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import beans.Administrator;
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
import dao.RegisteredUserDAO;
import dao.TicketDAO;
import dto.RegistrationDTO;
import dto.TicketDTO;
import dto.UserProfileDTO;
import exception.InvalidInputException;
import exception.UserExistsException;

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
		if (ctx.getAttribute("ticketDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("ticketDAO", new TicketDAO(contextPath));

			RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
			TicketDAO daoT = (TicketDAO) ctx.getAttribute("ticketDAO");
			// TEST

			Administrator a = (Administrator) dao.findByUsername("admin");
			RegisteredUser u = (RegisteredUser) dao.findByUsername("cao");

			List<Ticket> tickets = u.getTickets();
			tickets.add(daoT.findById("1"));
			tickets.add(daoT.findById("2"));
			u.setTickets(tickets);

			// --------------

			ctx.setAttribute("registeredUser", u);
		}

	}

	// Register new user into the system
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisteredUser addRegisteredUser(RegistrationDTO registrationDTO) {

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		if (dao.findByUsername(registrationDTO.getUsername()) != null)
			throw new UserExistsException("User with the username: " + registrationDTO.getUsername() + " already exists");
		String username = registrationDTO.getUsername().trim();
		String name = registrationDTO.getName().trim();
		String surname = registrationDTO.getLastName().trim();
		String password = registrationDTO.getPassword().trim();
		LocalDate date = null;
		if (name.equals("") || surname.equals("") || username.equals("") || password.equals("")
				|| registrationDTO.getBirthday().equals("") || registrationDTO.getGender().equals(""))
			throw new InvalidInputException("Input is invalid");

		try {

			date = LocalDate.parse(registrationDTO.getBirthday());

		} catch (DateTimeParseException e) {

			throw e;
		}

		if (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()))
			throw new InvalidInputException("Birthday is invalid");

		Gender gender = Gender.valueOf(registrationDTO.getGender().toUpperCase());
		List<Ticket> tickets = new ArrayList<Ticket>();
		RegisteredUser user = new RegisteredUser(username, password, name, surname, gender, date, UserRole.USER,
				CustomerKind.NEWBIE, tickets, 0, false);

		return (RegisteredUser) dao.addRegisteredUser(user);

	}

	// Returns the currently registered user
	@GET
	@Path("/registeredUser")
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileDTO getRegisteredUser() {
		User user = (User) ctx.getAttribute("registeredUser");

		TicketDAO dao = (TicketDAO) ctx.getAttribute("ticketDAO");

		List<TicketDTO> tickets = new ArrayList<TicketDTO>();

		if (user.getRole() == UserRole.USER) {
			for (Ticket ticket : ((RegisteredUser) user).getTickets()) {
				if (ticket.getStatus() == TicketStatus.RESERVED)
					tickets.add(new TicketDTO(ticket));
			}
		} else if (user.getRole() == UserRole.VENDOR) {
			for (Ticket ticket : dao.findAllList()) {
				if (ticket.getStatus() == TicketStatus.RESERVED)
					tickets.add(new TicketDTO(ticket));
			}
		} else if (user.getRole() == UserRole.ADMINISTRATOR) {
			for (Ticket ticket : dao.findAllList()) {
				tickets.add(new TicketDTO(ticket));
			}
		}

		UserProfileDTO userDAO = new UserProfileDTO(user.getUsername(), user.getPassword(), user.getFirstName(),
				user.getLastName(), user.getGender(), user.getBirthday().toString(), tickets,
				user.getRole().toString());
		return userDAO;

	}
}
