package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

import beans.Comment;
import beans.Location;
import beans.Manifestation;
import beans.ManifestationStatus;
import beans.ManifestationType;
import beans.Ticket;
import beans.User;
import beans.UserRole;
import beans.Vendor;
import dao.CommentDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dto.EditManifestationDTO;
import dto.NewManifestationDTO;
import exception.CommentNotFoundException;
import exception.InvalidInputException;
import exception.ManifestationExistsException;
import exception.ManifestationNotFoundException;
import exception.UnauthorizedUserException;
import exception.UserNotFoundException;

@Path("/manifestations")
public class ManifestationService {

	@Context
	ServletContext ctx;

	public ManifestationService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("manifestationDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("manifestationDAO", new ManifestationDAO(contextPath));
		}

	}

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Manifestation> getManifestations() {
		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		ArrayList<Manifestation> manifestations = dao.findAllList();
		manifestations.sort(new Comparator<Manifestation>() {
			@Override
			public int compare(Manifestation o1, Manifestation o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		// TODO: sort should be most recent
		return manifestations;

	}

	@GET
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation deleteManifestation(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Unauthorized action");

		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		int intId = -1;
		try {
			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			throw e;
		}

		Manifestation manifestation = dao.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");

		manifestation.setDeleted(true);
		dao.removeManifestation(manifestation);
		dao.writeAllManifetations();
		return manifestation;
	}

	@GET
	@Path("/isDeleteAllowed/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isMAnifestationDeleteAllowed(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
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

		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("ticketDAO");
		for (Ticket t : ticketDAO.findAllList()) {
			if (t.getManifestation().equals(manifestation))
				return false;
		}

		return true;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation findOne(@PathParam("id") String id) {
		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		int intId = -1;
		try {

			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			return null;
		}

		Manifestation manifestation = dao.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");
		return manifestation;
	}

	// Add new manifestation
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation addNewManifestation(NewManifestationDTO newManifestationDTO) {

		User maybeUser = (User) ctx.getAttribute("registeredUser");
		if (maybeUser == null)
			throw new UserNotFoundException("No user registered");

		if (maybeUser.getRole() != UserRole.VENDOR)
			throw new UnauthorizedUserException("Unauthorized action");

		Vendor user = (Vendor) maybeUser;

		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");

		if (newManifestationDTO.getLat() <= 0 || newManifestationDTO.getLocation().trim() == ""
				|| newManifestationDTO.getLon() <= 0 || newManifestationDTO.getName().trim() == ""
				|| newManifestationDTO.getPoster().trim() == "" || newManifestationDTO.getType().trim() == ""
				|| newManifestationDTO.getDate() == null || newManifestationDTO.getNumSeats() <= 0
				|| newManifestationDTO.getRegularPrice() <= 0)
			throw new InvalidInputException("Invalid input, please fill out all fields");

		LocationDAO locationDao = (LocationDAO) ctx.getAttribute("locationDAO");

		int locationID = locationDao.findNextId();
		Location location = new Location(locationID, newManifestationDTO.getLat(), newManifestationDTO.getLon(),
				newManifestationDTO.getLocation());

		if (dao.isManifestationOverlapping(newManifestationDTO.getDate(), location, -1)) {
			throw new ManifestationExistsException("Manifestation for that date and location exists");
		}

		locationDao.writeLocation(location);
		locationDao.addLocation(location);

		List<Manifestation> manifestations = user.getManifestations();

		ManifestationType type = ManifestationType.valueOf(newManifestationDTO.getType());

		Manifestation manifestation = new Manifestation(dao.getNextId(), newManifestationDTO.getName(), type,
				newManifestationDTO.getNumSeats(), newManifestationDTO.getDate(), newManifestationDTO.getRegularPrice(),
				ManifestationStatus.UNACTIVE, location, newManifestationDTO.getPoster(), false);
		manifestation.setVendorUsername(user.getUsername());
		manifestations.add(manifestation);
		user.setManifestations(manifestations);

		if (dao.writeManifestation(manifestation)) {
			return dao.addManifestation(manifestation);
		} else {
			return null;
		}
	}

	// Edit manifestation
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation editManifestation(EditManifestationDTO editDTO) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.VENDOR)
			throw new UnauthorizedUserException("Unauthorized action");

		if (editDTO.getId().trim() == "" || editDTO.getLat() <= 0 || editDTO.getLocation().trim() == ""
				|| editDTO.getLon() <= 0 || editDTO.getName().trim() == "" || editDTO.getPoster().trim() == ""
				|| editDTO.getType().trim() == "")
			throw new InvalidInputException("Invalid input, please fill out all fields");

		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");

		int intId = -1;
		try {

			intId = Integer.parseInt(editDTO.getId());

		} catch (NumberFormatException e) {
			System.out.println(editDTO.getId() + " is not a valid integer number");
			return null;
		}

		Manifestation manifestation = dao.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + editDTO.getId() + "not found");

		Location location = new Location(editDTO.getLat(), editDTO.getLon(), editDTO.getLocation());
		if (dao.isManifestationOverlapping(manifestation.getDate(), location, intId)) {
			throw new ManifestationExistsException("Manifestation for that date and location exists");
		}

		ManifestationType type = ManifestationType.valueOf(editDTO.getType());

		manifestation.setPoster(editDTO.getPoster());
		manifestation.setLocation(location);
		manifestation.setName(editDTO.getName());
		manifestation.setType(type);
		dao.addManifestation(manifestation);
		dao.writeAllManifetations();
		return manifestation;
	}

	@GET
	@Path("/approve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation approveComment(@PathParam("id") String id) {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.ADMINISTRATOR)
			throw new UnauthorizedUserException("Unauthorized action");

		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("manifestationDAO");

		int intId = -1;
		try {

			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			throw new NumberFormatException();
		}

		Manifestation manifestation = manifestationDAO.findById(intId);
		if (manifestation == null)
			throw new CommentNotFoundException("Comment with the id " + id + " not found");

		if (manifestation.getStatus() == ManifestationStatus.ACTIVE
				|| manifestation.getDate().isBefore(LocalDateTime.now()))
			throw new InvalidInputException("Unable to set status to active. Status is already active or date passed");

		manifestation.setStatus(ManifestationStatus.ACTIVE);
		manifestationDAO.addManifestation(manifestation);
		manifestationDAO.writeAllManifetations();
		return manifestation;
	}

	@GET
	@Path("/searchManifestations/{Name}/{Location}/{DateFrom}/{DateTo}/{PriceFrom}/{PriceTo}/{Type}/{SoldOut}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> searchManifestations(@PathParam("Name") String Name,
			@PathParam("Location") String Location, @PathParam("DateFrom") String DateFrom,
			@PathParam("DateTo") String DateTo, @PathParam("PriceFrom") String PriceFrom,
			@PathParam("PriceTo") String PriceTo, @PathParam("Type") String Type,
			@PathParam("SoldOut") String SoldOut) {

		String name;
		String location;
		if (Name.equals("null"))
			name = "";
		else
			name = Name;

		if (Location.equals("null"))
			location = "";
		else
			location = Location;
		LocalDate dateFrom = null;
		try {
			dateFrom = LocalDate.parse(DateFrom);
		} catch (Exception e) {
		}
		LocalDate dateTo = null;
		try {
			dateTo = LocalDate.parse(DateTo);
		} catch (Exception e) {
		}

		double priceFrom = -1;
		double priceTo = 1000000;
		try {
			priceFrom = Double.valueOf(PriceFrom);
		} catch (Exception e) {
		}
		try {
			priceTo = Double.valueOf(PriceTo);
		} catch (Exception e) {
		}

		int type = -1;
		int soldOut = -1;
		try {
			type = Integer.parseInt(Type);
			soldOut = Integer.parseInt(SoldOut);
		} catch (Exception e) {
		}

		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");

		List<Manifestation> allManifestations = dao.findAllList();
		List<Manifestation> filteredManifestations = new ArrayList<Manifestation>();
		for (Manifestation m : allManifestations) {
			if (!m.getName().toLowerCase().contains(name.toLowerCase()))
				continue; 
			if (!m.getLocation().getAddress().toLowerCase().contains(location.toLowerCase()))
				continue; 
			if (dateFrom != null && m.getDate().isBefore(LocalDateTime.of(dateFrom, LocalTime.now())))
				continue;
			if (dateTo != null && m.getDate().isAfter(LocalDateTime.of(dateTo, LocalTime.now())))
				continue;
			if (priceFrom > m.getRegularPrice())
				continue;
			if (priceTo < m.getRegularPrice())
				continue;
			if (type != -1 && m.getType().ordinal() != type)
				continue;
			if (soldOut != -1 && m.getLeftSeats() <= 0)
				continue;

			filteredManifestations.add(m);
		}
		return filteredManifestations;
	}

	@GET
	@Path("/rating/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public double getManifestationRating(@PathParam("id") String id) {
		ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
		int intId = -1;
		try {

			intId = Integer.parseInt(id);

		} catch (NumberFormatException e) {
			System.out.println(id + " is not a valid integer number");
			return -1;
		}

		Manifestation manifestation = dao.findById(intId);
		if (manifestation == null)
			throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");

		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");

		double score = 0;
		int i = 0;

		for (Comment comment : commentDAO.findAllList()) {
			if (comment.getManifestation().getId() == manifestation.getId()) {
				i++;
				score += comment.getRating();
			}
		}
		if (i == 0)
			return 0;
		else
			return score / i;
	}
}
