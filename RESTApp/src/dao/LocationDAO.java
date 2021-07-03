package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import beans.Comment;
import beans.Location;

public class LocationDAO {
	
	private Map<Integer, Location> locations = new HashMap<Integer, Location>();
	private String contextPath = "";

	public LocationDAO() {
		super();
	}

	public LocationDAO(String contextPath) {
		super();
		this.contextPath = contextPath;
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
	
	public int findNextId() {
		if(locations.size()==0) return 1;
		int lastId = (Integer)new TreeSet<Integer>(locations.keySet()).last();
		return lastId+1;
	}
	
	public boolean writeLocation(Location location) {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\locations.csv", true);
			writer.write(location.toCsvString());
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 		
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
