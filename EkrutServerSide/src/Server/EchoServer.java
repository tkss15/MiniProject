package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.twilio.Twilio;

import common.ChatIF;
import common.ClientConnection;
import common.RequestObjectClient;
import common.ResponseObject;
import database.DBConnect;
import database.DistributedLock;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer 
{
	ChatIF serverUI;
	DBConnect mySqlConnection;
	private ArrayList<String> serverConfing;
    private boolean SendSMSNotifiction = false;
	private Map<String,String> SqlQuerys = new HashMap<>();
	public EchoServer(int port) 
	{
		super(port);
	}

	public EchoServer(int port, ChatIF serverUI) 
	{
		this(port);
		this.serverUI = serverUI;
		
	}

	boolean isServerClosed;
	protected void serverStarted() 
	{
		serverUI.display("Server listening for connections on port " + getPort());
		serverUI.display("#SetButtonsOn");
		isServerClosed = false;
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() 
	{
		serverUI.display("Server has stopped listening for connections.");
		isServerClosed = true;
	}
	
	@SuppressWarnings("unchecked")
	public void handleMessageFromServerUI(Object message) {
		if(message instanceof ArrayList)
		{
			serverConfing = (ArrayList<String>)message;
			
			if(serverConfing.size() == 7)
			{
				try 
				{
					Twilio.init(serverConfing.get(5), serverConfing.get(6));
					SendSMSNotifiction = true;
					//SMSNotifiction sendExample = new SMSNotifiction("0547443546");
					//sendExample.SendNotification("הדגמה");
				}
				catch(Exception error)
				{
					serverUI.display(error.getMessage());
				}
			}
			
			InitiliazeQuerys();
			
			mySqlConnection = new DBConnect(serverUI, serverConfing);
			mySqlConnection.connectToDB();
		}
		else if (message instanceof String) 
		{
			String strMessage = (String) message;
			if (strMessage.charAt(0) == '#') 
			{
				try 
				{
					handleServerCommands(strMessage);
				} catch (IOException e) 
				{
					serverUI.display(e.getMessage());
				}
			}
		}
	}

	private void handleServerCommands(String message) throws IOException 
	{
		switch (message) 
		{
			case "#close":
				close();
				serverUI.display("#SetButtonsOff");
			break;
		}
	}
	protected void clientDisconnected(ConnectionToClient client) 
	{
		ClientConnection clientToShow = new ClientConnection(client,false);
		serverUI.display("Client Disconnected "+ client.getInetAddress());
		serverUI.display(clientToShow);
	}
	protected void clientConnected(ConnectionToClient client) 
	{
		//SendPhotosToClient(null,client);
		ClientConnection clientToShow = new ClientConnection(client);
		serverUI.display("Client Connected "+ client.getInetAddress());
		serverUI.display(clientToShow);
	}

	
	private void InitiliazeQuerys()
	{
		SqlQuerys.put("#FIRST_INSTALL", "table=facilities");
		SqlQuerys.put("#USER_LOGOUT", "table=users#condition=userName=@#values=userOnline=\"Offline\"");
		SqlQuerys.put("#UPDATE_AREAMANAGER", "SELECT c.userName, c.telephone, f.FacilityID "
				+ "FROM users as c "
				+ "INNER JOIN employees ON c.userName = employees.userName "
				+ "INNER JOIN ( "
				+ "    SELECT distinct facilities.FacilityID, facilities.FacilityArea "
				+ "    FROM facilities "
				+ "    INNER JOIN productsinfacility on facilities.FacilityID = productsinfacility.FacilityID "
				+ "    INNER JOIN users on users.Area = facilities.FacilityArea "
				+ "    WHERE productsinfacility.ProductAmount < facilities.FacilityThreshholdLevel) f on c.Area = f.FacilityArea "
				+ "WHERE employees.Employeerole = \"AreaManager\" ");
		
		/*ClientLoginPage*/
		SqlQuerys.put("#USER_LOGIN_DATA", "table=users#condition=userName=@&userPassword=@");
		SqlQuerys.put("#USER_UPDATELOGIN", "table=users#condition=userName=@#values=userOnline=\"Online\"");
		SqlQuerys.put("#USER_IS_EMPLOYEE", "table=Employees#condition=userName=@");

		/*EKrutInstallController*/
		SqlQuerys.put("#FACILITY_EKUPDATE", "table=facilities#condition=FacilityID=@#values=FacilityEK=\"1\"");
		
		/*HomepageController*/
		SqlQuerys.put("#USER_UPDATE_STATUS", "table=users#condition=userName=@#values=userOnline=\"Offline\"");
		SqlQuerys.put("#CHECK_CLIENT_STATUS", "table=registerclients#condition=userName=@");
		SqlQuerys.put("#SIMPLE_REQUEST", 
				"SELECT products.*, productsinfacility.ProductAmount FROM products"
				+ " LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode "
				+ "WHERE productsinfacility.FacilityID =@"
				+ " ORDER BY products.ProductCode");
		SqlQuerys.put("#GET_MONTHLY_FEE", "SELECT SUM(prices) "
				+ "from "
				+ "(SELECT orders.finalPrice AS prices "
				+ "FROM ekrutdatabase.delayedpayments "
				+ "LEFT JOIN orders ON delayedpayments.orderCode = orders.orderCode  "
				+ "WHERE SUBSTRING(delayedpayments.orderDate, 4, 2) = @ AND orders.userName=@) orders;");
		SqlQuerys.put("#UPDATE_MONTHLY_FEE","table=registerclients#condition=userName=@#values=MonthlyFeeCharge=@");
		
		/*CatalogViewerController*/
		SqlQuerys.put("#GET_ALL_SALES", "table=sales#values=saleType=saleType&Item=Item#condition=area=@&isActive=1");
		
		/*OrderDetails*/
		SqlQuerys.put("#REMOVE_ITEMS_FACILITY", "table=productsinfacility#condition=FacilityID=@&ProductCode=@#values=ProductAmount=@");
		SqlQuerys.put("#CREATE_NEW_ORDER", "table=orders#values=finalPrice=@&isInvoiceConfirmed=1&FacilityID=@&userName=@&orderdate=@");
		SqlQuerys.put("#GET_ORDER_NUMBER", "SELECT orders.orderCode FROM orders WHERE userName = @ AND orderCode = (SELECT MAX(orderCode) FROM orders);");
		SqlQuerys.put("#CREATE_NEW_VIRTUALORDER", "table=virtualorders#values=orderCode=@&HasDelivery=@&"
				+ "DeliveryLocation=@&DeliveryStatus=SentToProvider&customerApproval=0&estimatedDateAndTime=@&HasPickup=@");
		SqlQuerys.put("#CREATE_NEW_DELYEDPAYMENT", "table=delayedpayments#values=orderCode=@&orderDate=@");
		SqlQuerys.put("#UPDATE_FIRST_PURCHASE", "table=registerclients#condition=userName=@#values=firstPurchase=@");
		SqlQuerys.put("#ADD_ITEMS_TO_ORDER", "table=productsinorder#values=orderCode=@&ProductCode=@&FacilityID=@&ProductAmount=@&ProductFinalPrice=@");
		SqlQuerys.put("#UPDATE_PRODUCTS_CLIENT", "SELECT productsinfacility.FacilityID,productsinfacility.ProductCode, productsinfacility.ProductAmount "
				+ "FROM products "
				+ "LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode "
				+ "WHERE productsinfacility.FacilityID = @ "
				+ "ORDER BY products.ProductCode");
		
		/*OrderPickupController*/
		SqlQuerys.put("#GET_ORDER_PICKUP", "SELECT orders.FacilityID,virtualorders.DeliveryStatus "
				+ "FROM virtualorders "
				+ "inner join orders on orders.orderCode = virtualorders.ordercode "
				+ "WHERE virtualorders.orderCode=@ AND virtualorders.HasPickup = 1;");
		SqlQuerys.put("#UPDATE_ORDER_PICKUP", "table=virtualorders#condition=orderCode=@#values=DeliveryStatus=\"Done\"");
		
		/*CatalogViewerOnly*/
		SqlQuerys.put("#GET_ALL_PRODUCTS", "table=products");

		/*MyOrdersController */ 
		SqlQuerys.put("#GET_MY_ORDERS", "table=orders#condition=userName=@");
		SqlQuerys.put("#GET_MY_DELIVERYS", "table=virtualorders#HasDelivery=1");
		SqlQuerys.put("#SET_APPROVED_BY_CUSTOMER", "table=virtualorders#condition=orderCode=@#values=customerApproval=@");
		SqlQuerys.put("#GET_MY_ORDERS", "table=orders#condition=userName=@");
		SqlQuerys.put("#GET_ORDER_DETAILS", "SELECT products.productname, products.productcode,productsinorder.productamount, products.productprice, productsInOrder.ProductFinalPrice "
				+ "FROM productsinorder " + "INNER JOIN products ON products.ProductCode = productsinorder.productcode "
				+ "WHERE productsinorder.ordercode = @");
		SqlQuerys.put("#GET_ORDER_DATE", "table=orders#condition=orderCode=@#values=orderdate=orderdate");
		SqlQuerys.put("#GET_ORDER_FINALPRICE", "table=orders#condition=orderCode=@#values=finalprice=finalprice");



	}
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) 
	{
		if(msg instanceof RequestObjectClient)
		{
			RequestObjectClient clientRequest = (RequestObjectClient) msg;
			try 
			{
				/*
				 * Explantion:
				 * 
				 * - Client Request ends with SEND_ALL it will update all users.
				 * - Client Request ends with SEND_NOT_ME it will update all users BUT the sender. ( it will send empty response ).
				 * - Other.
				 * */
				
				
				Integer QueryCase = 0;
				if(clientRequest.getRequestID().endsWith("#SEND_NOT_ME"))
				{
					QueryCase = 1;
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_NOT_ME", ""));
				}
				if(clientRequest.getRequestID().endsWith("#SEND_ALL"))
				{
					QueryCase = 2;
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_ALL", ""));
				}
				
				String Key = SqlQuerys.get(clientRequest.getRequestID());
				String[] dataInjector = (clientRequest.getURL()).split("#");
				StringBuilder FinalQuery = new StringBuilder();
				ResponseObject ResponseEmpty = new ResponseObject("Empty");
				
				int currentData = 0;
				for(char currentChar : Key.toCharArray())
				{
					if(currentChar == '@')
					{
						FinalQuery.append(dataInjector[currentData++]);
					}
					else
					{
						FinalQuery.append(currentChar);
					}
				}
				
				clientRequest.setURL(FinalQuery.toString());
				
				/*
				 * Special Cases when we need to notify other users live
				 * */
				if(clientRequest.getRequestID().equals("#USER_LOGIN_DATA"))
				{
					DistributedLock lock = new DistributedLock(mySqlConnection.getConn(), "LogginLock");
					lock.setOwner(dataInjector[0]);
					
					if(!lock.isLocked())
					{		
						lock.acquire();
						client.sendToClient(mySqlConnection.SafeQuery(clientRequest));
					}
					else
					{
						ResponseEmpty.setRequest("Empty");
						client.sendToClient(ResponseEmpty);	
					}
				}
				if(clientRequest.getRequestID().equals("#USER_LOGOUT"))
				{	
					clientDisconnected(client);
					mySqlConnection.makeQuery(clientRequest);
					client.close();
				}
				
				switch(QueryCase)
				{
					case 0:
					{
						System.out.println("Opreation " + clientRequest.getRequestID());
						client.sendToClient(mySqlConnection.SafeQuery(clientRequest));							
						break;
					}
					case 1:
					{
						ResponseEmpty.setRequest("Empty");
						
						sendToAllClients(mySqlConnection.SafeQuery(clientRequest), client);
						client.sendToClient(ResponseEmpty);					
						break;
					}
					case 2:
					{
						sendToAllClients(mySqlConnection.SafeQuery(clientRequest));						
						break;
					}
				}
				
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
