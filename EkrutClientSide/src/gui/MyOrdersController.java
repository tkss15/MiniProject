package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entity.OrderRow;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MyOrdersController implements Initializable, IController {

	private ArrayList<OrderRow> orderRows;
	private ArrayList<OrderTable> OrdersData;

	// Eldad Changed
	private ObservableList<OrderRow> deliveryInfo;
	private HashMap<Integer,Integer> deliveryOrderCodes = new HashMap<>();
	@FXML
	private Button BackButton;

	@FXML
	private Button CloseButton;

	@FXML
	private HBox MyOrdersTitle;

	@FXML
	private TableView<OrderRow> OrdesTable;

	@FXML
	private TableColumn<OrderRow, Integer> OrderCode;

	@FXML
	private TableColumn<OrderRow, Button> OrderDetails;

	@FXML
	private TableColumn<OrderRow, Integer> FacilityID;

	@FXML
	private TableColumn<OrderRow, Integer> FinalPrice;

	@FXML
	private TableColumn<OrderRow, CheckBox> markAsReceivedColumn;

	@FXML
	private ImageView ShoppingImage;

	@FXML
	private ImageView logoImage;

    @FXML
    private TextField textFieldOrderCode;

	@FXML
	void closeWindow(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);

	}

	@FXML
	void GoBack(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);
	}

	/**
	 * Initializes the controller class. This method is called automatically when
	 * the FXML file is loaded.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */

	public void initialize(URL location, ResourceBundle resources) 
	{
		// Set this controller as the controller for the ClientUI
		ClientUI.clientController.setController(this);
		// Initialize the OrdersData(An array containing all the rows of the table with
		// the information imported from the query) and orderRows(Contains all rows of
		// the table) lists
		
		OrdersData = new ArrayList<>();
		orderRows = new ArrayList<>();

		// Make a query to the database to show the orders for the current user.
		RequestObjectClient request = new RequestObjectClient("#GET_MY_ORDERS",
				String.format("%s#", ClientUI.clientController.getUser().getUserName()),
				"GET");
		ClientUI.clientController.accept(request);

		RequestObjectClient requestDelivery = new RequestObjectClient("#GET_MY_DELIVERYS",
				String.format("%s#",
						ClientUI.clientController.getUser().getUserName()),
				"*");
		ClientUI.clientController.accept(requestDelivery);

		// Initialise the cells in the order table(columns)
		OrderCode.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("orderCode"));
		FinalPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("finalPrice"));
		FacilityID.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("facilityID"));
		markAsReceivedColumn.setCellValueFactory(new PropertyValueFactory<OrderRow, CheckBox>("orderReceptionAcception"));
		OrderDetails.setCellValueFactory(new PropertyValueFactory<OrderRow, Button>("orderDetails"));

		// Create OrderRow objects for each OrderTable object in the OrdersData list and
		// add them to the orderRows list.
		for (int i = 0; i < OrdersData.size(); i++) 
		{
			OrderTable temp = OrdersData.get(i);
			temp.createButton();
			
			OrderRow orderRow = new OrderRow(temp.orderCode, temp.finalPrice, temp.facilityID,
					temp.OrderDetails);
			
			if (deliveryOrderCodes.containsKey(temp.getOrderCode())) 
			{
				CheckBox checkBox = new CheckBox();
				
				if(deliveryOrderCodes.get(temp.getOrderCode())==1) {
					checkBox.setSelected(true);					
				}
				else checkBox.setSelected(false);
				
				if(checkBox.isSelected()) 
				{
					checkBox.setDisable(true);
				}
				
				orderRow.setOrderReceptionAcception(checkBox);
				checkBox.setOnAction((event) -> {
					
					int flag = checkBox.isSelected() ? 1 : 0;
					//request query to set the customer approval status as the value of the flag.
					//i.e if the check box is selected then set the status to 1. else set status to 0.
					RequestObjectClient setReceived = new RequestObjectClient("#SET_APPROVED_BY_CUSTOMER",
							String.format("%d#%d#",
							temp.getOrderCode(),flag),"PUT");
					ClientUI.clientController.accept(setReceived);
					
					//show alert of information for the user after completion of the action.
					Alert info = new Alert(AlertType.INFORMATION);
					
					info.setContentText("Thank you for your order! See you next time!");
					info.showAndWait();
					checkBox.setDisable(true);
					
				});
			}
			orderRows.add(orderRow); 

		}

		// Create an observable list from the orderRows list and set it as the items for
		// the OrdesTable.
		deliveryInfo = FXCollections.observableArrayList(orderRows);
		this.OrdesTable.setItems(deliveryInfo);
		
		// Eldad Changed
		textFieldOrderCode.textProperty().addListener((ob,oldValue,newValue) -> {
			
			deliveryInfo = FXCollections.observableArrayList(orderRows.stream()
					.filter(orderRow -> (String.valueOf(orderRow.getOrderCode()).startsWith(newValue)) )
					.collect(Collectors.toList()));
			this.OrdesTable.setItems(deliveryInfo);
		});

		// Refresh the OrdesTable
		this.OrdesTable.refresh();
	}

	/**
	 * This class represents an order in the table of orders.
	 */
	public class OrderTable {
		private int orderCode, facilityID;
		private double finalPrice;
		private Button OrderDetails;

		// added by Gal.
		private CheckBox checkBox;

		/**
		 * Constructor for the OrderTable class.
		 *
		 * @param orderCode        The code for the order.
		 * @param finalPrice       The final price of the order.
		 * @param invoiceConfirmed A boolean indicating whether the invoice for the
		 *                         order has been confirmed.
		 * @param facilityID       The ID of the facility associated with the order.
		 */

		public OrderTable(int orderCode, double finalPrice, int facilityID) {
			this.orderCode = orderCode;
			this.finalPrice = finalPrice;
			this.facilityID = facilityID;

		}

		/**
		 * Method to create a button for the OrderDetails field of the OrderTable
		 * object. When clicked, the button will show the details of the order.
		 */
		public void createButton() {
			// Create the OrderDetails button
			OrderDetails = new Button("Order Details");
			// Add an event handler for when the button is clicked
			OrderDetails.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				// Print the order code to the console
				System.out.println(this.orderCode);
				// Set the order details for the current manager
				ClientUI.clientController.setManagerOrderdeatils(this.orderCode);
				// Show the MyOrderDetails.fxml scene
				ClientUI.sceneManager.ShowSceneNew("../views/MyOrderDetails.fxml");
			});
		}

		/**
		 * 
		 * Returns the orderCode of the order.
		 * 
		 * @return The orderCode of the order.
		 */
		public int getOrderCode() {
			return orderCode;
		}

		/**
		 * Sets the orderCode of the order.
		 * 
		 * @param orderCode The orderCode to set for the order.
		 */
		public void setOrderCode(int orderCode) {
			this.orderCode = orderCode;
		}

		/**
		 * 
		 * Returns the finalPrice of the order.
		 * 
		 * @return The finalPrice of the order.
		 */
		public double getFinalPrice() {
			return finalPrice;
		}

		/**
		 * Sets the finalPrice of the order.
		 * 
		 * @param finalPrice The finalPrice to set for the order.
		 */
		public void setFinalPrice(double finalPrice) {
			this.finalPrice = finalPrice;
		}

		/**
		 * 
		 * Returns the facilityID of the order.
		 * 
		 * @return The facilityID of the order.
		 */
		public int getFacilityID() {
			return facilityID;
		}

		/**
		 * Sets the facilityID of the order.
		 * 
		 * @param facilityID The facilityID to set for the order.
		 */
		public void setFacilityID(int facilityID) {
			this.facilityID = facilityID;
		}

		/**
		 * Returns the OrderDetails button of the order.
		 * 
		 * @return The OrderDetails button of the order.
		 */
		public Button getOrderDetails() {
			return OrderDetails;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

	}

	/**
	 * This method updates the data of the orders table with the data received from
	 * the server.
	 *
	 * @param data an Object that contains the data received from the server
	 */
	@Override
	public void updatedata(Object data) {
		System.out.println(1);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;

			switch (serverResponse.getRequest()) {
			case "#GET_MY_ORDERS": {

				// Iterate through the rows of server response data

				for (int i = 0; i < serverResponse.Responsedata.size(); i++) // Rows
				{
					OrderTable TempOrder;
					System.out.println("in loop");
					Object[] values = (Object[]) serverResponse.Responsedata.get(i);
					// save all order data.
					int orderCode = (Integer) values[0];
					double finalPrice = (Double) values[1];
					int facilityID = (Integer) values[3];
					TempOrder = new OrderTable(orderCode, finalPrice, facilityID);
					OrdersData.add(TempOrder);

				}

				break;
			}
			case "#GET_MY_DELIVERYS":
				for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(i);
					int isReceiverdByCustomer = (Integer)values[1];
					deliveryOrderCodes.put((Integer) values[0],isReceiverdByCustomer);
	
				}

			}
		}

	}

}
