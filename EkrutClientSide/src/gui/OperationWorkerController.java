package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Product;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import gui.SalesTableController.SaleRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class OperationWorkerController implements Initializable, IController {

	/**
	 * The RefillRow class represents a row in the tabe "RefillTable" that displays
	 * information about a facility's product that needs to be refilled
	 *
	 */
	public class RefillRow {
		private TextField quantityToRefill;
		private String currentFacilityThreshold;
		private String productQuantity;
		private String facilityName;
		private String facilityLocation;
		private String productName;

		// constructor
		public RefillRow(TextField quantityToRefill, String currentFacilityThreshold, String productQuantity,
				String facilityName, String facilityLocation, String productName) {
			this.quantityToRefill = quantityToRefill;
			this.currentFacilityThreshold = currentFacilityThreshold;
			this.productQuantity = productQuantity;
			this.facilityName = facilityName;
			this.facilityLocation = facilityLocation;
			this.productName = productName;
		}

		// getters and setters for the class.
		public TextField getQuantityToRefill() {
			return quantityToRefill;
		}

		public void setQuantityToRefill(TextField quantityToRefill) {
			this.quantityToRefill = quantityToRefill;
		}

		public String getCurrentFacilityThreshold() {
			return currentFacilityThreshold;
		}

		public void setCurrentFacilityThreshold(String currentFacilityThreshold) {
			this.currentFacilityThreshold = currentFacilityThreshold;
		}

		public String getProductQuantity() {
			return productQuantity;
		}

		public void setProductQuantity(String productQuantity) {
			this.productQuantity = productQuantity;
		}

		public String getFacilityName() {
			return facilityName;
		}

		public void setFacilityName(String facilityName) {
			this.facilityName = facilityName;
		}

		public String getFacilityLocation() {
			return facilityLocation;
		}

		public void setFacilityLocation(String facilityLocation) {
			this.facilityLocation = facilityLocation;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

	}

	private ArrayList<ArrayList<Integer>> productCodeAndFacilityID = new ArrayList<>();
	private ArrayList<RefillRow> refillRows;

	@FXML
	private Button CloseButton;

	@FXML
	private TableView<RefillRow> RefillTable;

	@FXML
	private TableColumn<RefillRow, TextField> RefillCol;

	@FXML
	private TableColumn<RefillRow, String> CurrentThresholdCol;

	@FXML
	private TableColumn<RefillRow, String> QuantityCol;

	@FXML
	private TableColumn<RefillRow, String> FacilityNameCol;

	@FXML
	private TableColumn<RefillRow, String> FacilityLocationCol;

	@FXML
	private TableColumn<RefillRow, String> ProductNameCol;

	@FXML
	private Button RefillButton;

	@FXML
	private Button Logout;

	@FXML
	private Text areaText;

	@FXML
	private Text errorMessage;

	@FXML
	private Text SuccessMessage;

	/**
	 * method that triggers when the Refill button has been pressed this will only
	 * execute the refill process after the worker "refilled" the supply for the
	 * products and filled the text box that represents the new amount after the
	 * replenishment.
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void Refill(ActionEvent event) {
		int countDel = 0;
		boolean flagChecking = false;
		boolean flagAfterFirstFlag = false;
		productCodeAndFacilityID = new ArrayList<>();
		ArrayList<Integer> indexes = new ArrayList<>();
		errorMessage.setVisible(false);
		SuccessMessage.setVisible(false);
		for (int i = 0; i < refillRows.size(); i++) {
			// check if the textbox value is bigger than the product current quantity
			if (!refillRows.get(i).getQuantityToRefill().getText().isEmpty()) {
				if (Integer.parseInt(refillRows.get(i).getQuantityToRefill().getText()) <= Integer
						.parseInt(refillRows.get(i).getProductQuantity())) {
					flagChecking = true;
					flagAfterFirstFlag = true;
				}
				// check if the textbox value is bigger than the current threshold
				if (Integer.parseInt(refillRows.get(i).getQuantityToRefill().getText()) <= Integer
						.parseInt(refillRows.get(i).getCurrentFacilityThreshold())) {
					flagChecking = true;
					flagAfterFirstFlag = true;
				}
				// checks if all of the above conditions are false, only then runs
				// the replenishment process, which getting the row to refill, update it with
				// the current textbox value, and then delete the exeutive order from the DB
				if (!flagChecking) {
					RequestObjectClient getRefillRows = new RequestObjectClient("#OPERATION_REFILL_ROWS",
							String.format("'%s'#'%s'#'%s'#", refillRows.get(i).getFacilityName(),
									refillRows.get(i).getFacilityLocation(), refillRows.get(i).getProductName()),
							"*");

					ClientUI.clientController.accept(getRefillRows);

					int size = productCodeAndFacilityID.size() - 1;
					int freq = Collections.frequency(productCodeAndFacilityID, productCodeAndFacilityID.get(size));
					indexes.add(i);
					// simple check if the row is already in the array
					if (freq == 2) {
						productCodeAndFacilityID.remove(size);
						indexes.remove(indexes.size() - 1);
					}
				}

			}
			flagChecking = false;
		}
		// update the product quantity based on its correct row in the tableView, we
		// check the correct
		// row with the indexes ArrayList, this array list points on the correct row in
		// the tableView
		for (int i = 0; i < productCodeAndFacilityID.size(); i++) {
			ArrayList<Integer> curr = productCodeAndFacilityID.get(i);
			RequestObjectClient updateQuantity = new RequestObjectClient("#OPERATION_UPDATE_QUANTITY",
					String.format("%d#%d#%d#", curr.get(0), curr.get(1),
							Integer.parseInt(refillRows.get(indexes.get(i)).getQuantityToRefill().getText())),
					"PUT");
			ClientUI.clientController.accept(updateQuantity);
			
			RequestObjectClient request = new RequestObjectClient("#UPDATE_PRODUCTS_CLIENT#SEND_NOT_ME",String.format("%d#", curr.get(0)),"*");  
	    	ClientUI.clientController.accept(request); //////////////CHECK/////////////////////////////////////////////////////////////////
		}
		// delete the correct row after replenishment, also uses indexes ArrayList
		for (int i = 0; i < productCodeAndFacilityID.size(); i++) {
			RequestObjectClient deleteFromExecutiveOrder = new RequestObjectClient("#OPERATION_DELETE_ORDER",
					String.format("%s#%s#", refillRows.get(indexes.get(i)).getFacilityName(),
							refillRows.get(indexes.get(i)).getProductName()),
					"DELETE");
			ClientUI.clientController.accept(deleteFromExecutiveOrder);
			RefillTable.getItems().remove(refillRows.get(indexes.get(i)));
			countDel += 1;
		}

		// refresh the table to show the correct data after replenishment
		RefillTable.refresh();

		// if one or more executive orders do not meet the any of the conditions, this
		// flag will turn to "true" and
		// those that do not meet the conditions to the replenishment will not continue
		// the refill process.
		// after the process will finish it will show a message on the screen, based on
		// wether all orders have been
		// completed or not
		if (flagAfterFirstFlag) {
			errorMessage.setText("Not all orders have been fulfilled!\nThis might be becuase you've entered a Quantity"
					+ " after refill that is lower than the threshold or lower than the current quantity!");
			errorMessage.setVisible(true);

		} else {
			if (countDel > 0) {
				SuccessMessage.setText("All fields have been updated");
				SuccessMessage.setVisible(true);
			}

		}
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
		// This method will logout from the current user and finish the client-Side run
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	/**
	 * method that triggers when the Logout button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logOut(ActionEvent event) {
		// logs out from the user and going back to the login screen
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	/**
	 * retrieves the query data from the server to this method, where the query
	 * result set is distributed between all of the cases.
	 * 
	 * @param data that returns from the server
	 * 
	 */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			// get the information from the correct case
			// each case is a different query
			case "#OPERATION_GET_EXECUTIVE_ORDERS":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String currentThreshold = (String) values[5];
						String quantity = (String) values[1];
						String currFacilityName = (String) values[4];
						String FacilityLocation = (String) values[3];
						String productName = (String) values[0];

						RefillRow refillRow = new RefillRow(null, currentThreshold, quantity, currFacilityName,
								FacilityLocation, productName);
						refillRows.add(refillRow);
						i++;
					}
					break;
				}
			case "#OPERATION_REFILL_ROWS":

				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						ArrayList<Integer> curr = new ArrayList<>();
						Integer FacilityID = (Integer) values[0];
						Integer ProductCode = (Integer) values[1];
						curr.add(FacilityID);
						curr.add(ProductCode);
						productCodeAndFacilityID.add(curr);
						i++;
					}
					break;
				}
			}
		}
	}

	/**
	 * Initialize the controller, so that all of the initial settings will be as we
	 * wish this method actives as the controller activates
	 * 
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		errorMessage.setVisible(false);
		SuccessMessage.setVisible(false);
		ClientUI.clientController.setController(this);
		refillRows = new ArrayList<>();
		areaText.setText(ClientUI.clientController.getUser().getArea() + " Area");

		RequestObjectClient request = new RequestObjectClient("#OPERATION_GET_EXECUTIVE_ORDERS",
				String.format("%s#", ClientUI.clientController.getUser().getArea()), "GET");
		ClientUI.clientController.accept(request);

		// set all of the rows
		for (RefillRow rr : refillRows) {
			TextField QuantityInput = new TextField();
			rr.setQuantityToRefill(QuantityInput);
		}

		RefillCol.setCellValueFactory(new PropertyValueFactory<>("quantityToRefill"));
		RefillCol.setEditable(false);
		CurrentThresholdCol.setCellValueFactory(new PropertyValueFactory<>("currentFacilityThreshold"));
		CurrentThresholdCol.setEditable(false);
		QuantityCol.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
		QuantityCol.setEditable(false);
		FacilityNameCol.setCellValueFactory(new PropertyValueFactory<>("facilityName"));
		FacilityNameCol.setEditable(false);
		FacilityLocationCol.setCellValueFactory(new PropertyValueFactory<>("facilityLocation"));
		FacilityLocationCol.setEditable(false);
		ProductNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
		ProductNameCol.setEditable(false);

		ObservableList<RefillRow> info = FXCollections.observableArrayList(refillRows);
		RefillTable.setItems(info);

	}

}
