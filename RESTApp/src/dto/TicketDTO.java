package dto;

import beans.Ticket;

public class TicketDTO {
	private String id;
	private String manifestation;
	private String date;
	private double price;
	private String type;
	private String status;
	
	public TicketDTO (Ticket ticket) {
		this.id = ticket.getId();
		this.manifestation = ticket.getManifestation().getName();
		this.date = ticket.getDate().toString();
		this.price = ticket.getPrice();
		this.type = ticket.getType().toString();
		this.status = ticket.getStatus().toString();
	}
	
	public TicketDTO(String id, String manifestation, String date, double price, String type, String status) {
		super();
		this.id = id;
		this.manifestation = manifestation;
		this.date = date;
		this.price = price;
		this.type = type;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getManifestation() {
		return manifestation;
	}
	public void setManifestation(String manifestation) {
		this.manifestation = manifestation;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
