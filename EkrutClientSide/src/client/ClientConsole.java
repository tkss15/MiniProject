package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Entity.Employee;
import Entity.Facility;
import Entity.Order;
import Entity.Product;
import Entity.RegisterClient;
import Entity.User;
import common.ChatIF;
import common.CountdownOrder;
import common.IController;
import common.RequestObjectClient;
import common.SceneManager;
import javafx.concurrent.Task;

/***
 * 
 * ClientConsole is the second layer which help operating the client saved data or sending data to the server. 
 */
public class ClientConsole implements ChatIF 
{
	public static int DEFAULT_PORT = 5555;// the Port of the server
	public final String ApplicationConfig = "EkrutApplication/";// All files of the Application will be saved in a folder named 'EkrutApplication'

	ChatClient client;// the third layer of protection we create it here so we can use it.
	IController currentController;// IController is an interface all UI controllers have which help to detect which controller is being used right now.
	
	/**
	 * Data:
	 * List below is a data being used in different controllers. in order to restore data 
	 * when changing between different controllers. All data is private and can be change only with getters and setters.
	 * */
	private User clientUser = new User(null, null);
	private Order clientOrder = new Order(null,null,null);
	private Facility EKFacility = new Facility(null, null, null, null, null, null);
	private CountdownOrder taskCountdown = new CountdownOrder();
	private Integer managerOrderdeatils;
	private String ApplicationType = null;
	
	private HashMap<String,String> mapMonths = new HashMap<>();
	
	private ArrayList<String> monthlyReportParameters = new ArrayList<>(3);
	private ArrayList<Product> arrProducts = new ArrayList<>();
	private ArrayList<Facility> arrFacility = new ArrayList<>();
	private boolean isCeo = false;
	
	/***
	 * ClientConsole is the Second layer of client. client console works with the ClientInterfaceController and with ChatClient.
	 * @param host - saves the ip-address string adress that allows client to connect to sever.
	 * @param port - saves the port integer that allows client to connect to server.
	 * @param clientInterface
	 */
	public ClientConsole(String host, int port) 
	{
		try 
		{
			client = new ChatClient(host, port, this);
		} 
		catch (IOException exception) 
		{
			System.out.println("Error: Can't setup connection!Terminating client.");
			System.exit(1);
		}
	}
	/***
	 * 
	 * @param forceExit - a boolean value that will exit all client windows if its true.
	 * UserDisconnected is a function that will send the server that the current user want to disconnect his account.
	 */
	public void UserDisconnected(boolean forceExit)
	{
		if(this.clientUser.getOnlineStatus() != null && 
		   this.clientUser.getOnlineStatus().equals("Online"))
		{
	    	RequestObjectClient request = new RequestObjectClient(
	    			"#USER_LOGOUT",
	    			String.format("%s#", this.clientUser.getUserName()),
	    			"PUT");    
	    	accept(request);
	    	this.clientUser.setOnlineStatus("Offline");
		}
		if(forceExit)
		{
	    	RequestObjectClient request = new RequestObjectClient("#USER_QUIT","","*");    	
	    	ClientUI.clientController.accept(request);	
			System.exit(0);
		}
	}
	/***
	 * Setter for currentController
	 * @param currentController - is an @arg that holds a new controller which we need to update.
	 */
	public void setController(IController currentController) {
		this.currentController = currentController;
	}
	/***
	 * @param msg
	 * Function operates as second layer. the function sends object(msg) to the @ChatClient
	 * when client wants to send a message to the server. the message advanced to the next layer until it sends to the server.
	 * 										  We are here.
	 * Client UI -> Client UI Controller -> Client Console -> ChatClient -> EchoServer
	 */
	public void accept(Object msg) 
	{
		client.handleMessageFromClientUI(msg);

	}
	/***
	 * @param message
	 * display will transfer the data received from @ChatClient down to the Current used Controller. 
	 */
	@Override
	public void display(Object message) 
	{
		currentController.updatedata(message);
	}
	/***
	* Getters and Setters to our data
	 */
	public void setHashMapMonths(HashMap<String,String> map) {
		mapMonths = map;
	}
	public HashMap<String,String> getHashMapMonths() {
		return mapMonths;
	}
	public CountdownOrder getTaskCountdown() {
		return taskCountdown;
	}
	public void setTaskCountdown(CountdownOrder taskCountdown) {
		this.taskCountdown = taskCountdown;
	}
	public Facility getEKFacility() {
		return EKFacility;
	}
	public void setEKFacility(Facility eKFacility) {
		EKFacility = eKFacility;
	}
	public String getApplicationType() {
		return ApplicationType;
	}
	public void setApplicationType(String applicationType) {
		ApplicationType = applicationType;
	}
	public Order getClientOrder() {
		return clientOrder;
	}
	public void setClientOrder(Order clientOrder) {
		this.clientOrder = clientOrder;
	}
	public User getUser()
	{
		return clientUser;
	}
	public Integer getManagerOrderdeatils() {
		return managerOrderdeatils;
	}
	public void setManagerOrderdeatils(Integer managerOrderdeatils) {
		this.managerOrderdeatils = managerOrderdeatils;
	}
	public void setUser(User user)
	{
		if(user instanceof Employee) {// if user as an employee he has a new instance which is Employee ( subclass of user)
			Employee employee = (Employee)user;
			clientUser = new Employee(employee);
		}
		else clientUser = user; 
	}
	public ArrayList<Facility> getArrFacility() {
		return arrFacility;
	}
	public void setArrFacility(ArrayList<Facility> arrFacility) {
		this.arrFacility = arrFacility;
	}
	public ArrayList<Product> getArrProducts() {
		return arrProducts;
	}
	public void setArrProducts(ArrayList<Product> arrProducts) {
		this.arrProducts = arrProducts;
	}
	public void setReportYear(String year) 
	{
		monthlyReportParameters.add(0, year);
	}
	public void setReportMonth(String month) 
	{
		monthlyReportParameters.add(1, month);
	}
	public void setReportType(String type) 
	{
		monthlyReportParameters.add(2, type);
	}
	public String getReportYear() {
		return monthlyReportParameters.get(0);
	}
	
	public String getReportMonth() {
		return monthlyReportParameters.get(1);
	}
	
	public String getReportType() {
		return monthlyReportParameters.get(2);
	}
	public boolean isCeo() 
	{
		return isCeo;
	}
	public void setCeo(boolean isCeo) 
	{
		this.isCeo = isCeo;
	}

}
