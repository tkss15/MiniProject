package common;

/***
 *
 * IController is an interface that all controllers across our application will implement.
 * this interface has 1 method which is updatedata. in the method the Controller will get a data Object and will be able to identify it
 * updatedata transfers the data comming from ClientConsole to the Controllers.
 * 
 * How it looks:
 * Server -(Sends data)-> ChatClient receives it -(display)-> ClientConsole receives it -(updatedata)-> Controller of Current represented UI receives it  
 */
public interface IController 
{
	/**
	 * 
	 * @param data - an object received from the DB.
	 */
	void updatedata(Object data);
}
