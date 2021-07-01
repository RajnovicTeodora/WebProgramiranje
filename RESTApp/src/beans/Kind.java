package beans;

public class Kind {
	
	private CustomerKind name;
	private int points;
	private double discount;
	
	public Kind() {
		super();
	}

	public Kind(CustomerKind name, int points, double discount) {
		super();
		this.name = name;
		this.points = points;
		this.discount = discount;
	}

	public CustomerKind getName() {
		return name;
	}

	public void setName(CustomerKind name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	

}
