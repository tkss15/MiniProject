package Entity;

import common.IProductPrice;

public class PriceStartegySecondHalfPrice implements IProductPrice 
{

	@Override
	public double getPrice(double price, int amount) 
	{
		/*
		 * In order to pay for only one we need to calculate every second i and add it.
		 * */
		double finalPrice = 0.0;
		double halfPriced = price/2;
		
		for(int i = 0; i < amount; i++)
		{
			finalPrice += (i % 2 == 0) ? price : halfPriced;
		}
		
		return finalPrice;
	}
	public String toString()
	{
		return"Second half Price";
	}
}
