package beans;

import java.time.LocalDate;

public class Administrator extends User {

	public Administrator() {
		super();

	}

	public Administrator(String username, String password, String name, String lastName, Gender gender,
			LocalDate birthday, UserRole role, CustomerKind customerType) {
		super(username, password, name, lastName, gender, birthday, role, customerType);

	}

}
