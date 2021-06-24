package dto;

public class ReservationDTO {

	private int id;
	private int numTickets;
	private String ticketType;
	private double price;
	
	public ReservationDTO() {
		super();
	}
	
	public ReservationDTO(int id, int numTickets, String ticketType, double price) {
		super();
		this.id = id;
		this.numTickets = numTickets;
		this.ticketType = ticketType;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumTickets() {
		return numTickets;
	}
	public void setNumTickets(int numTickets) {
		this.numTickets = numTickets;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
