package beans;

import java.awt.Image;

public class Manifestation {
	private String name;
	private String type; //concert, festival, theater..
	private int numSeats; 
	private String date;
	private String time;
	private double regularPrice;
	private Location location;
	private Image poster;
	
	public Manifestation() {
		super();
	}

	public Manifestation(String name, String type, int numSeats, String date, String time, double regularPrice,
			Location location, Image poster) {
		super();
		this.name = name;
		this.type = type;
		this.numSeats = numSeats;
		this.date = date;
		this.time = time;
		this.regularPrice = regularPrice;
		this.location = location;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(double regularPrice) {
		this.regularPrice = regularPrice;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Image getPoster() {
		return poster;
	}

	public void setPoster(Image poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Manifestation [name=" + name + ", type=" + type + ", numSeats=" + numSeats + ", date=" + date
				+ ", time=" + time + ", regularPrice=" + regularPrice + ", location=" + location + ", poster=" + poster
				+ "]";
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
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numSeats != other.numSeats)
			return false;
		if (poster == null) {
			if (other.poster != null)
				return false;
		} else if (!poster.equals(other.poster))
			return false;
		if (Double.doubleToLongBits(regularPrice) != Double.doubleToLongBits(other.regularPrice))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
	

}
