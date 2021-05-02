package beans;

public class Location {
	private double latitude; // sirina
	private double longitude; // duzina
	private String address; // ulica i broj, mjesto/grad, posanski broj

	public Location() {
		super();
	}

	public Location(double latitude, double longitude, String address) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
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

}