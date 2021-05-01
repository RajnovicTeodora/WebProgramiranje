package beans;

public class CustomerType {

	private String typeName;
	private double discount;
	private int requiredPoints;

	public CustomerType() {
		super();
	}

	public CustomerType(String typeName, double discount, int requiredPoints) {
		super();
		this.typeName = typeName;
		this.discount = discount;
		this.requiredPoints = requiredPoints;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getRequiredPoints() {
		return requiredPoints;
	}

	public void setRequiredPoints(int requiredPoints) {
		this.requiredPoints = requiredPoints;
	}

	@Override
	public String toString() {
		return "CustomerType [typeName=" + typeName + ", discount=" + discount + ", requiredPoints=" + requiredPoints
				+ "]";
	}

}
