package Server;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.twilio.Twilio;

import common.ChatIF;
import common.ClientConnection;
import common.RequestObjectClient;
import common.ResponseObject;
import common.SMSNotifiction;
import database.DBConnect;
import database.DistributedLock;
import javafx.application.Platform;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	ChatIF serverConsole;
	DBConnect mySqlConnection;
	private ArrayList<String> serverConfing;
	private boolean SendSMSNotifiction = false;
	private Map<String, String> SqlQuerys = new HashMap<>();

	public EchoServer(int port) {
		super(port);
	}

	public EchoServer(int port, ChatIF serverUI) {
		this(port);
		this.serverConsole = serverUI;

	}

	boolean isServerClosed;

	protected void serverStarted() {
		serverConsole.display("Server listening for connections on port " + getPort());
		serverConsole.display("#SetButtonsOn");
		isServerClosed = false;

		/// Import Reports
		// if its the first day of month - import 9 reports to the DB.

		Platform.runLater(() -> {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("DD/MM/YYYY");
			LocalDate localDate = LocalDate.now();
			System.out.println(localDate.format(format));

			int currentDay = localDate.getDayOfMonth();
			Month month = localDate.getMonth();
			System.out.println(month.toString());
			int year = localDate.getYear();

			ArrayList<String> reportTypes = new ArrayList<>(Arrays.asList("Orders", "Supply", "Customer"));
			String reportDate = "";
			ArrayList<String> areas = new ArrayList<>(Arrays.asList("North", "South", "UAE"));

			if (currentDay == 1) {

				if (month.equals(Month.JANUARY)) {
					String monthUpperCase = Month.DECEMBER.toString();
					String correctMonth = monthUpperCase.charAt(0) + monthUpperCase.substring(1).toLowerCase();
					reportDate = year - 1 + "-" + correctMonth;
				} else {
					String monthUpperCase = month.toString();
					String correctMonth = monthUpperCase.charAt(0) + monthUpperCase.substring(1).toLowerCase();
					reportDate = year + "-" + correctMonth;
				}

				for (String reportType : reportTypes) {
					for (String area : areas) {
						// POST SPECIFIC REPORT IN TABLE
						postReport(reportType, reportDate, area);
					}
				}

			}
		});

	}

	private void postReport(String reportType, String reportDate, String area) {
		RequestObjectClient monthlyReports = new RequestObjectClient(
				String.format("%s#%s#%s#", reportType, reportDate, area), "POST", "#UPDATE_MONTHLY_REPORTS");
		monthlyReports.setURL(QueryChanged(monthlyReports));
		mySqlConnection.SafeQuery(monthlyReports);

	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		serverConsole.display("Server has stopped listening for connections.");
		isServerClosed = true;
	}

	@SuppressWarnings("unchecked")
	public void handleMessageFromServerUI(Object message) {
		if (message instanceof ArrayList) {
			serverConfing = (ArrayList<String>) message;

			if (serverConfing.size() == 7) {
				try {
					Twilio.init(serverConfing.get(5), serverConfing.get(6));
					SendSMSNotifiction = true;
					// SMSNotifiction sendExample = new SMSNotifiction("0547443546");
					// sendExample.SendNotification("הדגמה");
				} catch (Exception error) {
					serverConsole.display(error.getMessage());
				}
			}

			InitiliazeQuerys();

			mySqlConnection = new DBConnect(serverConsole, serverConfing);
			mySqlConnection.connectToDB();
		} else if (message instanceof String) {
			String strMessage = (String) message;
			if (strMessage.charAt(0) == '#') {
				try {
					handleServerCommands(strMessage);
				} catch (IOException e) {
					serverConsole.display(e.getMessage());
				}
			}
		}
		// HashMap
		else if (message instanceof HashMap) {
			LinkedHashMap<String, String> txtToTable = (LinkedHashMap<String, String>) message;

			for (String txtFile : txtToTable.keySet()) {
				
				

				RequestObjectClient setGlobal = new RequestObjectClient("", "*", "#SET_GLOBAL");
				setGlobal.setURL(QueryChanged(setGlobal));
//				serverConsole.display(setGlobal.getURL());
				mySqlConnection.SafeQuery(setGlobal);

				RequestObjectClient importSimul = new RequestObjectClient(
						String.format("%s#%s#", txtFile, txtToTable.get(txtFile)), "*", "#SECOND_TRY");
				importSimul.setURL(QueryChanged(importSimul));
//				serverConsole.display(importSimul.getURL());
				mySqlConnection.SafeQuery(importSimul);
				
			}
		}
	}

	private String AutoGenerateRandomCode() {
		StringBuilder RandomCode = new StringBuilder();
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < 5; i++)
			RandomCode.append(rand.nextInt(10));
		return RandomCode.toString();
	}

	private void handleServerCommands(String message) throws IOException {
		switch (message) {
		case "#close":
			close();
			serverConsole.display("#SetButtonsOff");
			break;
		}
	}

	protected void clientDisconnected(ConnectionToClient client) {
		ClientConnection clientToShow = new ClientConnection(client, false);
		serverConsole.display("Client Disconnected " + client.getInetAddress());
		serverConsole.display(clientToShow);
	}

	protected void clientConnected(ConnectionToClient client) {
		// SendPhotosToClient(null,client);
		ClientConnection clientToShow = new ClientConnection(client);
		serverConsole.display("Client Connected " + client.getInetAddress());
		serverConsole.display(clientToShow);
	}

	private void InitiliazeQuerys() {
		SqlQuerys.put("#FIRST_INSTALL", "table=facilities");
		SqlQuerys.put("#USER_LOGOUT", "table=users#condition=userName=@#values=userOnline=\"Offline\"");
		SqlQuerys.put("#UPDATE_AREAMANAGER", "SELECT c.userName, c.telephone, f.FacilityID " + "FROM users as c "
				+ "INNER JOIN employees ON c.userName = employees.userName " + "INNER JOIN ( "
				+ "    SELECT distinct facilities.FacilityID, facilities.FacilityArea " + "    FROM facilities "
				+ "    INNER JOIN productsinfacility on facilities.FacilityID = productsinfacility.FacilityID "
				+ "    INNER JOIN users on users.Area = facilities.FacilityArea "
				+ "    WHERE productsinfacility.ProductAmount < facilities.FacilityThreshholdLevel) f on c.Area = f.FacilityArea "
				+ "WHERE employees.Employeerole = \"AreaManager\" ");

		/* ClientLoginPage */
		SqlQuerys.put("#USER_LOGIN_DATA", "table=users#condition=userName=@&userPassword=@");
		SqlQuerys.put("#USER_UPDATELOGIN", "table=users#condition=userName=@#values=userOnline=\"Online\"");
		SqlQuerys.put("#USER_IS_EMPLOYEE", "table=Employees#condition=userName=@");

		/* EKrutInstallController */
		SqlQuerys.put("#FACILITY_EKUPDATE", "table=facilities#condition=FacilityID=@#values=FacilityEK=\"1\"");

		/* HomepageController */
		SqlQuerys.put("#USER_UPDATE_STATUS", "table=users#condition=userName=@#values=userOnline=\"Offline\"");
		SqlQuerys.put("#CHECK_CLIENT_STATUS", "table=registerclients#condition=userName=@");
		SqlQuerys.put("#SIMPLE_REQUEST",
				"SELECT products.*, productsinfacility.ProductAmount FROM products"
						+ " LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode "
						+ "WHERE productsinfacility.FacilityID =@" + " ORDER BY products.ProductCode");
		SqlQuerys.put("#GET_MONTHLY_FEE",
				"SELECT SUM(prices) " + "from " + "(SELECT orders.finalPrice AS prices "
						+ "FROM ekrutdatabase.delayedpayments "
						+ "LEFT JOIN orders ON delayedpayments.orderCode = orders.orderCode  "
						+ "WHERE SUBSTRING(delayedpayments.orderDate, 4, 2) = @ AND orders.userName=@) orders;");
		SqlQuerys.put("#UPDATE_MONTHLY_FEE", "table=registerclients#condition=userName=@#values=MonthlyFeeCharge=@");
		SqlQuerys.put("#UPDATE_CLIENT_TO_BE_SUBCRICBER",
				"table=registrationformtable#values=" + "firstName=@" + "&lastName=@" + "&telephone=@" + "&Email=@"
						+ "&ID=@" + "&userName=@" + "&userPassword=@" + "&area=@"
						+ "&userTypeRequest=Registered To Subscriber" + "&isSentToManager=0");
		SqlQuerys.put("#UPDATE_CLIENT_STATUS",
				"table=registerclients#condition=userName=@#values=userStatus=\'Registered To Subscriber\'");

		/* Fast Login */
		SqlQuerys.put("#GET_USER_SUBSCRIBER_NUBMER",
				"SELECT registerclients.SubscriberNumber,users.telephone FROM registerclients INNER join users ON registerclients.userName = users.userName where SubscriberNumber=@ and users.userOnline='Offline';");
		SqlQuerys.put("#PUT_USER_AUTHCODE", "table=registerclients#condition=SubscriberNumber=@#values=authcode=@");
		SqlQuerys.put("#GET_USER_AUTHCODE",
				"SELECT users.* FROM users INNER JOIN registerclients ON users.userName = registerclients.userName WHERE registerclients.authcode = @;");

		/* CatalogViewerController */
		SqlQuerys.put("#GET_ALL_SALES_CLIENT_VIEWER",
				"table=sales#values=saleType=saleType&Item=Item#condition=area=@&isActive=1");

		/* OrderDetails */
		SqlQuerys.put("#REMOVE_ITEMS_FACILITY",
				"table=productsinfacility#condition=FacilityID=@&ProductCode=@#values=ProductAmount=@");
		SqlQuerys.put("#CREATE_NEW_ORDER",
				"table=orders#values=finalPrice=@&isInvoiceConfirmed=1&FacilityID=@&userName=@&orderdate=@");
		SqlQuerys.put("#GET_ORDER_NUMBER",
				"SELECT orders.orderCode FROM orders WHERE userName = @ AND orderCode = (SELECT MAX(orderCode) FROM orders);");
		SqlQuerys.put("#CREATE_NEW_VIRTUALORDER", "table=virtualorders#values=orderCode=@&HasDelivery=@&"
				+ "DeliveryLocation=@&DeliveryStatus=SentToProvider&customerApproval=0&estimatedDateAndTime=0&HasPickup=@");
		SqlQuerys.put("#CREATE_NEW_DELYEDPAYMENT", "table=delayedpayments#values=orderCode=@&orderDate=@");
		SqlQuerys.put("#UPDATE_FIRST_PURCHASE", "table=registerclients#condition=userName=@#values=firstPurchase=@");
		SqlQuerys.put("#ADD_ITEMS_TO_ORDER",
				"table=productsinorder#values=orderCode=@&ProductCode=@&FacilityID=@&ProductAmount=@&ProductFinalPrice=@");
		SqlQuerys.put("#UPDATE_PRODUCTS_CLIENT",
				"SELECT productsinfacility.FacilityID,productsinfacility.ProductCode, productsinfacility.ProductAmount "
						+ "FROM products "
						+ "LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode "
						+ "WHERE productsinfacility.FacilityID = @ " + "ORDER BY products.ProductCode");

		/* OrderPickupController */
		SqlQuerys.put("#GET_ORDER_PICKUP",
				"SELECT orders.FacilityID,virtualorders.DeliveryStatus " + "FROM virtualorders "
						+ "inner join orders on orders.orderCode = virtualorders.ordercode "
						+ "WHERE virtualorders.orderCode=@ AND virtualorders.HasPickup = 1;");
		SqlQuerys.put("#UPDATE_ORDER_PICKUP",
				"table=virtualorders#condition=orderCode=@#values=DeliveryStatus=\"Done\"");

		/* CatalogViewerOnly */
		SqlQuerys.put("#GET_ALL_PRODUCTS", "table=products");

		/* MyOrdersController */
		SqlQuerys.put("#GET_MY_ORDERS", "table=orders#condition=userName=@");
		SqlQuerys.put("#GET_MY_DELIVERYS", "table=virtualorders#HasDelivery=1");
		SqlQuerys.put("#SET_APPROVED_BY_CUSTOMER",
				"table=virtualorders#condition=orderCode=@#values=customerApproval=@");
		SqlQuerys.put("#GET_MY_ORDERS", "table=orders#condition=userName=@");
		SqlQuerys.put("#GET_ORDER_DETAILS",
				"SELECT products.productname, products.productcode,productsinorder.productamount, products.productprice, productsInOrder.ProductFinalPrice "
						+ "FROM productsinorder "
						+ "INNER JOIN products ON products.ProductCode = productsinorder.productcode "
						+ "WHERE productsinorder.ordercode = @");
		SqlQuerys.put("#GET_ORDER_DATE", "table=orders#condition=orderCode=@#values=orderdate=orderdate");
		SqlQuerys.put("#GET_ORDER_FINALPRICE", "table=orders#condition=orderCode=@#values=finalprice=finalprice");

		/* OperationWorkerController */
		SqlQuerys.put("#OPERATION_REFILL_ROWS",
				"SELECT facilities.FacilityID, products.ProductCode" + " FROM facilities" + " INNER JOIN products"
						+ " INNER JOIN productsinfacility" + " ON productsinfacility.FacilityID = facilities.FacilityID"
						+ " WHERE FacilityName = @  AND FacilityLocation = @" + " AND ProductName = @"
						+ " GROUP BY facilities.FacilityID, products.ProductCode");

		SqlQuerys.put("#OPERATION_UPDATE_QUANTITY",
				"table=productsinfacility#condition=FacilityID=@&ProductCode=@#values=ProductAmount=@");

		SqlQuerys.put("#OPERATION_DELETE_ORDER", "table=executiveorders#condition=FacilityName=@&ProductName=@");

		SqlQuerys.put("#OPERATION_GET_EXECUTIVE_ORDERS", "table=executiveorders#condition=Area=@");

		/* RefillExecutiveOrderController */
		SqlQuerys.put("#REFILL_CHECK_EXEC_ORDER_ITEM", "table=executiveorders#condition=ProductName=@&"
				+ "ProductQuantity=@&Area=@&FacilityLocation=@&" + "FacilityName=@&FacilityThresholdLevel=@");

		SqlQuerys.put("#REFILL_PUT_TO_EXEC_ORDER", "table=executiveorders#values=ProductName=@&"
				+ "ProductQuantity=@&Area=@&FacilityLocation=@&" + "FacilityName=@&FacilityThresholdLevel=@");

		SqlQuerys.put("#REFILL_GET_THRESHOLD", "table=facilities#condition=FacilityLocation=@&FacilityName=@");

		SqlQuerys.put("#REFILL_GET_PRODUCTS",
				"SELECT products.ProductName, productsinfacility.ProductAmount, facilities.FacilityName, facilities.FacilityThreshholdLevel,facilities.FacilityLocation,facilities.FacilityID "
						+ "FROM  products " + "INNER JOIN facilities " + "INNER JOIN productsinfacility "
						+ "ON productsinfacility.ProductCode = products.ProductCode and productsinfacility.FacilityID = facilities.FacilityID "
						+ "where FacilityArea = @ and facilities.FacilityThreshholdLevel >= productsinfacility.ProductAmount");

		/* ThresholdLevelController */
		SqlQuerys.put("#THRESHOLD_UPDATE_FACILITY",
				"table=facilities#condition=FacilityID=@#values=FacilityThreshholdLevel=@");

		SqlQuerys.put("#THRESHOLD_GET_FACILITY", "table=facilities#condition=FacilityArea=@");

		/* SalesTableController */
		SqlQuerys.put("#GET_EMPLOYEE_TYPE", "table=employees#condition=userName=@#values=Employeerole=Employeerole");
		SqlQuerys.put("#GET_ALL_SALES", "table=sales");
		SqlQuerys.put("#GET_ALL_SALES_IN_AREA", "table=sales#condition=area=@");
		SqlQuerys.put("#UPDATE_ACTIVE_STATUS", "table=sales#values=isActive=@#"
				+ "condition=area=@&saleType=@&startDate=@&startTime=@&endDate=@&endTime=@&Item=@");
		SqlQuerys.put("#DELETE_SALE",
				"table=sales#condition=area=@&saleType=@&startDate=@&startTime=@&endDate=@&endTime=@&Item=@");

		/*
		 * NetworkMarketingManagerController
		 */
		SqlQuerys.put("#CREATE_NEW_SALE",
				"table=sales#values=area=@&saleType=@&startDate=@&startTime=@&endDate=@&endTime=@&isActive=0&Item=@");
		SqlQuerys.put("#GET_ALL_PRODUCTS_IN_AREA",
				"SELECT DISTINCT products.ProductCode,products.ProductName,products.ProductPrice,products.ProductDescription,products.ProductSrc "
						+ "FROM products  "
						+ "INNER JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode "
						+ "INNER JOIN facilities ON facilities.FacilityID = productsinfacility.FacilityID "
						+ "WHERE facilities.FacilityArea = '@'");

		/* ProviderUIController */
		SqlQuerys.put("#USER_AREA_PROV",
				"SELECT users.Area FROM users LEFT JOIN employees ON users.userName = employees.userName WHERE users.userName = '@'");
		SqlQuerys.put("#DELIVERY_PROVIDER_ORDERS_DELIVERY",
				"SELECT virtualorders.orderCode,virtualorders.DeliveryLocation,virtualorders.DeliveryStatus "
						+ "FROM virtualorders " + "INNER JOIN orders ON orders.orderCode = virtualorders.orderCode "
						+ "INNER JOIN facilities ON facilities.FacilityID = orders.FacilityID "
						+ "WHERE FacilityArea = '@' AND DeliveryStatus != 'Done' AND HasDelivery=1 "
						+ "GROUP BY orderCode");

		SqlQuerys.put("#UPDATE_EST_TIME_FOR_USER",
				"SELECT virtualorders.estimatedDateAndTime,orders.userName,virtualorders.orderCode "
						+ "FROM virtualorders " + "INNER JOIN orders ON orders.orderCode = virtualorders.orderCode "
						+ "WHERE virtualorders.orderCode = @");

		SqlQuerys.put("#UPDATE_ACTIVE_STATUS_DISPATCHED",
				"table=virtualorders#condition=orderCode=@&hasDelivery=1#values=DeliveryStatus='Dispatched'&estimatedDateAndTime=@");
		SqlQuerys.put("#UPDATE_ACTIVE_STATUS_SENTTOPROVIDER",
				"table=virtualorders#condition=orderCode=@&hasDelivery=1#values=DeliveryStatus='SentToProvider'");

		/* DeliveryOperatorInterfaceController */
		SqlQuerys.put("#USER_AREA_DELOP",
				"SELECT users.Area FROM users LEFT JOIN employees ON users.userName = employees.userName WHERE users.userName = '@'");
		SqlQuerys.put("#DELIVERY_OPERATOR_ORDERS_DELIVERY",
				"SELECT virtualorders.orderCode,virtualorders.DeliveryStatus,virtualorders.customerApproval "
						+ "FROM virtualorders " + "INNER JOIN orders on orders.orderCode = virtualorders.orderCode "
						+ "INNER JOIN facilities ON facilities.FacilityID = orders.FacilityID "
						+ "WHERE FacilityArea = '@' AND DeliveryStatus != 'Done' AND HasDelivery=1");
		SqlQuerys.put("#DELIVERY_OPERATOR_SET_DELIVERY_STATUS",
				"table=virtualorders#condition=HasDelivery=1&orderCode=@#values=DeliveryStatus='@'");

		/* UserRegistrationController */
		SqlQuerys.put("#UPDATE_REQUEST_TO_MANAGER_URC",
				"table=registrationformtable#condition=ID=@#values=isSentToManager=1&creditCard=@");
		SqlQuerys.put("#UPDATE_USERS_TABLE_URC",
				"table=users#values=firstName=@&lastName=@&telephone=@&Email=@&ID=@&userName=@&"
						+ "userPassword=@&userOnline=Offline&Area=@");
		SqlQuerys.put("#UPDATE_REGISTERED_CLIENTS_TABLE_URC",
				"table=registerclients#values=userName=@&userStatus=PENDING&CardNumber=@&SubscriberNumber=0");
		SqlQuerys.put("#UPDATE_REQUEST_IN_REG_CLIENTS_URC",
				"table=registerclients#condition=userName=@#values=userStatus='@'&CardNumber=@&SubscriberNumber=0");
		SqlQuerys.put("#GET_USERS_URC", "table=registrationformtable#condition=isSentToManager=0");
		SqlQuerys.put("#GET_EXISTING_USERS_URC", "table=users#values=ID=ID");

		/* AreaManagerApproveController */
		SqlQuerys.put("#REJECT_USER_FROM_REG_CLIENTS_AMAC", "table=registerclients#condition=userName=@");
		SqlQuerys.put("#REJECT_USER_FROM_USERS_AMAC", "table=users#condition=ID=@");
		SqlQuerys.put("#REJECT_USER_FROM_REG_FORM_AMAC", "table=registrationformtable#condition=ID=@");
		SqlQuerys.put("#REJECT_APPROVED_USER_AMAC",
				"table=registerclients#condition=userName=@#values=userStatus='APPROVED'");
		SqlQuerys.put("#APPROVE_BASIC_USER_AMC",
				"table=registerclients#condition=userName=@#values=userStatus='APPROVED'&CardNumber=@&SubscriberNumber=0");
		SqlQuerys.put("#GET_MAX_SUB_NUM", "table=registerclients#values=SubscriberNumber=SubscriberNumber");
		SqlQuerys.put("#APPROVE_SUBSCRIBED_USER_AMAC",
				"table=registerclients#condition=userName=@#values=SubscriberNumber=@&userStatus='SUBSCRIBER'&firstPurchase=1");
		SqlQuerys.put("#GET_USERS_AMAC", "table=registrationformtable#condition=area=@&isSentToManager=1");

		/* TypeReportController */
		SqlQuerys.put("#GET_COUNT_TRC",
				"SELECT facilities.FacilityName, COUNT(*) " + "FROM facilities " + "INNER JOIN orders "
						+ "ON orders.facilityID = facilities.FacilityID "
						+ "WHERE FacilityArea = '@'  and SUBSTRING(orderdate, 4, 2) = '@' "
						+ " and SUBSTRING(orderdate, 7, 4) = '@' " + "GROUP BY facilities.FacilityName;");
		SqlQuerys.put("#GET_ALL_FACILITIES_TRC", "table=facilities");
		SqlQuerys.put("#GET_FACILITIES_FROM_AREA_TRC", "table=facilities#condition=FacilityArea=@");
		SqlQuerys.put("#GET_SUPPLY_REPORT_TRC", "SELECT products.ProductName,productsinfacility.ProductAmount "
				+ "FROM products " + "INNER JOIN facilities " + "INNER JOIN productsinfacility "
				+ "ON productsinfacility.ProductCode = products.ProductCode AND productsinfacility.FacilityID = facilities.FacilityID "
				+ "WHERE FacilityArea = '@' AND facilities.FacilityLocation = '@' AND facilities.FacilityName = '@' "
				+ "GROUP BY products.ProductName,productsinfacility.ProductAmount");
		SqlQuerys.put("#GET_SUPPLY_BELOW_THRESHOLD_TRC",
				"SELECT facilities.FacilityName ,COUNT(facilities.FacilityName) AS AmountOfTimeBelowThreshold "
						+ "FROM facilities "
						+ "INNER JOIN productsinfacility ON facilities.FacilityID = productsinfacility.FacilityID AND facilities.FacilityArea ='@' "
						+ "AND productsinfacility.ProductAmount < facilities.FacilityThreshholdLevel "
						+ "INNER JOIN products ON products.ProductCode = productsinfacility.ProductCode "
						+ "GROUP BY facilities.FacilityName " + "ORDER BY facilities.FacilityName");
		SqlQuerys.put("#GET_CUTOMER_HISTOGRAM",
				"SELECT users.userName,COUNT(*) AS 'Number of Purchases' " + "FROM users "
						+ "INNER JOIN orders ON users.userName = orders.userName "
						+ "INNER JOIN facilities ON facilities.FacilityID = orders.FacilityID "
						+ "WHERE facilities.FacilityName = '@' AND SUBSTRING(orders.orderdate,4,2) = '@' "
						+ "GROUP BY users.userName, facilities.FacilityName");

		/* monthlyReportsController */
		SqlQuerys.put("#GET_REPORTS_MRC", "table=reports#condition=Area=@&reportType=@&reportDate=@");

		/* server Commands */
		SqlQuerys.put("#UPDATE_MONTHLY_REPORTS", "table=reports#values=reportType=@&reportDate=@&Area=@");

		/* Server Import Simulator */
		SqlQuerys.put("#SECOND_TRY", "LOAD DATA LOCAL INFILE '@' INTO TABLE @ " + "FIELDS TERMINATED BY ',' "
				+ "ENCLOSED BY '\"' " + "LINES TERMINATED BY '\r\n'" + "IGNORE 1 LINES");
		SqlQuerys.put("#IMPORT_SIMUL", "load data local infile " + "\"" + "@" + "\"" + " into table @");
		SqlQuerys.put("#SET_GLOBAL", "SET GLOBAL local_infile=1");

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof RequestObjectClient) {
			RequestObjectClient clientRequest = (RequestObjectClient) msg;
			try {
				/*
				 * Explantion:
				 * 
				 * - Client Request ends with #SEND_ALL it will update all users. - Client
				 * Request ends with #SEND_NOT_ME it will update all users BUT the sender. ( it
				 * will send empty response ). - Other.
				 */

				Integer QueryCase = 0;
				if (clientRequest.getRequestID().endsWith("#SEND_NOT_ME")) {
					QueryCase = 1;
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_NOT_ME", ""));
				}
				if (clientRequest.getRequestID().endsWith("#SEND_ALL")) {
					QueryCase = 2;
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_ALL", ""));
				}

				String Key = SqlQuerys.get(clientRequest.getRequestID());
				String[] dataInjector = (clientRequest.getURL()).split("#");
				StringBuilder FinalQuery = new StringBuilder();
				ResponseObject ResponseEmpty = new ResponseObject("Empty");

				int currentData = 0;
				for (char currentChar : Key.toCharArray()) {
					if (currentChar == '@') {
						FinalQuery.append(dataInjector[currentData++]);
					} else {
						FinalQuery.append(currentChar);
					}
				}

				clientRequest.setURL(FinalQuery.toString());

				/*
				 * Special Cases when we need to notify other users live
				 */
				if (clientRequest.getRequestID().equals("#USER_LOGIN_DATA")) {
					DistributedLock lock = new DistributedLock(mySqlConnection.getConn(), "LogginLock");
					lock.setOwner(dataInjector[0]);

					if (!lock.isLocked()) {
						lock.acquire();
						client.sendToClient(mySqlConnection.SafeQuery(clientRequest));
					} else {
						ResponseEmpty.setRequest("Empty");
						client.sendToClient(ResponseEmpty);
					}
				}
				if (clientRequest.getRequestID().equals("#USER_LOGOUT")) {
					clientDisconnected(client);
					mySqlConnection.makeQuery(clientRequest);
					client.close();
				}
				if (clientRequest.getRequestID().equals("#GET_USER_SUBSCRIBER_NUBMER")) {
					// Here we need to check the data live on server.
					ResponseObject ServerResponse = mySqlConnection.SafeQuery(clientRequest);
					ResponseObject ClientResponse = new ResponseObject("");

					ClientResponse.setRequest("#GET_USER_SUBSCRIBER_NUBMER");

					if (ServerResponse.Responsedata.size() == 0) {
						ClientResponse.Responsedata.add(false);
						client.sendToClient(ClientResponse);
						return;
					} else {
						ClientResponse.Responsedata.add(true);
						client.sendToClient(ClientResponse);

						Object[] values = (Object[]) ServerResponse.Responsedata.get(0);
						Integer SubscriberNumber = (Integer) values[0];
						String Telephone = (String) values[1];

						String AuthCode = AutoGenerateRandomCode();
						String SMS = "Your Auth code is " + AuthCode;

						if (SendSMSNotifiction) {
							SMSNotifiction sendExample = new SMSNotifiction(Telephone);
							sendExample.SendNotification(SMS);
						} else {
							serverConsole.display(SMS);
						}
						RequestObjectClient UpdateAuth = new RequestObjectClient(
								String.format("%d#%s#", SubscriberNumber, AuthCode), "PUT", "#PUT_USER_AUTHCODE");
						UpdateAuth.setURL(QueryChanged(UpdateAuth));
						mySqlConnection.SafeQuery(UpdateAuth);
						return;

					}
				}

				switch (QueryCase) {
				case 0: {
//						System.out.println("Opreation " + clientRequest.getRequestID());
					client.sendToClient(mySqlConnection.SafeQuery(clientRequest));
					break;
				}
				case 1: {
					ResponseEmpty.setRequest("Empty");

					sendToAllClients(mySqlConnection.SafeQuery(clientRequest), client);
					client.sendToClient(ResponseEmpty);
					break;
				}
				case 2: {
					sendToAllClients(mySqlConnection.SafeQuery(clientRequest));
					break;
				}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String QueryChanged(RequestObjectClient clientRequest) {
		String Key = SqlQuerys.get(clientRequest.getRequestID());
		String[] dataInjector = (clientRequest.getURL()).split("#");
		StringBuilder FinalQuery = new StringBuilder();

		System.out.println(Key + " " + dataInjector);

		int currentData = 0;
		for (char currentChar : Key.toCharArray()) {
			if (currentChar == '@') {
				FinalQuery.append(dataInjector[currentData++]);
			} else {
				FinalQuery.append(currentChar);
			}
		}

		return FinalQuery.toString();
	}
}
