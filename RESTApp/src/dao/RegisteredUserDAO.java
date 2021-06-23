package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Administrator;
import beans.CustomerKind;
import beans.Gender;
import beans.RegisteredUser;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.User;
import beans.UserRole;
import beans.Vendor;

public class RegisteredUserDAO {
	private Map<String, User> registeredUsers = new HashMap<>();

	private String contextPath;

	public RegisteredUserDAO() {
		Administrator a = new Administrator("admin", "admin", "admin", "admin", Gender.FEMALE, LocalDate.now(), UserRole.ADMINISTRATOR, CustomerKind.CHAMP); 
		addRegisteredUser(a);
		RegisteredUser u = new RegisteredUser("cao", "cao", "cao", "cao", Gender.FEMALE, LocalDate.now(), UserRole.USER, CustomerKind.CHAMP, new ArrayList<Ticket>(), 10 , false); 
		addRegisteredUser(u);
	}

	public RegisteredUserDAO(String contextPath) {
		this.contextPath = contextPath;
		Administrator a = new Administrator("admin", "admin", "admin", "admin", Gender.FEMALE, LocalDate.now(), UserRole.ADMINISTRATOR, CustomerKind.CHAMP); 
		addRegisteredUser(a);
		RegisteredUser u = new RegisteredUser("cao", "cao", "cao", "cao", Gender.FEMALE, LocalDate.now(), UserRole.USER, CustomerKind.CHAMP, new ArrayList<Ticket>(), 10 , false); 
		addRegisteredUser(u);
		Vendor v = new Vendor("vendor", "vendor", "vendor", "vendor", Gender.FEMALE, LocalDate.now(), UserRole.VENDOR, CustomerKind.CHAMP); 
		addRegisteredUser(v);
	}

	public User find(String username, String password) {
		if (!registeredUsers.containsKey(username)) {
			return null;
		}
		User user = registeredUsers.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}

	public User findByUsername(String username) {
		if (!registeredUsers.containsKey(username)) {
			return null;
		}
		return registeredUsers.get(username);
	}
	
	public User updateUser(User user) {
		
		return registeredUsers.replace(user.getUsername(), user);
	}

	public User addRegisteredUser(User registeredUser) {
		registeredUsers.put(registeredUser.getUsername(), registeredUser);
		return registeredUser;
	}

	public Collection<User> findAll() {
		return registeredUsers.values();
	}

	public ArrayList<User> findAllList(){
		ArrayList<User> userList = new ArrayList<User>();
		for (User user : findAll()) {
			userList.add(user);
		}
		return userList;
	}
	
	private void loadRegisteredUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/user.csv");
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
