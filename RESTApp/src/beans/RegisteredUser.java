package beans;

import java.time.LocalDate;
import java.util.List;

public class RegisteredUser extends User {

	private List<Ticket> tickets;
	private double points;
	private Boolean deleted;

	public RegisteredUser() {
		super();
	}

	public RegisteredUser(String username, String password, String firstName, String lastName, Gender gender,
			LocalDate birthday, UserRole role, CustomerKind customerType, List<Ticket> tickets, double points,
			Boolean deleted) {
		super(username, password, firstName, lastName, gender, birthday, role, customerType);
		this.deleted = deleted;
		this.points = points;
		this.tickets = tickets;
	}
	
	@Override
	public String toCsvString() {
		String[] elems = {this.getUsername(),this.getPassword(),this.getFirstName(),this.getLastName(),String.valueOf(this.getGender().ordinal()),this.getBirthday().toString(),
				String.valueOf(this.getRole().ordinal()), String.valueOf(this.getCustomerType().ordinal()), String.valueOf(points)};
		return String.join(";",elems)+"\n";
	}
	
	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
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

		if (this.tickets != null && this.tickets.size() > 0) {

			for (Ticket ticket : this.tickets) {
				ticketList += ticket.getId() + ";";
			}
		}
		ticketList = ticketList.substring(0, ticketList.length() - 1);
		return super.toCSVString() + ", " + ticketList + "," + points + "," + deleted;
	}
	
}
