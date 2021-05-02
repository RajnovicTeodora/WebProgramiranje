package beans;

import java.time.LocalDate;
import java.util.List;

public class RegisteredUser extends User{

	private List<Ticket> tickets;
	private int points;
	private Boolean deleted;

	public RegisteredUser() {
		super();
	}

	

	public RegisteredUser(String username, String password, String firstName, String lastName, Gender gender,
			LocalDate birthday, UserRole role, CustomerKind customerType, List<Ticket> tickets, int points,
			Boolean deleted) {
		super(username, password, firstName, lastName, gender, birthday, role, customerType);
		this.deleted = deleted;
		this.points = points;
		this.tickets = tickets;	
	}



	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String toCSVString() {
		String ticketList = "";
		for (Ticket ticket : this.tickets) {
			ticketList += ticket.getId() + ";";
		}
		ticketList = ticketList.substring(0, ticketList.length() - 1);
		return super.toFileString() + ", " + ticketList + "," + points + "," + deleted;
	}
	
	

	
	
	
}
