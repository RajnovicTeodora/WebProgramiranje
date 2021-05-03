package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Administrator;
import beans.CustomerKind;
import beans.Gender;
import beans.UserRole;

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
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/Administrator.csv");
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

					administrators.put(username, new Administrator(username, password, firstName, lastName, gender,
							date, UserRole.USER, CustomerKind.NEWBIE));
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
