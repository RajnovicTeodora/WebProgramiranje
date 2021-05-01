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

	public RegisteredUser(String username, String password, String name, String lastName, Gender gender,
			LocalDate birthday, UserRole role, CustomerKind customerType, List<Ticket> tickets, int points,
			Boolean deleted) {
		super(username, password, name, lastName, gender, birthday, role, customerType);
		this.tickets = tickets;
		this.points = points;
		this.deleted = deleted;
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

}
