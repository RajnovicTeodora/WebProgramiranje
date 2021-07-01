package dto;

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
	private String role;
	private int points;
	private String kind;

	public UserProfileDTO(User user) {
		super();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.gender = user.getGender();
		this.birthday = user.getBirthday().toString();
		this.role = user.getRole().toString();

		if (user.getRole() == UserRole.USER) {
			this.points = ((RegisteredUser) user).getPoints();
			this.kind = ((RegisteredUser) user).getCustomerType().toString().substring(0, 1)
					+ ((RegisteredUser) user).getCustomerType().toString().substring(1).toLowerCase();
		} else {
			this.points = 0;
			this.kind = "";
		}
	}

	public UserProfileDTO(String username, String password, String firstName, String lastName, Gender gender,
			String birthday, String role, String kind) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.kind = kind;
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
