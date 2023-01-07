package common;

public class ProductPriceStartegy implements IProductPrice {
	IProductPrice priceStartegy;
	
	public ProductPriceStartegy(IProductPrice priceStartegy)
	{
		this.priceStartegy = priceStartegy;
	}
	@Override
	public double getPrice(double price, int amount) {
		return priceStartegy.getPrice(price, amount);		
	}

}
