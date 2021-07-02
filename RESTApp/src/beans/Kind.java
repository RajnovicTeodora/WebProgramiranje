package beans;

public class Kind {

	private CustomerKind name;
	private double points;
	private int discount;

	public Kind() {
		super();
	}

	public Kind(CustomerKind name, double points, int discount) {
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

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "Kind [name=" + name + ", points=" + points + ", discount=" + discount + "]";
	}

}
