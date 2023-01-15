package Entity;

import java.util.ArrayList;
/**

The Order class represents an order object and contains information about the order such as the type of facility, type of order, facility associated with the order, final price, and a list of products (myCart) that make up the order.

@field FacilityType - a string representing the type of facility associated with the order
@field orderType - a string representing the type of order.
@field orderFacility - a Facility object representing the facility associated with the order
@field finalPrice - a Double representing the final price of the order
@field myCart - an ArrayList of Product objects representing the products included in the order

@constructor Order(Facility orderFacility, String orderType, String FacilityType) - Constructor for creating a new Order object. 
It takes in information about the facility, order type, and facility type associated with the order.

@method addItem(Product product) - adds a product to the order's myCart and updates the final price
@method UpdateItem(Product product, Integer Amount) - updates the amount of a product in the order's myCart and updates the final price
@method removeItem(Product product) - removes a product from the order's myCart and updates the final price
@method calcuatePrice() - calculates the final price of the order based on the products in the myCart
@method getFinalPrice() - returns the final price of the order
@method PriceItemNoDiscount(Product product) - returns the price of a product without any discounts
@method setFinalPrice(Double finalPrice) - sets the final price of the order
@method PriceItem(Product product) - returns the price of a product after applying any discounts
@method getFacilityType() - returns the facility type associated with the order as a string
@method setFacilityType(String facilityType) - sets the facility type associated with the order
@method getOrderType() - returns the order type as a string
@method setOrderType(String orderType) - sets the order type
@method getOrderFacility() - returns the facility associated with the order as a Facility object
@method setOrderFacility(Facility orderFacility) - sets the facility associated with the order
*/
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
	
	/**
	 * 
	 * @param product
	 * 
	 */
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
		{
			myCart.add(product);
		}
		calcuatePrice();
	}
	/**
	 * 
	 * @param product - the product to update its amount in the cart
	 * @param Amount - the new amount of the product. 
	 */
	public void UpdateItem(Product product, Integer Amount)
	{
		if(!myCart.contains(product))
			return;
		
		int index = myCart.indexOf(product);
		Product UpdatedProduct = myCart.get(index);
		UpdatedProduct.setAmount(Amount);
		calcuatePrice();
		myCart.set(index, UpdatedProduct);
	}
	/**
	 * 
	 * @param product - product to be removed from the cart.
	 */
	public void removeItem(Product product)
	{
		if(myCart.contains(product))
		{
			myCart.remove(product);
		}
		calcuatePrice();
	}
	/**
	 * calculate the total price of the cart.
	 */
	private void calcuatePrice()
	{
		finalPrice = 0.0;
		for(Product temp : myCart)
		{
			finalPrice += PriceItem(temp);
		}
	}
	
	/**
	 * 
	 * @param product
	 * @return price of the product with current product amount (without discount).
	 */
	public Double PriceItemNoDiscount(Product product) {

		return product.getProductPrice() * product.getProductAmount();
	}
	
	//Getters and Setters for the Order class.
	
	public Double getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	public Double PriceItem(Product product)
	{
		return product.getPriceStategy().getPrice(product.getProductPrice(),product.getProductAmount());
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
