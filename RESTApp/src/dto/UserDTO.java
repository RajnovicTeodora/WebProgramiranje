package dto;

import beans.RegisteredUser;
import beans.User;
import beans.UserRole;

public class UserDTO {
	private String username;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthday;
	private String role;
	private String customerType;
	private int points;
	
	public UserDTO(User user) {
		this.username = user.getUsername();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.gender = user.getGender().toString().substring(0, 1) + user.getGender().toString().substring(1).toLowerCase();
		this.birthday = user.getBirthday().toString();
		this.role = user.getRole().toString().substring(0, 1) + user.getRole().toString().substring(1).toLowerCase();
		this.customerType = user.getCustomerType().toString().substring(0, 1) + user.getCustomerType().toString().substring(1).toLowerCase();
		
		if(user.getRole() == UserRole.USER) {
			this.points = ((RegisteredUser) user).getPoints();
		}else {
			this.points = 0;
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
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
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
}
