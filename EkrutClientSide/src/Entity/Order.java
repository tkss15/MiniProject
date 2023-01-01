package Entity;

import java.util.ArrayList;

public class Order 
{
	private String FacilityType, orderType;
	private Facility orderFacility;
	private Double finalPrice;

	public ArrayList<Product> myCart = new ArrayList<>();
	public Order(Facility orderFacility, String orderType, String FacilityType)
	{
		finalPrice = 0.0;
		this.orderType = orderType;
		this.FacilityType = FacilityType;
		this.orderFacility = orderFacility;
	}
	public void addItem(Product product)
	{
		if(myCart.contains(product))
		{
			int ProductIndex = myCart.indexOf(product);
			Product ProductUpdate = myCart.get(ProductIndex);
			ProductUpdate.setAmount(ProductUpdate.getProductAmount() + product.getProductAmount()); 
			myCart.set(ProductIndex, ProductUpdate);
		}
		else
			myCart.add(product);
		finalPrice += PriceItem(product);
	}
	public void UpdateItem(Product product, Integer Amount)
	{
		if(!myCart.contains(product))
			return;
		
		int index = myCart.indexOf(product);
		Product UpdatedProduct = myCart.get(index);
		
		finalPrice -= PriceItem(UpdatedProduct);
		
		UpdatedProduct.setAmount(Amount);
		finalPrice += PriceItem(UpdatedProduct);
		myCart.set(index, UpdatedProduct);
	}
	public void removeItem(Product product)
	{
		if(myCart.contains(product))
		{
			finalPrice -= PriceItem(product);
			myCart.remove(product);
		}
	}
	public Double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	public Double PriceItem(Product product)
	{
		return product.getProductAmount() * product.getProductPrice();
	}
	public String getFacilityType() {
		return FacilityType;
	}
	public void setFacilityType(String facilityType) {
		FacilityType = facilityType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Facility getOrderFacility() {
		return orderFacility;
	}
	public void setOrderFacility(Facility orderFacility) {
		this.orderFacility = orderFacility;
	}
}
