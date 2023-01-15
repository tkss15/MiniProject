package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Delivery.DeliveryStatus;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class DeliveryOperatorInterfaceController implements IController, Initializable {

	/**
	 * inner class for the rows of the table. DeliveryRow class saves information
	 * about Deliveries which are presented in the table, such that order code,
	 * customer approval status and the delivery status.
	 *
	 */

	public class DeliveryRow {
		private int orderCode;

		private boolean customerApproval;
		private DeliveryStatus deliveryStatus;
		private ComboBox<DeliveryStatus> deliveryStatusCombobox;

		/**
		 * Constructor of DeliveryRow class.
		 * 
		 * @param orderCode
		 * @param customerApproval
		 * @param deliveryStatus
		 * @param deliveryStatusCombobox
		 */
		public DeliveryRow(int orderCode, int customerApproval, DeliveryStatus deliveryStatus,
				ComboBox<DeliveryStatus> deliveryStatusCombobox) {
			this.orderCode = orderCode;
			if (customerApproval == 1) {
				this.customerApproval = true;
			} else
				this.customerApproval = false;

			this.deliveryStatus = deliveryStatus;
			this.deliveryStatusCombobox = deliveryStatusCombobox;
		}

		/* standard getters and setters. */
		public int getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(int orderCode) {
			this.orderCode = orderCode;
		}

		public boolean isCustomerApproval() {
			return customerApproval;
		}

		public void setCustomerApproval(boolean customerApproval) {
			this.customerApproval = customerApproval;
		}

		public DeliveryStatus getDeliveryStatus() {
			return deliveryStatus;
		}

		public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
			this.deliveryStatus = deliveryStatus;
		}

		public ComboBox<DeliveryStatus> getDeliveryStatusCombobox() {
			return deliveryStatusCombobox;
		}

		/**
		 * method which sets the delivery status combo box items with the current value
		 * of the combo box + "Done". this method is called after the customer accepts
		 * the delivery, so the delivery operator can change the delivery status from
		 * current status to "Done".
		 * 
		 * @param deliveryStatusCombobox
		 * @param curr                   - the current status of the delivery.
		 */
		public void setDeliveryStatusCombobox(ComboBox<DeliveryStatus> deliveryStatusCombobox, String curr) {

			this.deliveryStatusCombobox = deliveryStatusCombobox;
			this.deliveryStatusCombobox.getItems().addAll(DeliveryStatus.valueOf(curr), DeliveryStatus.valueOf("Done"));
			deliveryStatusCombobox.setValue(deliveryStatus);
		}

	}

	private String userAreaStr;
