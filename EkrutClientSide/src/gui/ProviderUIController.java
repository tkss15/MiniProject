package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class ProviderUIController implements IController, Initializable {

	public class DeliveryRow {
		// row for the table.
		private int orderCode;
		private String deliveryLocation;
		private String deliveryStatus;
		private CheckBox acceptCheckBox;

		public DeliveryRow(int orderCode, String deliveryLocation, String deliveryStatus, CheckBox acceptCheckBox) {
			this.orderCode = orderCode;
			this.deliveryLocation = deliveryLocation;
			this.deliveryStatus = deliveryStatus;
			this.acceptCheckBox = acceptCheckBox; 
		}

		public CheckBox getAcceptCheckBox() {
			return acceptCheckBox;
		}

		public void setAcceptCheckBox(CheckBox acceptCheckBox) {
			this.acceptCheckBox = acceptCheckBox;
		}

		public int getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(int orderCode) {
			this.orderCode = orderCode;
		}

		public String getDeliveryLocation() {
			return deliveryLocation;
		}

		public void setDeliveryLocation(String deliveryLocation) {
			this.deliveryLocation = deliveryLocation;
		}

		public String getDeliveryStatus() {
			return deliveryStatus;
		}

		public void setDeliveryStatus(String deliveryStatus) {
			this.deliveryStatus = deliveryStatus;
		}

	}

	private String userAreaStr;
	private ArrayList<DeliveryRow> deliveryRows;
	@FXML
	private Button CloseButton;
	@FXML
	private Text providerTextArea;

	@FXML
	private TableView<DeliveryRow> DeliveryOrdersTable;

	@FXML
	private Button Logout;

	@FXML
	private TableColumn<DeliveryRow, Integer> orderCodeColumn;

	@FXML
	private TableColumn<DeliveryRow, String> deliveryLocationColumn;
	@FXML

	private TableColumn<DeliveryRow, String> deliveryStatusColumn;
	@FXML
	private TableColumn<DeliveryRow, CheckBox> acceptOrderColumn;

	@FXML
	private Text textEmail;

	@FXML
	private Text textFirstName;

	@FXML
	private Text textID;

	@FXML
	private Text textLastName;

	@FXML
	private Text textTelephone;

	@FXML
	private Text textUserlogin;

	@FXML
	void applyChangesToTable(ActionEvent event) {
		
	}

	@FXML
	void closeWindow(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void logOut(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS", // DONE
					String.format("%s#",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);

		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		RequestObjectClient userArea = new RequestObjectClient("#USER_AREA_PROV", String.format( //DONE
				"%s#",
				ClientUI.clientController.getUser().getUserName()), "*");
		ClientUI.clientController.accept(userArea);

		providerTextArea.setText("Provider: " + userAreaStr);

		deliveryRows = new ArrayList<>();

		refresh();

//			RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_PROVIDER_ORDERS_DELIVERY",
//					String.format("SELECT virtualorders.orderCode,virtualorders.DeliveryLocation,virtualorders.DeliveryStatus "
//					+ "FROM virtualorders "
//					+ "INNER JOIN orders "
//					+ "INNER JOIN facilities "
//					+ "WHERE FacilityArea = '%s'",userAreaStr), "*");
//			ClientUI.clientController.accept(deliveries);
//			
//			for(DeliveryRow d : deliveryRows) {
//				CheckBox checkBox = new CheckBox();
//				checkBox.setOnAction((e) ->{
//					System.out.println(checkBox.isSelected());
//					if(checkBox.isSelected()) {
//						RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS",
//								String.format(
//										"table=virtualorders#condition=orderCode=%d&hasDelivery=1#values=DeliveryStatus='Dispatched'",d.getOrderCode()),
//								"PUT");
//						ClientUI.clientController.accept(activateRequest);						
//					}
//					else {
//						RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS",
//								String.format(
//										"table=virtualorders#condition=orderCode=%d&hasDelivery=1#values=DeliveryStatus='SentToProvider'",d.getOrderCode()),
//								"PUT");
//						ClientUI.clientController.accept(activateRequest);
//					}
//					Alert info = new Alert(AlertType.INFORMATION);
//					info.setContentText("Status Updated Successfully!");
//					info.showAndWait();
//					refresh();
//				});
//				d.setAcceptCheckBox(checkBox);
//			}

		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Integer>("orderCode")); 
		orderCodeColumn.setResizable(false);
		deliveryLocationColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, String>("deliveryLocation"));
		deliveryLocationColumn.setResizable(false);
		deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, String>("deliveryStatus"));
		deliveryLocationColumn.setResizable(false);
		acceptOrderColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, CheckBox>("acceptCheckBox"));
		acceptOrderColumn.setResizable(false);

		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);

		DeliveryOrdersTable.setItems(deliveryInfo);
		DeliveryOrdersTable.refresh();
	}

	private void refresh() {
		deliveryRows.clear();
		DeliveryOrdersTable.getItems().clear();
		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_PROVIDER_ORDERS_DELIVERY", // DONE
				String.format("%s#",userAreaStr),"*");
		ClientUI.clientController.accept(deliveries);

		for (DeliveryRow d : deliveryRows) {
			CheckBox checkBox = new CheckBox();
			if (d.getDeliveryStatus().equals("SentToProvider")) {
				checkBox.setSelected(false);
			} else if (d.getDeliveryStatus().equals("Dispatched"))
				checkBox.setSelected(true);
			checkBox.setOnAction((e) -> {
				Calendar CalenderTime = Calendar.getInstance();
				SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
				String timeStamp = simpleFormat.format(CalenderTime.getTime());
				String estTime="";
				try 
				{
					CalenderTime.setTime(simpleFormat.parse(timeStamp));
					CalenderTime.add(Calendar.DATE, 7);
					estTime = simpleFormat.format(CalenderTime.getTime());
				} 
				catch (ParseException exc) 
				{
					exc.printStackTrace();
				}
				System.out.println(checkBox.isSelected());
				if (checkBox.isSelected()) {
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS_DISPATCHED", // DONE
							String.format("%d#'%s'#",d.getOrderCode(),estTime),"PUT");
					ClientUI.clientController.accept(activateRequest);
					
					RequestObjectClient updateEstTimeForUser = new RequestObjectClient("#UPDATE_EST_TIME_FOR_USER#SEND_NOT_ME", // DONE
							String.format("%d#",d.getOrderCode()),"*");
					ClientUI.clientController.accept(updateEstTimeForUser);	
					
				} else {
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS_SENTTOPROVIDER", // DONE
							String.format("%d#",d.getOrderCode()),"PUT");
					ClientUI.clientController.accept(activateRequest);
				}
				Alert info = new Alert(AlertType.INFORMATION);
				info.setContentText("Status Updated Successfully!");
				info.showAndWait();
				refresh();
			});
			d.setAcceptCheckBox(checkBox);
		}
		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		DeliveryOrdersTable.setItems(deliveryInfo);
//		DeliveryOrdersTable.refresh();
	}

	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#USER_AREA_PROV":
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);// Row 1
					userAreaStr = (String) values[0];
				}
				break;

			case "#DELIVERY_PROVIDER_ORDERS_DELIVERY":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						int orderCode = (Integer) values[0];
						String deliveryLocation = (String) values[1];
						String deliveryStatus = (String) values[2];

						DeliveryRow d = new DeliveryRow(orderCode, deliveryLocation, deliveryStatus, null);
						deliveryRows.add(d);
						i++;
					}
				}
				break;
			}

		} 

	}
}
