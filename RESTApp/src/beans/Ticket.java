package beans;

import java.time.LocalDateTime;

public class Ticket {
	private String id; //10 chars
	private Manifestation manifestation;
	private LocalDateTime date;
	private double price;
	private String buyer; // name + surname
	private String buyerUsername;
	private TicketStatus status; //reserved, canceled
	private TicketType type; // VIP, regular, fan pit
	
	public Ticket() {
		super();
	}

	public Ticket(String id, Manifestation manifestation, LocalDateTime date, double price, String buyer,
			TicketStatus status, TicketType type) {
		super();
		this.id = id;
		this.manifestation = manifestation;
		this.date = date;
		this.price = price;
		this.buyer = buyer;
		this.status = status;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Manifestation getManifestation() {
		return manifestation;
	}

	public void setManifestation(Manifestation manifestation) {
		this.manifestation = manifestation;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public TicketType getType() {
		return type;
	}

	public void setType(TicketType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", manifestation=" + manifestation + ", date=" + date + ", price=" + price
				+ ", buyer=" + buyer + ", status=" + status + ", type=" + type + "]";
	}

	public String getBuyerUsername() {
		return buyerUsername;
	}

	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}
	
	public String toCsvString() {
		String[] elems = {String.valueOf(this.id),String.valueOf(this.getManifestation().getId()), this.date.toString(), String.valueOf(this.price), this.buyer, this.buyerUsername, String.valueOf(this.status.ordinal()), String.valueOf(this.type.ordinal())};
		return String.join(";", elems)+"\n";	
	}
	
}
