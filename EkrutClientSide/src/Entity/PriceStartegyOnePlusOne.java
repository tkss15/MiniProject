package Entity;

import common.IProductPrice;

public class PriceStartegyOnePlusOne implements IProductPrice 
{

	@Override
	public double getPrice(double price, int amount) 
	{
		/*
		 * In order to pay for only one we need to calculate every second i and add it.
		 * */
		int calculatedAmount = amount/2;
		double finalPrice = calculatedAmount * price;
		
		
		if(amount % 2 == 1)
		{
			finalPrice += price;
		}
		return finalPrice;
	}
	public String toString()
	{
		return"One plus One";
	}
}
