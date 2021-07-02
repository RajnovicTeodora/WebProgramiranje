package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

	public double getPoints(CustomerKind customerKind) {
		if (customerKinds.containsKey(customerKind.name())) {
			return customerKinds.get(customerKind.name()).getPoints();
		}
		return -1;
	}

	public int getDiscount(CustomerKind customerKind) {
		
		if (customerKinds.containsKey(customerKind.name())) {
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
				double points = Double.valueOf(st[1]);
				int discount = Integer.valueOf(st[2]);

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

	public Collection<Kind> findAll() {
		return customerKinds.values();
	}

	public ArrayList<Kind> findAllList() {
		ArrayList<Kind> kindList = new ArrayList<Kind>();
		for (Kind kind : findAll()) {
			kindList.add(kind);
		}
		return kindList;
	}

	public CustomerKind getKindFromPoints(double points) {
		ArrayList<Kind> kinds = findAllList();
		Collections.sort(kinds, new Comparator<Kind>() {
			@Override
			public int compare(Kind k1, Kind k2) {
				if (k1.getPoints() >= k2.getPoints())
					return 0;
				else
					return -1;
			}
		});

		// [ , ) ...
		for (int i = 0; i < kinds.size(); i++) {
			if (points >= kinds.get(i).getPoints()) {
				if (i + 1 != kinds.size()) {
					if (points < kinds.get(i + 1).getPoints())
						return kinds.get(i).getName();
				} else {
					return kinds.get(i).getName();
				}
			}
		}
		return CustomerKind.NEWBIE;
	}

}
