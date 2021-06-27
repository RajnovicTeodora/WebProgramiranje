package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import beans.Administrator;
import beans.CustomerKind;
import beans.Gender;
import beans.RegisteredUser;
import beans.Ticket;
import beans.User;
import beans.UserRole;
import beans.Vendor;

public class RegisteredUserDAO {
	private Map<String, User> registeredUsers = new HashMap<>();
	
	String contextPath = "";

	public RegisteredUserDAO() {
		
	}

	public RegisteredUserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadRegisteredUsers(contextPath);
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
		//editUser(user);
		return registeredUsers.replace(user.getUsername(), user);
	}

	public User addRegisteredUser(User registeredUser) {
		//addUser(registeredUser);
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
		BufferedReader bufferedReader = null;
		try {		
			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\users.csv"); 
			bufferedReader = new BufferedReader(reader);
			String line;
			
			line =  bufferedReader.readLine();
			
			while (line != null){
				
				if ( line.charAt(0) == '#') {
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] st = line.split(";");
				
				String username = st[0].trim();
				String password = st[1].trim();
				String firstName = st[2].trim();
				String lastName = st[3].trim();
				Gender gender = Gender.values()[Integer.valueOf(st[4])];
				LocalDate date = LocalDate.parse(st[5]);				
				UserRole role = UserRole.values()[Integer.valueOf(st[6])];
				CustomerKind kind = CustomerKind.values()[Integer.valueOf(st[7])];
				
			
				if(role == UserRole.ADMINISTRATOR) {
					Administrator admin = new Administrator(username, password, firstName, lastName, gender, date, role, kind);
					addRegisteredUser(admin);
				}
				
				if(role == UserRole.VENDOR) {
					Vendor v = new Vendor(username, password, firstName, lastName, gender, date, role, kind);
					v.setManifestations(ToiToiDAO.getVendorManifestations(contextPath, username));
					addRegisteredUser(v);
				}
				
				if(role == UserRole.USER) {
					List<Ticket> tickets = ToiToiDAO.getUserTickets(contextPath, username);
					int points = Integer.valueOf(st[8]);
					RegisteredUser u = new RegisteredUser(username, password, firstName, lastName, gender, date, role, kind, tickets, points, false);
					addRegisteredUser(u);
				}
				line = bufferedReader.readLine();
			}			
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean addUser(User user) {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\users.csv", true);
			writer.write(user.toCsvString());
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 		
	}
	
	private void editUser(User user) {
		//TODO: file write
	}
}