//	private String userRoleStr;
	/**
	 * ArrayList which saves all the data about the relevant deliveries.
	 */
	private ArrayList<DeliveryRow> deliveryRows;

	@FXML
	private Button CloseButton;

	private ComboBox<DeliveryStatus> combo;

	@FXML
	private Text DeliveryOperatorAreaText;

	@FXML
	private Button refreshBTN;
	@FXML
	private Button changesBTN;
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
	private Text branch;

	@FXML
	private Button Logout;

	/**
	 * the next 4 TableViews are the column of the DeliveryOrdersTable.
	 */
	@FXML
	private TableView<DeliveryRow> DeliveryOrdersTable;

	@FXML
	private TableColumn<DeliveryRow, Integer> orderCodeColumn;

	@FXML
	private TableColumn<DeliveryRow, Boolean> customerApprovalStatusColumn;

	@FXML
	private TableColumn<DeliveryRow, ComboBox<DeliveryStatus>> deliveryStatusColumn;

	/**
	 * initialises the controller as it is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// setting the static clientController to be this controller.
		ClientUI.clientController.setController(this);

		// updating all the info about the current logged in user, in the Text elements
		// of the GUI.
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		// request a query which gets the current logged in user area.
		RequestObjectClient userArea = new RequestObjectClient("#USER_AREA_DELOP",
				String.format("%s#", ClientUI.clientController.getUser().getUserName()), "*");
		ClientUI.clientController.accept(userArea);

		// setting the second title text to the correct user area.
		DeliveryOperatorAreaText.setText("Delivery Operator: " + userAreaStr);

		// initialising the delivery rows array list.
		deliveryRows = new ArrayList<>();

		getDeliveries();
		
		if(deliveryRows.isEmpty()) {
			changesBTN.setDisable(true);
		}
		else changesBTN.setDisable(false);

		// initialising the columns of the DeliveryOrdersTable.
		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Integer>("orderCode"));
		orderCodeColumn.setResizable(false);
		customerApprovalStatusColumn
				.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Boolean>("customerApproval"));
		customerApprovalStatusColumn.setResizable(false);
		deliveryStatusColumn.setCellValueFactory(
				new PropertyValueFactory<DeliveryRow, ComboBox<DeliveryStatus>>("deliveryStatusCombobox"));
		deliveryStatusColumn.setResizable(false);

		// inserting all the deliveryRows into the table via ObservableList.
		final ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		DeliveryOrdersTable.setItems(deliveryInfo);
//		DeliveryOrdersTable.refresh();

	}

	/**
	 * saves all the deliveries of the area in the deliveryRows, and adds check
	 * boxes with current delivery status. finally sets the
	 */
	private void getDeliveries() {
		deliveryRows.clear();
		// request a query which gets all the deliveries which were made in the current
		// area - which is stored in the userAreaStr.
		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_ORDERS_DELIVERY",
				String.format("%s#", userAreaStr), "*");
		ClientUI.clientController.accept(deliveries);

		// for each DeliveryRow object, a delivery status combo box is added.
		for (DeliveryRow d : deliveryRows) {
			combo = new ComboBox<>();
			combo.setOnAction((e) -> {
				d.setDeliveryStatus(d.getDeliveryStatusCombobox().getValue());

			});
			// if the customer has not approved reception of the delivery, the combo box is
			// disabled so the Delivery cannot change it's status to "Done".
			if (!d.isCustomerApproval()) {
				combo.setDisable(true);
				combo.setOpacity(100);
			}
			// initialising the prompt text of the combo box according to the delivery
			// status retrieved from the DB, and setting the delivery status combo box of
			// the
			// current specific row with that combo box.
			combo.setPromptText(d.deliveryStatus.toString());
			d.setDeliveryStatusCombobox(combo, d.deliveryStatus.toString());
		}
	}

	/**
	 * saving all the data which is returned from the DB and relevant for the
	 * current controller. saving the deliveries in deliveryRows. saving the user
	 * area in userAreaStr.
	 */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			// in this case we construct a DeliveryRow object which contains data about a
			// specific delivery of the current area.
			case "#DELIVERY_OPERATOR_ORDERS_DELIVERY":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);// Row 1
						int orderCode = (Integer) values[0];
						String deliveryStatus = (String) values[1];
						System.out.println(deliveryStatus);
						DeliveryStatus d = DeliveryStatus.valueOf(deliveryStatus);

						int customerApproval = (Integer) values[2];

						DeliveryRow deliveryRow = new DeliveryRow(orderCode, customerApproval, d, null);
						deliveryRows.add(deliveryRow);
						i++;

					}

				}
				break;
			// in this case we get the area of the current logged user in userAreaStr.
			case "#USER_AREA_DELOP":
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);// Row 1
					userAreaStr = (String) values[0];
				}
			}
		}
	}

	/**
	 * applyChangedToTable saves the delivery status which is set by the user
	 * (Delivery operator). this method is triggered when the "apply changes" button
	 * is pressed. this method sets the delivery status to be the delivery status
	 * which the delivery operator has selected in the combo box.
	 * 
	 * @param event
	 */
	@FXML
	void applyChangesToTable(ActionEvent event) {
		for (DeliveryRow d : deliveryRows) {
			if (!d.getDeliveryStatusCombobox().isDisable()) {
				String deliveryStatus = d.getDeliveryStatusCombobox().getValue().toString();
				// request a query which sets the current delivery's status to be exactly the
				// status which is selected in the corresponding combo box.
				RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_SET_DELIVERY_STATUS",
						String.format("%s#%s#", Integer.toString(d.getOrderCode()), deliveryStatus), "PUT");
				ClientUI.clientController.accept(deliveries);
			}
		}

		// inserting all the deliveryRows into the table via ObservableList.
		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		this.DeliveryOrdersTable.setItems(deliveryInfo);
//		this.DeliveryOrdersTable.refresh();

		// showing a pop up alert to the user which informs him that the changes which
		// he made were completed successfully.
		Alert confirmationMsg = new Alert(AlertType.INFORMATION);
		confirmationMsg.setContentText("Changes have been successfully saved.");

		confirmationMsg.showAndWait();

	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	public void closeWindow(ActionEvent e) {
		logOut(e);
	}

	/**
	 * method that triggers when the Logout button has been pressed this method
	 * sends a request to make a query which sets the user status to be Offline and
	 * thus log's him out from his account.
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	public void logOut(ActionEvent e) {
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", e);
	}

	@FXML
	/**
	 * invoked when the user clicks on the refresh button.
	 * refreshes the table and updates it according to the current info in the DB.
	 * @param event the ActionEvent that triggered this method call
	 */
	void refreshTable(ActionEvent event) {
		getDeliveries();
		
		if(deliveryRows.isEmpty()) {
			changesBTN.setDisable(true);
		}
		else changesBTN.setDisable(false);
		
		// inserting all the deliveryRows into the table via ObservableList.
		ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		this.DeliveryOrdersTable.setItems(deliveryInfo);

	}

}
