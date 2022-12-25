package Entity;

import java.util.ArrayList;

public class Order 
{
	protected ArrayList<Product> myCart = new ArrayList<>();
	public Order()
	{
		
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
	}
	public void UpdateItem(Product product, Integer Amount)
	{
		if(!myCart.contains(product))
			return;
		
		int index = myCart.indexOf(product);
		Product UpdatedProduct = myCart.get(index);
		
		UpdatedProduct.setAmount(Amount);
		myCart.set(index, UpdatedProduct);
	}
	public void removeItem(Product product)
	{
		if(myCart.contains(product))
			myCart.remove(product);
	}
}
