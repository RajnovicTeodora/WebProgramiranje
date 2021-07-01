package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import beans.CustomerKind;
import beans.Kind;

public class CustomerKindDAO {
	
	private Map<String, Kind> customerKinds = new HashMap<>();
	private String contextPath = "";
	
	public CustomerKindDAO(String contextPath) {
		this.contextPath = contextPath;
		loadAll(contextPath);
	}

	public CustomerKindDAO() {

	}
	
	public Kind addKind(Kind kind) {
		customerKinds.put(kind.getName().name(), kind);
		return kind;
	}
	
	public int getPoints(CustomerKind customerKind) {
		if(customerKinds.containsKey(customerKind.name())) {
			return customerKinds.get(customerKind.name()).getPoints();
		}
		return -1;
	}
	
	public double  getDiscount(CustomerKind customerKind) {
		if(customerKinds.containsKey(customerKind.name())) {
			return customerKinds.get(customerKind.name()).getDiscount();
		}
		return -1;
	}
	
	private void loadAll(String contextPath) {
		BufferedReader bufferedReader = null;
		try {

			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\kinds.csv");
			bufferedReader = new BufferedReader(reader);
			String line;

			line = bufferedReader.readLine();

			while (line != null) {

				if (line.charAt(0) == '#') {
					line = bufferedReader.readLine();
					continue;
				}

				String[] st = line.split(";");

				CustomerKind name = CustomerKind.values()[Integer.valueOf(st[0])];
				int points = Integer.valueOf(st[1]);
				double discount = Double.valueOf(st[2]);
				
				addKind(new Kind(name, points, discount));

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
