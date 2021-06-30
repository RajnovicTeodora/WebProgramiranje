package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.CustomerKind;
import beans.Gender;
import beans.Manifestation;
import beans.RegisteredUser;
import beans.Ticket;
import beans.User;
import beans.UserRole;
import beans.Vendor;
import dao.CommentDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.RegisteredUserDAO;
import dao.TicketDAO;
import dto.EditProfileDTO;
import dto.LogInDTO;
import dto.RegistrationDTO;
import dto.UserDTO;
import dto.UserProfileDTO;
import exception.InvalidInputException;
import exception.UnauthorizedUserException;
import exception.UserExistsException;
import exception.UserNotFoundException;

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
		if (ctx.getAttribute("manifestationDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("manifestationDAO", new ManifestationDAO(contextPath));
		}
		if (ctx.getAttribute("commentDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
		}
		if (ctx.getAttribute("ticketDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("ticketDAO", new TicketDAO(contextPath));
		}
		if (ctx.getAttribute("locationDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("locationDAO", new LocationDAO(contextPath));
		}

	}

	// Register new user into the system
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User addRegisteredUser(RegistrationDTO registrationDTO) {

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		if (dao.findByUsername(registrationDTO.getUsername()) != null)
			throw new UserExistsException(
					"User with the username: " + registrationDTO.getUsername() + " already exists");
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

		User maybeAdmin = (User) ctx.getAttribute("registeredUser");
		UserRole userRole = UserRole.USER;

		if (maybeAdmin != null && maybeAdmin.getRole() == UserRole.ADMINISTRATOR) {
			userRole = UserRole.VENDOR;
			Vendor user = new Vendor(username, password, name, surname, gender, date, userRole,
					CustomerKind.NEWBIE);
			dao.addUser(user);
			return (Vendor) dao.addRegisteredUser(user);
		}
		else {
			
			RegisteredUser user = new RegisteredUser(username, password, name, surname, gender, date, userRole,
					CustomerKind.NEWBIE, tickets, 0, false);
			if(dao.addUser(user)) {
				return (RegisteredUser) dao.addRegisteredUser(user);
			}
			else {
				return null;
			}
			
		}
		
	}

	// Returns the currently registered user
	@GET
	@Path("/registeredUser")
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileDTO getRegisteredUser() {
		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");
		UserProfileDTO userDAO = new UserProfileDTO(user);
		return userDAO;

	}

	// Edit user profile
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileDTO editRegisteredUser(EditProfileDTO editDTO) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");

		String name = editDTO.getName().trim();
		String surname = editDTO.getSurname().trim();
		String password = editDTO.getPassword().trim();

		if (name.equals("") || surname.equals("") || password.equals(""))
			throw new InvalidInputException("Input is invalid");

		User foundUser = dao.findByUsername(user.getUsername());
		if (foundUser == null)
			throw new UserNotFoundException("User not found");

		foundUser.setFirstName(name);
		foundUser.setLastName(surname);
		foundUser.setPassword(password);

		ctx.setAttribute("registeredUser", foundUser);

		dao.addRegisteredUser(foundUser);
		dao.writeAllUsers();
		return new UserProfileDTO(foundUser);

	}

	// Returns the currently registered users
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UserDTO> getRegisteredUsers() {

		User user = (User) ctx.getAttribute("registeredUser");

		if (user == null)
			throw new UserNotFoundException("User not registered");

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");

		ArrayList<UserDTO> users = new ArrayList<UserDTO>();

		if (user.getRole() == UserRole.ADMINISTRATOR) {
			for (User u : dao.findAllList()) {
				if (!u.getUsername().equals(user.getUsername())) {
					UserDTO dto = new UserDTO(u);
					dto.setStatus("active");
					users.add(dto);
				}
			}
			for (User u : dao.findAllBlockedList()) {
				UserDTO dto = new UserDTO(u);
				dto.setStatus("blocked");
				users.add(dto);
			}
		} else if (user.getRole() == UserRole.VENDOR) {
			for (User u : dao.findAllList()) {
				if (u.getRole() != UserRole.USER || u.getUsername().equals(user.getUsername()))
					continue;
				else {
					for (Ticket t : ((RegisteredUser) u).getTickets()) {
						if (((Vendor) user).getManifestations().contains(t.getManifestation())) {
							users.add(new UserDTO(u));
							break;
						}
					}
				}
			}
		} else {
			throw new UnauthorizedUserException("User does not have permission to view system users");
		}

		return users;
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileDTO logIn(LogInDTO logInDTO) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user != null)
			throw new UserExistsException("User already logged in");

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");

		String username = logInDTO.getUsername().trim();
		String password = logInDTO.getPassword().trim();

		if (username.equals("") || password.equals(""))
			throw new InvalidInputException("Input is invalid");

		User foundUser = dao.find(username, password);
		if (foundUser == null)
			throw new UserNotFoundException("Entered credidentials are invalid");

		ctx.setAttribute("registeredUser", foundUser);
		return new UserProfileDTO(foundUser);

	}

	@GET
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserExistsException("User not logged in");

		ctx.setAttribute("registeredUser", null);

	}
	
	@GET
	@Path("/searchUsers/{FirstName}/{LastName}/{Username}/{Type}/{Role}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> searchUsers(@PathParam("FirstName") String FirstName, @PathParam("LastName") String LastName, @PathParam("Username") String Username,
			@PathParam("Type") String Type, @PathParam("Role") String Role){  
				
		String fistName;
		String lastName;
		String username;
		if (FirstName.equals("null")) fistName="";
		else fistName = FirstName;
		
		if (LastName.equals("null")) lastName="";
		else lastName = LastName;
		
		if (Username.equals("null")) username="";
		else username = Username;
		
		int type = -1;
		int role = -1;
		try {
			type = Integer.parseInt(Type);
			role = Integer.parseInt(Role);
		}catch (Exception e) {}

		
		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		
		List<User> allUsers = dao.findAllList();
		List<UserDTO> filteredUsers = new ArrayList<UserDTO>();
		for(User u : allUsers) {
			if(!u.getFirstName().toLowerCase().contains(fistName.toLowerCase())) continue;   // provera ime
			if(!u.getLastName().toLowerCase().contains(lastName.toLowerCase())) continue; //provera lokacija
			if(!u.getUsername().toLowerCase().contains(username.toLowerCase())) continue;
			if(type != -1 && u.getCustomerType().ordinal() != type) continue;
			if(role != -1 && u.getRole().ordinal() != role) continue;

			
			filteredUsers.add(new UserDTO(u));
		}
		return filteredUsers;
	}
}
