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
	private Map<String, User> blockedUsers = new HashMap<>();
	private List<User> deletedUsers = new ArrayList<User>();

	String contextPath = "";

	public RegisteredUserDAO() {

	}

	public RegisteredUserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadRegisteredUsers(contextPath);
		loadBlockedUsers(contextPath);
		loadDeletedUsers(contextPath);
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
		if (registeredUsers.containsKey(username)) {
			return registeredUsers.get(username);
		}
		if (blockedUsers.containsKey(username)) {
			return blockedUsers.get(username);
		}
		return null;
	}

	public User updateUser(User user) {
		return registeredUsers.replace(user.getUsername(), user);
	}

	public User addRegisteredUser(User registeredUser) {
		registeredUsers.put(registeredUser.getUsername(), registeredUser);
		removeBlockedUser(registeredUser);
		return registeredUser;
	}

	public User addBlockedUser(User blockedUser) {
		blockedUsers.put(blockedUser.getUsername(), blockedUser);
		removeRegisteredUser(blockedUser);
		return blockedUser;
	}

	public User addDeletedUser(User user) {
		deletedUsers.add(user);
		removeRegisteredUser(user);
		removeBlockedUser(user);
		return user;
	}

	public Collection<User> findAll() {
		return registeredUsers.values();
	}

	public Collection<User> findAllBlocked() {
		return blockedUsers.values();
	}

	public Collection<User> findAllDeleted() {
		return deletedUsers;
	}

	public ArrayList<User> findAllList() {
		ArrayList<User> userList = new ArrayList<User>();
		for (User user : findAll()) {
			userList.add(user);
		}
		return userList;
	}

	public ArrayList<User> findAllBlockedList() {
		ArrayList<User> userList = new ArrayList<User>();
		for (User user : findAllBlocked()) {
			userList.add(user);
		}
		return userList;
	}

	public ArrayList<User> findAllDeletedList() {
		ArrayList<User> userList = new ArrayList<User>();
		for (User user : findAllDeleted()) {
			userList.add(user);
		}
		return userList;
	}

	public ArrayList<User> findAllAdminList() {
		ArrayList<User> userList = new ArrayList<User>();
		for (User user : findAll()) {
			userList.add(user);
		}
		for (User user : findAllBlocked()) {
			userList.add(user);
		}
		for (User user : findAllDeleted()) {
			userList.add(user);
		}
		return userList;
	}

	public void removeRegisteredUser(User user) {
		if (registeredUsers.containsKey(user.getUsername())) {
			registeredUsers.remove(user.getUsername());
		}
	}

	public void removeBlockedUser(User user) {
		if (blockedUsers.containsKey(user.getUsername())) {
			blockedUsers.remove(user.getUsername());
		}
	}

	public boolean isUserBlocked(User user) {
		if (blockedUsers.containsKey(user.getUsername()))
			return true;
		return false;
	}

	private void loadRegisteredUsers(String contextPath) {
		BufferedReader bufferedReader = null;
		try {
			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\users.csv");
			bufferedReader = new BufferedReader(reader);
			String line;

			line = bufferedReader.readLine();

			while (line != null) {

				if (line.charAt(0) == '#') {
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

				if (role == UserRole.ADMINISTRATOR) {
					Administrator admin = new Administrator(username, password, firstName, lastName, gender, date, role,
							kind);
					addRegisteredUser(admin);
				}

				if (role == UserRole.VENDOR) {
					Vendor v = new Vendor(username, password, firstName, lastName, gender, date, role, kind);
					v.setManifestations(ToiToiDAO.getVendorManifestations(contextPath, username));
					addRegisteredUser(v);
				}

				if (role == UserRole.USER) {
					List<Ticket> tickets = ToiToiDAO.getUserTickets(contextPath, username);
					double points = Double.valueOf(st[8]);
					RegisteredUser u = new RegisteredUser(username, password, firstName, lastName, gender, date, role,
							kind, tickets, points, false);
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

	public boolean deleteUser(User user) {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\deletedUsers.csv", true);
			writer.write(user.toCsvString());
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean writeAllUsers() {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\users.csv", false);

			for (User u : findAllList()) {
				writer.write(u.toCsvString());
			}
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean writeBlockedUsers() {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\blockedUsers.csv", false);

			for (User u : findAllBlockedList()) {
				writer.write(u.toCsvString());
			}
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void loadBlockedUsers(String contextPath) {
		BufferedReader bufferedReader = null;
		try {
			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\blockedUsers.csv");
			bufferedReader = new BufferedReader(reader);
			String line;

			line = bufferedReader.readLine();

			while (line != null) {

				if (line.charAt(0) == '#') {
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

				if (role == UserRole.ADMINISTRATOR) {
					Administrator admin = new Administrator(username, password, firstName, lastName, gender, date, role,
							kind);
					addBlockedUser(admin);
				}

				if (role == UserRole.VENDOR) {
					Vendor v = new Vendor(username, password, firstName, lastName, gender, date, role, kind);
					v.setManifestations(ToiToiDAO.getVendorManifestations(contextPath, username));
					addBlockedUser(v);
				}

				if (role == UserRole.USER) {
					List<Ticket> tickets = ToiToiDAO.getUserTickets(contextPath, username);
					int points = Integer.valueOf(st[8]);
					RegisteredUser u = new RegisteredUser(username, password, firstName, lastName, gender, date, role,
							kind, tickets, points, false);
					addBlockedUser(u);
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

	private void loadDeletedUsers(String contextPath) {
		BufferedReader bufferedReader = null;
		try {
			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\deletedUsers.csv");
			bufferedReader = new BufferedReader(reader);
			String line;

			line = bufferedReader.readLine();

			while (line != null) {

				if (line.charAt(0) == '#') {
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

				if (role == UserRole.VENDOR) {
					Vendor v = new Vendor(username, password, firstName, lastName, gender, date, role, kind);
					addDeletedUser(v);
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
}
