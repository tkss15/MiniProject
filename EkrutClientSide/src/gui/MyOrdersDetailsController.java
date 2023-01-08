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
		System.exit(0);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		OrderData = new ArrayList<>();
		orderRows = new ArrayList<>();
		System.out.println(Thread.currentThread().getName());
		// initialization of order column
		ProductName.setCellValueFactory(new PropertyValueFactory<OrderRow, String>("ProductName"));
		ProductCode.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("ProductCode"));
		ProductAmount.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("ProductAmount"));
		ProductPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Double>("ProductPrice"));
		FinalPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Double>("FinalPrice"));

		/*
		 * Update - PUT - - URL:
		 * table=subscriber#condition=id=4#values=creditcardnumber=3&subscribernumber=4
		 * - UPDATE subscriber SET subscribernumber = 4, creditcardnumber = 3 WHERE id =
		 * 4; Delete - DELETE - - table=subscriber#values=id=3 - DELETE FROM subscriber
		 * WHERE id=3 Insert - POST - -
		 * table=subscriber#values=id=3&username=tkss15&lastname=shneor - INSERT INTO
		 * subscriber (id,username,lastname) VALUES ('3','tkss15','shneor') SELECT - GET
		 * - table=users#condition=userName=%s#values=userName=username&userPassword=
		 * password - SELECT userPassword, userName FROM users WHERE userName = "tkss15"
		 * Gal Changed WildCard -
		 * 
		 */

		String sql = "SELECT products.productname, products.productcode,productsinorder.productamount, products.productprice, productsInOrder.ProductFinalPrice "
				+ "FROM productsinorder " + "INNER JOIN products ON products.ProductCode "
				+ "WHERE productsinorder.ordercode = " + ClientUI.clientController.getManagerOrderdeatils();
		RequestObjectClient request1 = new RequestObjectClient("ShowDetails", sql, "*");

		ClientUI.clientController.accept(request1);
		
		RequestObjectClient request2 = new RequestObjectClient("Date", "table=orders#condition=orderCode= "
				+ ClientUI.clientController.getManagerOrderdeatils() + "#values=orderdate=orderdate", "GET");

		ClientUI.clientController.accept(request2);
		
		number.setText(String.valueOf(ClientUI.clientController.getManagerOrderdeatils()));
		date.setText("Date: "+ Date);
		
		
		RequestObjectClient request3 = new RequestObjectClient("FinalPrice", "table=orders#condition=orderCode= "
				+ ClientUI.clientController.getManagerOrderdeatils() + "#values=finalprice=finalprice", "GET");
		ClientUI.clientController.accept(request3);
		
		Total.setText(finalPrice+"");

		for (int i = 0; i < OrderData.size(); i++) {
			OrderDetails temp = OrderData.get(i);
			OrderRowDetails orderRow = new OrderRowDetails(temp.ProductName, temp.ProductCode, temp.ProductAmount,
					temp.ProductPrice, temp.FinalPrice);
			orderRows.add(orderRow);

		}

		final ObservableList<OrderRowDetails> deliveryInfo = FXCollections.observableArrayList(orderRows);
		this.OrdersDetails.setItems(deliveryInfo);
		this.OrdersDetails.refresh();

	}

	public class OrderDetails {
		private String ProductName;
		private Integer ProductCode;
		private Integer ProductAmount;
		private double ProductPrice;
		private double FinalPrice;

		public OrderDetails(String ProductName, Integer ProductCode, Integer ProductAmount, double ProductPrice,
				double FinalPrice) {
			this.ProductName = ProductName;
			this.ProductCode = ProductCode;
			this.ProductAmount = ProductAmount;
			this.ProductPrice = ProductPrice;
			this.FinalPrice = FinalPrice;

			// TODO Auto-generated constructor stub
		}

		public String getProductName() {
			return ProductName;
		}

		public void setProductName(String productName) {
			ProductName = productName;
		}

		public Integer getProductCode() {
			return ProductCode;
		}

		public void setProductCode(Integer productCode) {
			ProductCode = productCode;
		}

		public Integer getProductAmount() {
			return ProductAmount;
		}

		public void setProductAmount(Integer productAmount) {
			ProductAmount = productAmount;
		}

		public double getProductPrice() {
			return ProductPrice;
		}

		public void setProductPrice(double productPrice) {
			ProductPrice = productPrice;
		}

		public double getFinalPrice() {
			return FinalPrice;
		}

		public void setFinalPrice(double finalPrice) {
			FinalPrice = finalPrice;
		}

	}

	@Override
	public void updatedata(Object data) {
		System.out.println(1);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;

			switch (serverResponse.getRequest()) {
			case "ShowDetails": 

				for (int i = 0; i < serverResponse.Responsedata.size(); i++) // Rows
				{
					OrderDetails TempOrder;
					System.out.println("in loop");

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
			case "Date":{
				Object[] values = (Object[]) serverResponse.Responsedata.get(0);
				Date = (String) values[0];
				break;
			}
			case "FinalPrice":
				Object[] values = (Object[]) serverResponse.Responsedata.get(0);
				finalPrice = (Double) values[0];
				break;
				
			}
			
		}

	}
}
