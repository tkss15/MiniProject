package Entity;

import common.IProductPrice;

public class PriceStartegyCustom implements IProductPrice 
{
	double percent;
	
	public PriceStartegyCustom(double percent)
	{
		this.percent = percent;
	}
	

	@Override
	public double getPrice(double price, int amount) 
	{
		System.out.println("This is Custom Price");
		/*
		 * In order to pay for only one we need to calculate every second i and add it.
		 * */
		double finalPrice = 0.0;
		
		for(int i = 0; i < amount; i++)
		{
			finalPrice += price*(1-percent);
		}
		
		return finalPrice;
	}
	
	public String toString()
	{
		return percent + "% Off price";
	}
	
}
