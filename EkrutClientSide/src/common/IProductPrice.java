package common;

/* @author eldad
*
* IProductPrice is an interface that implements the strategy design pattern.
* Each product can have a different kind of sale, and the strategy pattern allows for
* the encapsulation of these different sale behaviors in separate classes.
* The interface defines a method, getPrice, which takes in the original price of a product
* and the amount of the product being purchased, and returns the final price after the sale has been applied.
* It also defines toString, which is used to print the sale name.
*/
public interface IProductPrice 
{
	double getPrice(double price, int amount);
	String toString();
}
