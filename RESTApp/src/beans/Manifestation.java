package beans;

import java.time.LocalDateTime;

public class Manifestation {
	private int id;
	private String name;
	private ManifestationType type; // concert, festival, theater..
	private int numSeats;
	private LocalDateTime date;
	private double regularPrice;
	private ManifestationStatus status;
	private Location location;
	private String poster;
	private int leftSeats;

	public Manifestation() {
		super();
	}

	public Manifestation(String name, ManifestationType type, int numSeats, LocalDateTime date, double regularPrice,
			ManifestationStatus status, Location location, String poster) {
		super();
		this.name = name;
		this.type = type;
		this.numSeats = numSeats;
		this.date = date;
		this.regularPrice = regularPrice;
		this.status = status;
		this.location = location;
		this.poster = poster;
		this.leftSeats = numSeats; // TODO
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ManifestationType getType() {
		return type;
	}

	public void setType(ManifestationType type) {
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

	public ManifestationStatus getStatus() {
		return status;
	}

	public void setStatus(ManifestationStatus status) {
		this.status = status;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Manifestation [name=" + name + ", type=" + type + ", numSeats=" + numSeats + ", date=" + date
				+ ", regularPrice=" + regularPrice + ", location=" + location + ", poster=" + poster + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numSeats;
		result = prime * result + ((poster == null) ? 0 : poster.hashCode());
		long temp;
		temp = Double.doubleToLongBits(regularPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manifestation other = (Manifestation) obj;
		if (id != other.id)
			return false;
		
		return true;
	}

	public int getLeftSeats() {
		return leftSeats;
	}

	public void setLeftSeats(int leftSeats) {
		this.leftSeats = leftSeats;
	}

}
