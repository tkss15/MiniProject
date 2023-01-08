package Entity;

import common.IProductPrice;

public class PriceStartegyRegular implements IProductPrice 
{
	@Override
	public double getPrice(double price, int amount) 
	{
		System.out.println("This is Regular Price");
		return price*amount;
	}
}
