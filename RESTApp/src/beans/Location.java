package beans;

public class Location {
	private int id;
	private double latitude; // sirina
	private double longitude; // duzina
	private String address; // ulica i broj, mjesto/grad, postanski broj

	public Location() {
		super();
	}

	public Location(double latitude, double longitude, String address) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
	
	public Location(int id, double latitude, double longitude, String address) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude + ", address=" + address + "]";
	}
	
	public String toCsvString() {
		String[] elems = {String.valueOf(this.id), String.valueOf(this.latitude), String.valueOf(this.longitude), this.address};
		return String.join(";", elems)+"\n";	
	}

}
