package Entity;

public class OrderRowDetails {
	private String ProductName;
	private Integer ProductCode;
	private Integer ProductAmount;
	private double ProductPrice;
	private double FinalPrice;
	
	public OrderRowDetails(String ProductName,Integer ProductCode, Integer ProductAmount,  double ProductPrice, double FinalPrice) 
	{
		this.ProductName = ProductName;
		this.ProductCode=ProductCode;
		this.ProductAmount = ProductAmount;
		this.ProductPrice = ProductPrice;
		this.FinalPrice = FinalPrice;
		
	}
	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public Integer getProductCode() {
		return ProductCode;
	}

	public void setProductCode(Integer productCode) {
		ProductCode = productCode;
	}

	public Integer getProductAmount() {
		return ProductAmount;
	}

	public void setProductAmount(Integer productAmount) {
		ProductAmount = productAmount;
	}

	public double getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(double productPrice) {
		ProductPrice = productPrice;
	}

	public double getFinalPrice() {
		return FinalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		FinalPrice = finalPrice;
	}


}
