package beans;

import java.time.LocalDate;
import java.util.List;

public class Vendor extends User {

	private List<Manifestation> manifestations;
	
	public Vendor() {
		super();
	}

	public Vendor(String username, String password, String name, String lastName, Gender gender, LocalDate birthday,
			UserRole role, CustomerKind customerType) {
		super(username, password, name, lastName, gender, birthday, role, customerType);

	}

	public List<Manifestation> getManifestations() {
		return manifestations;
	}

	public void setManifestations(List<Manifestation> manifestations) {
		this.manifestations = manifestations;
	}

}
