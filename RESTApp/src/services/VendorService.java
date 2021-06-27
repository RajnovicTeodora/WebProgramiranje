package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.User;
import beans.UserRole;
import beans.Vendor;
import exception.UnauthorizedUserException;
import exception.UserNotFoundException;

@Path("/vendor")
public class VendorService {

	@Context
	ServletContext ctx;

	public VendorService() {

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

		if (user.getRole() != UserRole.VENDOR)
			throw new UnauthorizedUserException("Unauthorized action");

		return ((Vendor) user).getManifestations();

	}
}
