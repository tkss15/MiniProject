package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.OrderRow;
import Entity.OrderRowDetails;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class MyOrdersDetailsController implements Initializable, IController {

	private ArrayList<OrderRowDetails> orderRows;

	private ArrayList<OrderDetails> OrderData;

	private String Date;
	private Double finalPrice;
	@FXML
	private Button CloseButton;

	@FXML
	private TableColumn<OrderRow, Double> FinalPrice;

	@FXML
	private ImageView LogoImage;

	@FXML
	private Label OrderNumber;

	@FXML
	private TableView<OrderRowDetails> OrdersDetails;

	@FXML
	private TableColumn<OrderRow, Integer> ProductAmount;

	@FXML
	private TableColumn<OrderRow, Integer> ProductCode;

	@FXML
	private TableColumn<OrderRow, String> ProductName;

	@FXML
	private TableColumn<OrderRow, Double> ProductPrice;

	@FXML
	private Label TotalPrIce;
	
    @FXML
	    private Label Total;

	@FXML
	private Label date;

	@FXML
	private Label number;

	@FXML
	void closeWindow(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	/**

	Initialize method for the Order Details UI. This method is called when the UI is first displayed.

	It sets up the cell value factories for the table columns and retrieves the order details from the database.

	@param location The location of the UI.

	@param resources The resources used to display the UI.
	*/

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set the client controller for this UI
		ClientUI.clientController.setController(this);
		
		// Initialize the OrderData(An array containing all the rows of the table with the information imported from the query) and orderRows(Contains all rows of the table) lists
		OrderData = new ArrayList<>();
		orderRows = new ArrayList<>();

		
		// Set up the cell value factories for the table columns
		ProductName.setCellValueFactory(new PropertyValueFactory<OrderRow, String>("ProductName"));
		ProductCode.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("ProductCode"));
		ProductAmount.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("ProductAmount"));
		ProductPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Double>("ProductPrice"));
		FinalPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Double>("FinalPrice"));


		// Retrieve the order details from the database by a query


		RequestObjectClient request1 = new RequestObjectClient("#GET_ORDER_DETAILS", String.format("%d#", ClientUI.clientController.getManagerOrderdeatils()), "*");

		ClientUI.clientController.accept(request1);
		// Retrieve the order date from the database by a query.
		RequestObjectClient request2 = new RequestObjectClient("#GET_ORDER_DATE", String.format("%d#",ClientUI.clientController.getManagerOrderdeatils()), "GET");

		ClientUI.clientController.accept(request2);
		// Set the order number and date fields
		number.setText(String.valueOf(ClientUI.clientController.getManagerOrderdeatils()));
		date.setText("Date: "+ Date);
		
		// Retrieve the final price for the order from the database by a query.
		RequestObjectClient request3 = new RequestObjectClient("#GET_ORDER_FINALPRICE", String.format("%d#",ClientUI.clientController.getManagerOrderdeatils()), "GET");
		ClientUI.clientController.accept(request3);
		
		// Set the final price field of the order.
		Total.setText(finalPrice+"");
		
       // Create OrderRowDetails objects for each OrderDetails object in the OrderData list.
		for (int i = 0; i < OrderData.size(); i++) 
		{
			OrderDetails temp = OrderData.get(i);
			OrderRowDetails orderRow = new OrderRowDetails(temp.ProductName, temp.ProductCode, temp.ProductAmount,
					temp.ProductPrice, temp.FinalPrice);
			orderRows.add(orderRow);

		}

		final ObservableList<OrderRowDetails> deliveryInfo = FXCollections.observableArrayList(orderRows);
		this.OrdersDetails.setItems(deliveryInfo);
		this.OrdersDetails.refresh();

	}
	/**

	The OrderDetails class represents a single order, containing information about a product such as its name, code,

	amount, price, and final price.
	*/
	public class OrderDetails {
		private String ProductName;
		private Integer ProductCode;
		private Integer ProductAmount;
		private double ProductPrice;
		private double FinalPrice;
		/**

		Constructs a new OrderDetails object with the given product name, code, amount, price, and final price.

		@param ProductName The name of the product.

		@param ProductCode The code of the product.

		@param ProductAmount The amount of the product.

		@param ProductPrice The price of the product.

		@param FinalPrice The final price of the product.
		*/
		public OrderDetails(String ProductName, Integer ProductCode, Integer ProductAmount, double ProductPrice,
				double FinalPrice) {
			this.ProductName = ProductName;
			this.ProductCode = ProductCode;
			this.ProductAmount = ProductAmount;
			this.ProductPrice = ProductPrice;
			this.FinalPrice = FinalPrice;

	
		}
		/**

		Returns the name of the product.
		@return The name of the product.
		*/

		public String getProductName() {
			return ProductName;
		}
		/**

		Sets the name of the product.
		@param productName The name of the product.
		*/

		public void setProductName(String productName) {
			ProductName = productName;
		}
		/**

		Returns the code of the product.
		@return The code of the product.
		*/

		public Integer getProductCode() {
			return ProductCode;
		}
		/**

		Sets the code of the product.
		@param productCode The code of the product.
		*/

		public void setProductCode(Integer productCode) {
			ProductCode = productCode;
		}
		/**

		Returns the amount of the product.
		@return The amount of the product.
		*/
		public Integer getProductAmount() {
			return ProductAmount;
		}
		/**

		Sets the amount of the product.
		@param productAmount The amount of the product.
		*/
		public void setProductAmount(Integer productAmount) {
			ProductAmount = productAmount;
		}
		/**

		Returns the price of the product.
		@return The price of the product.
		*/
		public double getProductPrice() {
			return ProductPrice;
		}
		/**

		Sets the price of the product.
		@param productPrice The price of the product.
		*/
		public void setProductPrice(double productPrice) {
			ProductPrice = productPrice;
		}
		/**

		Returns the final price of the product.
		@return The final price of the product.
		*/
		public double getFinalPrice() {
			return FinalPrice;
		}
		/**

		Sets the final price of the product.
		@param The final price of the product.
		*/
		public void setFinalPrice(double finalPrice) {
			FinalPrice = finalPrice;
		}

	}
	
	/**

	This method is called when data is updated from the server. It processes the data based on the request type.

	@param data The data received from the server.
	*/
	
	@Override
	public void updatedata(Object data) {
		// Check if the data is a ResponseObject
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			 // Process the data based on the request type
			switch (serverResponse.getRequest()) {
			case "#GET_ORDER_DETAILS": 
				 // Loop through all rows of data
				for (int i = 0; i < serverResponse.Responsedata.size(); i++) // Rows
				{
					OrderDetails TempOrder;
					// Retrieve the data for the current row
					Object[] values = (Object[]) serverResponse.Responsedata.get(i);
					// save all order data.
					String ProductName = (String) values[0];
					Integer ProductCode = (Integer) values[1];
					Integer ProductAmount = (Integer) values[2];
					double ProductPrice = (Double) values[3];
					double FinalPrice = (Double) values[4];
					TempOrder = new OrderDetails(ProductName, ProductCode, ProductAmount, ProductPrice, FinalPrice);
					OrderData.add(TempOrder);

				}

				break;
			case "#GET_ORDER_DATE":{
				// Retrieve the data and save the orderDate
				Object[] values = (Object[]) serverResponse.Responsedata.get(0); 
				Date = (String) values[0];
				break;
			}
			case "#GET_ORDER_FINALPRICE":
				// Retrieve the data and save the finalPrice of the order.
				Object[] values = (Object[]) serverResponse.Responsedata.get(0);
				finalPrice = (Double) values[0];
				break;
				
			}
			
		}

	}
}
