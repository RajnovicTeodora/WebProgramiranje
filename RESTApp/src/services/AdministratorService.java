package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.ManifestationStatus;
import beans.User;
import beans.UserRole;
import dao.ManifestationDAO;
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

		List<Manifestation> manifestations = new ArrayList<Manifestation>();
		for (Manifestation manifestation : manifestationDAO.findAllList()) {
			// Manifestation not active and date is in future
			if (manifestation.getStatus() == ManifestationStatus.UNACTIVE
					&& (manifestation.getDate().isEqual(today) || manifestation.getDate().isAfter(today))) {
				manifestations.add(manifestation);
			}
		}
		return manifestations;

	}
}
