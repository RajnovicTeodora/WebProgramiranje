package dto;

import java.util.List;

import beans.Gender;
import beans.RegisteredUser;
import beans.User;
import beans.UserRole;

public class UserProfileDTO {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private String birthday;
	private List<TicketDTO> tickets;
	private String role;
	private int points;

	public UserProfileDTO(User user, List<TicketDTO> tickets) {
		super();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.gender = user.getGender();
		this.birthday = user.getBirthday().toString();
		this.tickets = tickets;
		this.role = user.getRole().toString();

		if (user.getRole() == UserRole.USER)
			this.points = ((RegisteredUser) user).getPoints();
		else
			this.points = 0;
	}

	public UserProfileDTO(String username, String password, String firstName, String lastName, Gender gender,
			String birthday, List<TicketDTO> tickets, String role) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.tickets = tickets;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public List<TicketDTO> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketDTO> tickets) {
		this.tickets = tickets;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
