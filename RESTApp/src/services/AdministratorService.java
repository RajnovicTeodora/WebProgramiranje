package services;

import java.time.LocalDateTime;
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

import beans.Manifestation;
import beans.ManifestationStatus;
import beans.Ticket;
import beans.User;
import beans.UserRole;
import beans.Vendor;
import dao.ManifestationDAO;
import dao.RegisteredUserDAO;
import dto.ReservationDTO;
import dao.TicketDAO;
import exception.UnauthorizedUserException;
import exception.UserNotFoundException;

@Path("/admin")
public class AdministratorService {

	@Context
	ServletContext ctx;

	public AdministratorService() {

	}

	@PostConstruct
	public void init() {

	}

	@GET
	@Path("/manifestations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getManifestations() {
		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Unauthorized action");

		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		LocalDateTime today = LocalDateTime.now();

		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("ticketDAO");
		boolean canDelete = true;
		List<Manifestation> manifestations = new ArrayList<Manifestation>();
		for (Manifestation manifestation : manifestationDAO.findAllList()) {
			
			// Manifestation doesn't have reserved tickets
			for (Ticket t : ticketDAO.findAllList()) {
				if (t.getManifestation().equals(manifestation))
					canDelete = false;
			}
			
			// Manifestation not active and date is in future
			if (manifestation.getStatus() == ManifestationStatus.UNACTIVE
					&& (manifestation.getDate().isEqual(today) || manifestation.getDate().isAfter(today)) || canDelete) {
				manifestations.add(manifestation);
			}
			canDelete = true;
			
		}
		return manifestations;
	}
	
	@POST
	@Path("/blockUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean blockUser(String username) {
		
		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Unauthorized action");

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		
		if(dao.findByUsername(username) == null)
			throw new UserNotFoundException("No user with that username");
		
		User blockedUser = dao.findByUsername(username);
		
		if(blockedUser.getRole() == UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Can not block administrator!");
		
		if(dao.isUserBlocked(blockedUser)) {
			dao.addRegisteredUser(blockedUser);
		}
		
		else {
			dao.addBlockedUser(blockedUser);
		}
		
		if(dao.writeAllUsers())
			return dao.writeBlockedUsers();
		
		return false;
		
	}
	
	@POST
	@Path("/deleteUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(String username) {
		
		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Unauthorized action");

		RegisteredUserDAO dao = (RegisteredUserDAO) ctx.getAttribute("registeredUserDAO");
		
		if(dao.findByUsername(username) == null)
			throw new UserNotFoundException("No user with that username");
		
		User deletedUser = dao.findByUsername(username);
		
		List<Manifestation> manifestations = ((Vendor) deletedUser).getManifestations();
		for (Manifestation m : manifestations) {
			m.setVendorUsername("null");
		}
		
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		manifestationDao.writeAllManifetations();
		
		if(deletedUser.getRole() == UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Can not delete administrator!");
		
		dao.addDeletedUser(deletedUser);
		
		dao.writeAllUsers();
		dao.writeBlockedUsers();
		
		return dao.deleteUser(deletedUser);
		
	}
}
