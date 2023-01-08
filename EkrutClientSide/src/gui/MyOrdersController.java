package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Order;
import Entity.OrderRow;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MyOrdersController implements Initializable, IController {

	private ArrayList<OrderRow> orderRows;
	private ArrayList<OrderTable> OrdersData;
	@FXML
	private ImageView BackButton;

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
	private TableColumn<OrderRow, Boolean> IsInvoiceConfirmed;

	@FXML
	private ImageView ShoppingImage;

	@FXML
	private ImageView logoImage;

	@FXML
	void closeWindow(ActionEvent event) {
		System.exit(0);

	}

	@FXML
	void BackButton(ActionEvent event) {

		ClientUI.sceneManager.ShowSceneNew("../views/HomePage.fxml", event);
	}

	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		OrdersData = new ArrayList<>();
		orderRows = new ArrayList<>();
		System.out.println(Thread.currentThread().getName());
		// initialization of order column
		OrderCode.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("orderCode"));
		FinalPrice.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("finalPrice"));
		IsInvoiceConfirmed.setCellValueFactory(new PropertyValueFactory<OrderRow, Boolean>("invoiceConfirmed"));
		FacilityID.setCellValueFactory(new PropertyValueFactory<OrderRow, Integer>("facilityID"));
		OrderDetails.setCellValueFactory(new PropertyValueFactory<OrderRow, Button>("orderDetails"));

		System.out.println(ClientUI.clientController.getUser().getUserName());
		// making query to db.
		RequestObjectClient request = new RequestObjectClient("ShowOrders",
				String.format("table=orders#condition=userName=%s", ClientUI.clientController.getUser().getUserName()),
				"GET");
		ClientUI.clientController.accept(request);

		for (int i = 0; i < OrdersData.size(); i++) {
			OrderTable temp = OrdersData.get(i);
			temp.createButton();
			OrderRow orderRow = new OrderRow(temp.orderCode, temp.finalPrice, temp.invoiceConfirmed, temp.facilityID,
					temp.OrderDetails);
			orderRows.add(orderRow);

		}

		final ObservableList<OrderRow> deliveryInfo = FXCollections.observableArrayList(orderRows);
		this.OrdesTable.setItems(deliveryInfo);
		this.OrdesTable.refresh();
	}

	public class OrderTable {
		private int orderCode, facilityID;
		private double finalPrice;
		private Boolean invoiceConfirmed;
		private Button OrderDetails;

		public OrderTable(int orderCode, double finalPrice, Boolean invoiceConfirmed, int facilityID) {
			this.orderCode = orderCode;
			this.finalPrice = finalPrice;
			this.invoiceConfirmed = invoiceConfirmed;
			this.facilityID = facilityID;

		}

		public void createButton() {
			OrderDetails = new Button("Order Details");
			OrderDetails.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				System.out.println(this.orderCode);
				ClientUI.clientController.setManagerOrderdeatils(this.orderCode);
				ClientUI.sceneManager.ShowScene("../views/MyOrderDetails.fxml");
			});

		}

		public int getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(int orderCode) {
			this.orderCode = orderCode;
		}

		public double getFinalPrice() {
			return finalPrice;
		}

		public void setFinalPrice(double finalPrice) {
			this.finalPrice = finalPrice;
		}

		public int getFacilityID() {
			return facilityID;
		}

		public void setFacilityID(int facilityID) {
			this.facilityID = facilityID;
		}

		public Boolean getInvoiceConfirmed() {
			return invoiceConfirmed;
		}

		public void setInvoiceConfirmed(Boolean invoiceConfirmed) {
			this.invoiceConfirmed = invoiceConfirmed;
		}

		public Button getOrderDetails() {
			return OrderDetails;
		}

	}

	@Override
	public void updatedata(Object data) {
		System.out.println(1);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;

			switch (serverResponse.getRequest()) {
			case "ShowOrders": {

				for (int i = 0; i < serverResponse.Responsedata.size(); i++) // Rows
				{
					OrderTable TempOrder;
					System.out.println("in loop");
					Object[] values = (Object[]) serverResponse.Responsedata.get(i);
					// save all order data.
					int orderCode = (Integer) values[0];
					double finalPrice = (Double) values[1];
					Boolean invoiceConfirmed = (Boolean) values[2];
					int facilityID = (Integer) values[3];
					TempOrder = new OrderTable(orderCode, finalPrice, invoiceConfirmed, facilityID);
					OrdersData.add(TempOrder);

				}

				break;
			}

			}
		}

	}

}
