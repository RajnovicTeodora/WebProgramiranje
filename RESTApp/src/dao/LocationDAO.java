package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import beans.Location;

public class LocationDAO {
	
	private Map<Integer, Location> locations = new HashMap<Integer, Location>();

	public LocationDAO() {
		super();
	}

	public LocationDAO(String contextPath) {
		super();
		loadLocations(contextPath);
	}
	
	public Location findById(int id) {
		if (!locations.containsKey(id)) {
			return null;
		}
		return locations.get(id);
	}
	
	public Location addLocation(Location location) {
		if (findById(location.getId()) != null)
			return null;
		locations.put(location.getId(), location);
		return location;
	}
	
	public Collection<Location> findAll(){
		return locations.values();
	}
	
	public ArrayList<Location> findAllList() {
		ArrayList<Location> locationList = new ArrayList<Location>();
		for (Location location : findAll()) {
			locationList.add(location);
		}
		return locationList;
	}
	
	private void loadLocations(String contextPath) {
		
		BufferedReader bufferedReader = null;
		try {

			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\locations.csv"); 
			bufferedReader = new BufferedReader(reader);
			String line;
			
			line =  bufferedReader.readLine();
			
			while (line != null){
				
				if ( line.charAt(0) == '#') {
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] st = line.split(";");
				
				int id = Integer.parseInt(st[0].trim());
				double lat = Double.valueOf(st[1].trim());
				double longitude = Double.valueOf(st[2].trim());
				String address = st[3].trim();
				
				Location l = new Location(id, lat, longitude, address);
				addLocation(l);
				
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
