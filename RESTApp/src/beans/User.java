package beans;

import java.time.LocalDate;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private LocalDate birthday;
	private UserRole role;
	private CustomerKind customerType;

	public User() {
		super();
	}
	

	public User(String username, String password, String firstName, String lastName, Gender gender, LocalDate birthday,
			UserRole role, CustomerKind customerType) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.customerType = customerType;
	}
	
	public String toCsvString() {
		String[] elems = {this.getUsername(),this.getPassword(),this.getFirstName(),this.getLastName(),String.valueOf(this.getGender().ordinal()),this.getBirthday().toString(),
				String.valueOf(this.getRole().ordinal()), String.valueOf(this.getCustomerType().ordinal())};
		return String.join(";",elems)+"\n";
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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public CustomerKind getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerKind customerType) {
		this.customerType = customerType;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", gender=" + gender + ", birthday=" + birthday + ", role=" + role + ", customerType="
				+ customerType + "]";
	}
	
	public String toCSVString() {
		return username + "," + password + "," + firstName + "," + lastName + "," + gender + "," + birthday + "," + role + "," + customerType;
	}


	

}
