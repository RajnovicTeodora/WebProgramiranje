package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import beans.Comment;
import beans.CommentStatus;
import beans.Location;
import beans.Manifestation;
import beans.ManifestationStatus;
import beans.ManifestationType;
import beans.RegisteredUser;

public class ManifestationDAO {
	private Map<Integer, Manifestation> manifestations = new HashMap<Integer, Manifestation>();

	private String contextPath;

	public ManifestationDAO() {

	}

	public ManifestationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadManifestations(contextPath);
	}

	public Manifestation findById(int id) {
		if (!manifestations.containsKey(id)) {
			return null;
		}
		return manifestations.get(id);
	}

	public Manifestation addManifestation(Manifestation manifestation) {
		if (findById(manifestation.getId()) != null)
			return null;
		manifestations.put(manifestation.getId(), manifestation);
		return manifestation;
	}

	public Collection<Manifestation> findAll() {
		return manifestations.values();
	}

	public ArrayList<Manifestation> findAllList() {
		ArrayList<Manifestation> manifestationList = new ArrayList<Manifestation>();
		for (Manifestation manifestation : findAll()) {
			manifestationList.add(manifestation);
		}
		return manifestationList;
	}
	
	public List<Manifestation> findByVendor(String username){
		List<Manifestation> manifestationList = new ArrayList<Manifestation>();
		for (Manifestation manifestation : findAll()) {
			if(manifestation.getVendorUsername().equals(username)) {
				manifestationList.add(manifestation);
			}		
		}
		return manifestationList;
	}

	public Boolean isManifestationOverlapping(LocalDateTime date, Location location, int id) {

		for (Manifestation manifestation : findAllList()) {
			if(manifestation.getId() != id) {
				if (manifestation.getDate().toLocalDate().equals(date.toLocalDate()) // if date overlaps
						&& (manifestation.getLocation().getAddress().equals(location.getAddress()) // and location
																									// overlaps
								|| (manifestation.getLocation().getLatitude() == location.getLatitude() // or lat and
																										// lon
										&& manifestation.getLocation().getLongitude() == location.getLongitude()))) {
					return true;
				}
			}
			
		}

		return false;
	}
	
	public int getNextId() {
		if(manifestations.size()==0) return 1;
		int lastId = (Integer)new TreeSet<Integer>(manifestations.keySet()).last();
		return lastId+1;
	}

	private void loadManifestations(String contextPath) {
		
		BufferedReader bufferedReader = null;
		try {

			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\manifestations.csv"); 
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
				String name = st[1].trim();
				ManifestationType type = ManifestationType.values()[Integer.valueOf(st[2])];
				int numSeats = Integer.valueOf(st[3].trim());
				LocalDateTime date = LocalDateTime.parse(st[4]);
				int price = Integer.valueOf(st[5].trim());
				ManifestationStatus status = ManifestationStatus.values()[Integer.valueOf(st[6])];
				Location location = ToiToiDAO.getManifestationLocation(contextPath, Integer.valueOf(st[7].trim()));
				String poster = st[8].trim();
				int leftSeats = Integer.valueOf(st[9].trim());
				String vendorUsername = st[10];
				
				Manifestation manifestation = new Manifestation(id, name, type, numSeats, date, price, status, location, poster, leftSeats);
				manifestation.setVendorUsername(vendorUsername);
				addManifestation(manifestation);
				
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
