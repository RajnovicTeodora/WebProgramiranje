package dto;

public class EditManifestationDTO {
	private String id;
	private String name;
	private String type; // concert, festival, theater..
	private String location;
	private double lat;
	private double lon;
	private String poster;

	public EditManifestationDTO() {
		super();
	}

	public EditManifestationDTO(String id, String name, String type, String location, double lat, double lon,
			String poster) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.location = location;
		this.lat = lat;
		this.lon = lon;
		this.poster = poster;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

}
