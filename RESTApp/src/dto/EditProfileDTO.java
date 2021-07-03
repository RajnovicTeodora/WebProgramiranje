package dto;

public class EditProfileDTO {
	private String password;
	private String name;
	private String surname;

	public EditProfileDTO() {
		super();
	}

	public EditProfileDTO(String password, String name, String surname) {
		super();
		this.password = password;
		this.name = name;
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}
