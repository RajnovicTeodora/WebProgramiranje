package dto;

import java.time.LocalDateTime;

public class NewManifestationDTO {

	private String name;
	private String type; // concert, festival, theater..
	private int numSeats;
	private LocalDateTime date;
	private double regularPrice;
	private String location;
	private double lat;
	private double lon;
	private String poster;

	
	
	public NewManifestationDTO() {
		super();
	}

	public NewManifestationDTO(String name, String type, int numSeats, LocalDateTime date, double regularPrice,
			String location, double lat, double lon, String poster) {
		super();
		this.name = name;
		this.type = type;
		this.numSeats = numSeats;
		this.date = date;
		this.regularPrice = regularPrice;
		this.location = location;
		this.lat = lat;
		this.lon = lon;
		this.poster = poster;
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

	public int getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(double regularPrice) {
		this.regularPrice = regularPrice;
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
