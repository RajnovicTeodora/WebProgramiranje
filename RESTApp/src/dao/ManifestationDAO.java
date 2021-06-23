package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationStatus;
import beans.ManifestationType;

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

	private void loadManifestations(String contextPath) {

		Location l1 = new Location(50.84673, 4.35247, "Mainski Put 2, Budva, Crna Gora");

		Manifestation m1 = new Manifestation("Manifestation1", ManifestationType.FESTIVAL, 20,
				LocalDateTime.now().plusDays(1), 10, ManifestationStatus.ACTIVE, l1, "ticket.png");
		Manifestation m2 = new Manifestation("Manifestation2", ManifestationType.FESTIVAL, 20,
				LocalDateTime.now().plusDays(3), 10, ManifestationStatus.ACTIVE, l1, "ticket.png");
		Manifestation m3 = new Manifestation("Manifestation3", ManifestationType.FESTIVAL, 20, LocalDateTime.now(), 10,
				ManifestationStatus.ACTIVE, l1, "ticket.png");
		m1.setId(1);
		m2.setId(2);
		m3.setId(3);

		addManifestation(m1);
		addManifestation(m2);
		addManifestation(m3);

	}
}
