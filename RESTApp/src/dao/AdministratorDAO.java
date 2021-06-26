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

import beans.Administrator;
import beans.CustomerKind;
import beans.Gender;
import beans.Manifestation;
import beans.RegisteredUser;
import beans.Ticket;
import beans.UserRole;
import beans.Vendor;

public class AdministratorDAO {

	private Map<String, Administrator> administrators = new HashMap<>();

	public AdministratorDAO() {

	}

	public AdministratorDAO(String contextPath) {
		loadAdministrators(contextPath);
	}

	public Administrator find(String username, String password) {
		if (!administrators.containsKey(username)) {
			return null;
		}
		Administrator administrator = administrators.get(username);
		if (!administrator.getPassword().equals(password)) {
			return null;
		}
		return administrator;
	}

	public Administrator findByUsername(String username) {
		if (!administrators.containsKey(username)) {
			return null;
		}
		return administrators.get(username);
	}

	public Administrator addAdministrator(Administrator administrator) {
		administrators.put(administrator.getUsername(), administrator);
		return administrator;
	}

	public Collection<Administrator> findAll() {
		return administrators.values();
	}

	private void loadAdministrators(String contextPath) {
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
				UserRole role = UserRole.values()[Integer.valueOf(st[6])];
				if(role != UserRole.ADMINISTRATOR) {
					line = bufferedReader.readLine();
					continue;
				}
				
				String username = st[0].trim();
				String password = st[1].trim();
				String firstName = st[2].trim();
				String lastName = st[3].trim();
				Gender gender = Gender.values()[Integer.valueOf(st[4])];
				LocalDate date = LocalDate.parse(st[5]);				
				CustomerKind kind = CustomerKind.values()[Integer.valueOf(st[7])];
				
				Administrator admin = new Administrator(username, password, firstName, lastName, gender, date, role, kind);
				addAdministrator(admin);
				
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
