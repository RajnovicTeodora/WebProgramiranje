package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.CustomerKind;
import beans.Gender;
import beans.RegisteredUser;
import beans.Ticket;
import beans.UserRole;

public class RegisteredUserDAO {
	private Map<String, RegisteredUser> registeredUsers = new HashMap<>();

	public RegisteredUserDAO() {

	}

	public RegisteredUserDAO(String contextPath) {
		loadRegisteredUsers(contextPath);
	}

	public RegisteredUser find(String username, String password) {
		if (!registeredUsers.containsKey(username)) {
			return null;
		}
		RegisteredUser user = registeredUsers.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}

	public RegisteredUser findByUsername(String username) {
		if (!registeredUsers.containsKey(username)) {
			return null;
		}
		return registeredUsers.get(username);
	}

	public RegisteredUser addRegisteredUser(RegisteredUser registeredUser) {
		registeredUsers.put(registeredUser.getUsername(), registeredUser);
		return registeredUser;
	}

	public Collection<RegisteredUser> findAll() {
		return registeredUsers.values();
	}

	private void loadRegisteredUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/registeredUsers.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = Gender.valueOf(st.nextToken().trim());
					LocalDate date = LocalDate.parse(st.nextToken().trim());
					List<Ticket> tickets = new ArrayList<Ticket>();

					// TODO load 
					registeredUsers.put(username, new RegisteredUser(username, password, firstName, lastName, gender,
							date, UserRole.USER, CustomerKind.NEWBIE, tickets, 0, false));
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
