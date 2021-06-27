package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.ManifestationStatus;
import beans.RegisteredUser;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.User;
import beans.UserRole;
import beans.Vendor;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dto.ReservationDTO;
import dto.TicketDTO;
import exception.InvalidInputException;
import exception.ManifestationNotFoundException;
import exception.TicketNotFoundEception;
import exception.UnauthorizedUserException;
import exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

@Path("/tickets")
public class TicketService {

	@Context
	ServletContext ctx;

	public TicketService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("ticketDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("ticketDAO", new TicketDAO(contextPath));
		}

	}

	@GET
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ticket cancelTicket(@PathParam("id") String id) {

		RegisteredUser user = (RegisteredUser) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		TicketDAO dao = (TicketDAO) ctx.getAttribute("ticketDAO");

		Ticket ticket = dao.findById(id);
		if (ticket == null)
			throw new TicketNotFoundEception("Ticket with the id " + id + " not found.");

		ticket.setStatus(TicketStatus.CANCELED);

		ticket.getManifestation().setNumSeats(ticket.getManifestation().getLeftSeats() + 1);

		user.setPoints((int) (user.getPoints() - ticket.getPrice() / 1000 * 133 * 4));
		return ticket;
		// TODO really cancel manifestation and save
	}

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TicketDTO> getTickets() {

		User user = (User) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		TicketDAO dao = (TicketDAO) ctx.getAttribute("ticketDAO");

		List<TicketDTO> tickets = new ArrayList<TicketDTO>();

		if (user.getRole() == UserRole.USER) {
			for (Ticket ticket : ((RegisteredUser) user).getTickets()) {
				if (ticket.getStatus() == TicketStatus.RESERVED) {
				
					TicketDTO dto = new TicketDTO(ticket);
					dto.setStatus(dto.getStatus().substring(0, 1) + dto.getStatus().toLowerCase().substring(1));
					dto.setType(dto.getType().substring(0, 1) + dto.getType().toLowerCase().substring(1));
		
					tickets.add(dto);
				}

			}
		} else if (user.getRole() == UserRole.VENDOR) {
			for (Ticket ticket : dao.findAllList()) {
				if (ticket.getStatus() == TicketStatus.RESERVED
						&& ((Vendor) user).getManifestations().contains(ticket.getManifestation())) {

					TicketDTO dto = new TicketDTO(ticket);
					dto.setStatus(dto.getStatus().substring(0, 1) + dto.getStatus().toLowerCase().substring(1));
					dto.setType(dto.getType().substring(0, 1) + dto.getType().toLowerCase().substring(1));
	
					tickets.add(dto);
				}

			}
		} else if (user.getRole() == UserRole.ADMINISTRATOR) {
			for (Ticket ticket : dao.findAllList()) {

				TicketDTO dto = new TicketDTO(ticket);
				dto.setStatus(dto.getStatus().substring(0, 1) + dto.getStatus().toLowerCase().substring(1));
				dto.setType(dto.getType().substring(0, 1) + dto.getType().toLowerCase().substring(1));
				
				tickets.add(dto);
			}
		}

		return tickets;
	}

	// Reserve manifestation ticket
	@POST
	@Path("/reserve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> reserveTickets(ReservationDTO reservationDTO) {

		RegisteredUser user = (RegisteredUser) ctx.getAttribute("registeredUser");
		if (user == null)
			throw new UserNotFoundException("No user registered");

		if (user.getRole() != UserRole.USER)
			throw new UnauthorizedUserException("Registered user must be of type user");

		TicketDAO ticketDao = (TicketDAO) ctx.getAttribute("ticketDAO");
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");

		Manifestation manifestation = manifestationDao.findById(reservationDTO.getId());
		if (manifestation == null)
			throw new ManifestationNotFoundException(
					"Manifestation with the id " + reservationDTO.getId() + " not found");

		TicketType ticketType = TicketType.valueOf(reservationDTO.getTicketType());
		int numTickets = reservationDTO.getNumTickets();
		double price = reservationDTO.getPrice();

		if (numTickets <= 0 || numTickets > manifestation.getLeftSeats())
			throw new InvalidInputException("Number of reserved tickets is invalid");

		if (manifestation.getStatus() != ManifestationStatus.ACTIVE
				|| manifestation.getDate().isBefore(LocalDateTime.now()))
			throw new InvalidInputException("Manifestation status must be active");

		String ticketId = ticketDao.findId();
		String buyer = user.getFirstName() + " " + user.getLastName();

		List<Ticket> tickets = user.getTickets();

		manifestation.setLeftSeats(manifestation.getLeftSeats() - numTickets);
		user.setPoints((int) (user.getPoints() + price / 1000 * 133 * numTickets));

		while (numTickets > 0) {
			Ticket ticket = new Ticket(ticketId, manifestation, manifestation.getDate(), price, buyer,
					TicketStatus.RESERVED, ticketType);
			ticket.setBuyerUsername(user.getUsername());
			tickets.add(ticket);
			ticketDao.writeTicket(ticket);
			ticketDao.addTicket(ticket);
			numTickets--;
		}

		user.setTickets(tickets);

		return tickets;

	}

}
