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

	
	/**
	 * inner class for the rows of the table.
	 * DeliveryRow class saves information about Deliveries which are presented in the table, such that order code, delivery location, delivery status,
	 * and providers acceptance status.
	 * @author galmu
	 *
	 */
	public class DeliveryRow {
		private int orderCode;
		private String deliveryLocation;
		private String deliveryStatus;
		private CheckBox acceptCheckBox;
		

		/**
		 * Constructor of DeliveryRow class.
		 * @param orderCode
		 * @param deliveryLocation
		 * @param deliveryStatus
		 * @param acceptCheckBox
		 */
		public DeliveryRow(int orderCode, String deliveryLocation, String deliveryStatus, CheckBox acceptCheckBox) {
			this.orderCode = orderCode;
			this.deliveryLocation = deliveryLocation;
			this.deliveryStatus = deliveryStatus;
			this.acceptCheckBox = acceptCheckBox; 
		}

		/*standard getters and setters */
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
	/**
	 * ArrayList which saves all the data about the relevant deliveries.
	 */
	private ArrayList<DeliveryRow> deliveryRows;
	@FXML
	private Button CloseButton;
	@FXML
	private Text providerTextArea;

	@FXML
	private TableView<DeliveryRow> DeliveryOrdersTable;

	@FXML
	private Button Logout;

	//DeliveryOrdersTable columns.
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

	
	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @author galmu
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		logOut(event);
	}

	/**
	 * method that triggers when the Logout button has been pressed
	 * this method sends a query to which sets the user status to be Offline and thus log's him out from his account.
	 * 
	 * @author galmu
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logOut(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS", 
					String.format("%s#",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	/**
	 * initialises the controller as it is loaded. 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//setting the static clientController to be this controller.
		ClientUI.clientController.setController(this);

		//updating all the info about the current logged in user, in the Text elements of the GUI.
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		//query which retrieves the area of the current logged in user - (provider of some area). 
		RequestObjectClient userArea = new RequestObjectClient("#USER_AREA_PROV", String.format(
				"%s#",
				ClientUI.clientController.getUser().getUserName()), "*");
		ClientUI.clientController.accept(userArea);

		providerTextArea.setText("Provider: " + userAreaStr);

		//initialising the array to save the relevant deliveries.
		deliveryRows = new ArrayList<>();

		//method which refreshes the table after changes have been made.
		refresh();


		//initialising the columns of the table.
		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Integer>("orderCode")); 
		orderCodeColumn.setResizable(false);
		deliveryLocationColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, String>("deliveryLocation"));
		deliveryLocationColumn.setResizable(false);
		deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, String>("deliveryStatus"));
		deliveryLocationColumn.setResizable(false);
		acceptOrderColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, CheckBox>("acceptCheckBox"));
		acceptOrderColumn.setResizable(false);

		
		//inserting all the deliveryRows into the table via ObservableList.
		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		DeliveryOrdersTable.setItems(deliveryInfo);
		DeliveryOrdersTable.refresh();
	}

	/**
	 * constructing the table from scratch.
	 * clearing all the deliveries and retrieving them again from the DB.
	 * this method usually called after some changes have been made in the table and the DB.
	 */
	private void refresh() {
		deliveryRows.clear();
		DeliveryOrdersTable.getItems().clear();
		//query which retrieves all the orders with delivery from the provider's area (userAreaStr).
		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_PROVIDER_ORDERS_DELIVERY", // DONE
				String.format("%s#",userAreaStr),"*");
		ClientUI.clientController.accept(deliveries);

		/**
		 * for each delivery row received from the query we need to set the corresponding check box state.
		 * if the delivery status is "SentToProvider" - the check box will not be selected.
		 * else the check box will be set as selected.
		 */
		for (DeliveryRow d : deliveryRows) {
			CheckBox checkBox = new CheckBox();
			if (d.getDeliveryStatus().equals("SentToProvider")) {
				checkBox.setSelected(false);
			} else if (d.getDeliveryStatus().equals("Dispatched"))
				checkBox.setSelected(true);
			
			// defining the on action method of the check box:
			/** when the check box is clicked by the provider:
			 * 	1. a date of week forward is calculated via the Calendar class - this is the estimated delivery time.
			 *  2. when the provider click on the check box - he updates the delivery status.
			 */
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
				if (checkBox.isSelected()) {
					//query which changes the delivery status from SentToProvider to Dispatched
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS_DISPATCHED", 
							String.format("%d#'%s'#",d.getOrderCode(),estTime),"PUT");
					ClientUI.clientController.accept(activateRequest);
					
					//query which selects the order code and notifies the connected customer about the estimated delivery time.
					RequestObjectClient updateEstTimeForUser = new RequestObjectClient("#UPDATE_EST_TIME_FOR_USER#SEND_NOT_ME",
							String.format("%d#",d.getOrderCode()),"*");
					ClientUI.clientController.accept(updateEstTimeForUser);	
					
				} else {
					////query which changes the delivery status from Dispatched to SentToProvider
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS_SENTTOPROVIDER", 
							String.format("%d#",d.getOrderCode()),"PUT");
					ClientUI.clientController.accept(activateRequest);
				}
				//after all changes have been made - an info pop up shows up, and the table is refreshed.
				Alert info = new Alert(AlertType.INFORMATION);
				info.setContentText("Status Updated Successfully!");
				info.showAndWait();
				refresh();
			});
			//finally the delivery row of the table is set with the combo box with all of that functionallity.
			d.setAcceptCheckBox(checkBox);
		}
		
		//inserting all the deliveryRows into the table via ObservableList.
		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		DeliveryOrdersTable.setItems(deliveryInfo);
	}

	
	/**
	 * saving all the data which is returned from the DB and actual for the current controller.
	 * saves the user area.
	 * saves the deliveries of that area.
	 */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			//in this case we save the area of the user in userAreaStr.
			case "#USER_AREA_PROV":
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);// Row 1
					userAreaStr = (String) values[0];
				}
				break;

			// in this case we save all the orders with delivery which have been made in this area.
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
