package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entity.ProductToRefill;
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
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class RefillExecutiveOrderController implements Initializable, IController {

	String userAreaName;
	private ArrayList<ArrayList<String>> productsBelowThreshold;
	boolean exists = false;
	boolean existsItems = false;
	boolean existsProduct = false;
	boolean existsThreshold = false;
	boolean itemExistsInOrderTable = false;
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;
	private ArrayList<String> items;
	private Integer tempThreshold;
	private ArrayList<ProductToRefill> productsToRefill = new ArrayList<>();
	private ProductToRefill product;
	ObservableList<String> LocationList;
	ObservableList<String> NameList;
	ObservableList<String> ItemsList;
	ObservableList<String> itemsToListView = FXCollections.observableArrayList();
	private HashMap<String, String> productsOfFacility = new HashMap<>();
	@FXML
	private Text facilityNameText;

	@FXML
	private Button backButton;

	@FXML
	private Button CloseButton;

	@FXML
	private Text errorMessageText;

	@FXML
	private Button sendExecutiveOrderButton;

	@FXML
	private Button AddToExecutiveOrderButton;

	@FXML
	private Text successMessageText;

	@FXML
	private Text itemQuantityText;

	@FXML
	private ComboBox<String> LocationCombo;

	@FXML
	private ComboBox<String> NameCombo;

	@FXML
	private ComboBox<String> ItemCombo;

	@FXML
	private ListView<String> itemsListView;

	@FXML
	private Button RemoveItemButton;

	/**
	 * Initialize the controller, so that all of the initial settings will be as we
	 * wish this method actives as the controller activates
	 * 
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@FXML
	void RemoveItem(ActionEvent event) {
		ArrayList<String> items = new ArrayList<>();
		if (itemsListView.getSelectionModel().getSelectedItem() == null)
			return;
		String itemToRemove = itemsListView.getSelectionModel().getSelectedItem();
		String[] parts = itemToRemove.split("-");
		for (int i = 0; i < productsToRefill.size(); i++) {
			// get the relevent item and remove it from the productsToRefill ArrayList
			ProductToRefill curr = productsToRefill.get(i);
			if (curr.getProductName().equals(parts[0]) && curr.getFacilityName().equals(parts[1])) {
				productsToRefill.remove(i);
				break;
			}
		}
		productsOfFacility.remove(itemToRemove, parts[1]);
		itemsToListView.remove(itemToRemove);
		itemsListView.setItems(itemsToListView);
		itemsListView.refresh();

		ItemCombo.getItems().add(parts[0]); // add removed item from ListView back to its relevent ItemCombo ComboBox

	}

	/**
	 * sends all of the items in the ListView - itemsListView , to the executive
	 * order table where the facility operation employee will refill the stock of
	 * the selected item productsToRefill contains all of the data needed to add the
	 * product to the executiveorder table.
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void sendExecutiveOrder(ActionEvent event) {
		errorMessageText.setVisible(false);
		if (productsToRefill.isEmpty()) {
			errorMessageText.setText("No items to send!");
			errorMessageText.setVisible(true);
			return;
		}
		// iterate through the productsToRefill and put the executive order in the
		// table.
		for (int i = 0; i < productsToRefill.size(); i++) {
			// check if the order is already in the table
			ProductToRefill curr = productsToRefill.get(i);
			RequestObjectClient CheckItemInExec = new RequestObjectClient("#REFILL_CHECK_EXEC_ORDER_ITEM",
					String.format("%s#%s#%s#%s#%s#%s#", curr.getProductName(), curr.getProductQuantity(),
							curr.getArea(), curr.getFacilityLocation(), curr.getFacilityName(),
							curr.getFacilityThresholdLevel()),
					"GET");
			ClientUI.clientController.accept(CheckItemInExec);

			if (!itemExistsInOrderTable) {
				// if the order isn't in the table, add it
				RequestObjectClient putToExecOrder = new RequestObjectClient("#REFILL_PUT_TO_EXEC_ORDER",
						String.format("%s#%s#%s#%s#%s#%s#", curr.getProductName(), curr.getProductQuantity(),
								curr.getArea(), curr.getFacilityLocation(), curr.getFacilityName(),
								curr.getFacilityThresholdLevel()),
						"POST");

				ClientUI.clientController.accept(putToExecOrder);
			}
			itemExistsInOrderTable = false; // reset the flag

		}
		productsToRefill.clear();
		Alert info = new Alert(AlertType.INFORMATION); // make an alert about completion of the executive orders
		info.setContentText("Executive order has been sent to the employee!");
		info.showAndWait();
		itemsListView.getItems().clear();
		itemsListView.refresh();
	}

	/**
	 * add the selected item from the comboBox to a ListView and make a
	 * ProductToRefill object with all of the stored data about the product and its
	 * quantity and location, and name, and facility name etc. add the product to
	 * productsToRefill ArrayList of ProductToRefill type.
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void addToExecutiveOrder(ActionEvent event) {
		errorMessageText.setVisible(false);
		if (ItemCombo.getValue() == null)
			return;
		ArrayList<String> items = new ArrayList<>();
		String toOrder = ItemCombo.getValue();
		String toOrderInList = toOrder + "-" + NameCombo.getValue();
		itemsToListView.add(toOrderInList);
		itemsListView.setItems(itemsToListView);
		productsOfFacility.put(toOrderInList, NameCombo.getValue());
		String[] parts = itemQuantityText.getText().split(" ");
		ProductToRefill product = new ProductToRefill(toOrder, parts[2], ClientUI.clientController.getUser().getArea(),
				LocationCombo.getValue(), NameCombo.getValue(), null);

		productsToRefill.add(product);

		ItemsList.clear();
		// get the threshold level of each facility that has the product.
		for (int i = 0; i < productsToRefill.size(); i++) {
			ProductToRefill curr = productsToRefill.get(i);
			RequestObjectClient getFacilityThresholdLevel = new RequestObjectClient("#REFILL_GET_THRESHOLD",
					String.format("%s#%s#", curr.getFacilityLocation(), curr.getFacilityName()), "GET");

			ClientUI.clientController.accept(getFacilityThresholdLevel);

			productsToRefill.get(i).setFacilityThresholdLevel(String.valueOf(tempThreshold));
			existsThreshold = false;
		}
		// rearrange the Item ComboBox.
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(2).equals(NameCombo.getValue()) && curr.get(4).equals(LocationCombo.getValue())) {
				if (!curr.get(0).equals(toOrder)) {
					if (!itemsListView.getItems().contains(curr.get(0) + "-" + NameCombo.getValue()))
						items.add(curr.get(0));
				}
			}

		}
		itemQuantityText.setVisible(false);
		ItemCombo.getItems().addAll(items);

	}

	/**
	 * select the Location name and set the Facility Names below its threshold
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void selectLocation(ActionEvent event) {
		errorMessageText.setVisible(false);
		itemQuantityText.setVisible(false);
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		ArrayList<String> names = new ArrayList<>();
		ItemCombo.setVisible(false);
//		ItemCombo.getItems().clear(); don't need to clear it here, but it is possible
		NameCombo.getItems().clear();
		// add the Facility Names of the location the an arrayList called names
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(4).equals(LocationCombo.getValue())) {
				if (!names.contains(curr.get(2)))
					names.add(curr.get(2));
			}
		}
		// make an observableList from the names ArrayList
		ObservableList<String> namesList = FXCollections.observableArrayList(names);

		NameCombo.getItems().addAll(namesList);

		NameCombo.setVisible(true); // select Name

	}

	/**
	 * select the facility name and set the items below its threshold
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void selectName(ActionEvent event) {
		itemQuantityText.setVisible(false);
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		ArrayList<String> items = new ArrayList<>();
		ItemCombo.getItems().clear(); // clear the ItemCombo comoboBox, because we will change it
		ItemCombo.setVisible(true);
		// rearrange the ItemCombo comboBox
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(2).equals(NameCombo.getValue())) {
				if (!items.contains(curr.get(0)))
					// make sure the item is not in the executive order list
					if (!productsOfFacility.containsKey(curr.get(0) + "-" + NameCombo.getValue()))
						items.add(curr.get(0));
			}
		}

		ObservableList<String> itemsList = FXCollections.observableArrayList(items);

		ItemCombo.getItems().addAll(itemsList);
	}

	/**
	 * select an item from the comboBox, this item will not appear in the comboBox
	 * after selection
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void selectItem(ActionEvent event) {
		if (ItemCombo.getValue() == null)
			itemQuantityText.setVisible(false);
		itemQuantityText.setVisible(true);
		errorMessageText.setVisible(false);
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			// rearrange the ItemCombo
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(2).equals(NameCombo.getValue()) && curr.get(4).equals(LocationCombo.getValue())
					&& curr.get(0).equals(ItemCombo.getValue())) {
				// display current quantity of the selected item
				itemQuantityText.setText(String.format("Item Quantity: %s", curr.get(1)));
			}
		}

	}

	/**
	 * method that triggers when the "Back" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void back(ActionEvent event) {
		// goes back to the Area Manager interface window
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
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
			case "#REFILL_GET_PRODUCTS":
				productsBelowThreshold = new ArrayList<>();
				if (serverResponse.Responsedata.size() != 0) {
					existsProduct = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						ArrayList<String> toProduct = new ArrayList<>();
						String ProductName = (String) values[0];
						toProduct.add(ProductName);
						Integer ProductAmount = (Integer) values[1];
						String ProductAmountstr = String.valueOf(ProductAmount);
						toProduct.add(ProductAmountstr);
						String FacilityName = (String) values[2];
						toProduct.add(FacilityName);
						Integer FacilityThresholdLevel = (Integer) values[3];
						String FacilityThresholdLevelstr = String.valueOf(FacilityThresholdLevel);
						toProduct.add(FacilityThresholdLevelstr);
						String FacilityLocation = (String) values[4];
						toProduct.add(FacilityLocation);
						Integer FacilityID = (Integer) values[5];
						String FacilityIDstr = String.valueOf(FacilityID);
						toProduct.add(FacilityIDstr);
						productsBelowThreshold.add(toProduct);
					}
				}
				break;

			case "#REFILL_GET_THRESHOLD":
				if (serverResponse.Responsedata.size() != 0) {
					existsThreshold = true;
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);
					tempThreshold = (Integer) values[4];

				}
			case "#REFILL_PUT_TO_EXEC_ORDER":
				break;
			case "#REFILL_CHECK_EXEC_ORDER_ITEM":
				if (serverResponse.Responsedata.size() != 0)
					itemExistsInOrderTable = true;
				break;
			}
		}
	}

	/**
	 * Initialize the controller, so that all of the initial settings will be as we
	 * wish. This method actives as the controller activates
	 * 
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		itemQuantityText.setVisible(false);
		ItemCombo.setVisible(false);
		ClientUI.clientController.setController(this);

		userAreaName = ClientUI.clientController.getUser().getArea();

		facilityNameText.setText(String.format("%s Area", userAreaName));
		// initially the query will execute based on the employee that tries to use it,
		// but as the ceo will watch reports,
		// the else statement will activate, but this is a preparation to the second
		// phase, with this set it will
		// be easier to implement the CEO's part
		if (userAreaName.equals("All")) {
			facilityNameText.setText("All Areas");
			RequestObjectClient getItemsBelowThreshold = new RequestObjectClient("#REFILL_GET_PRODUCTS",
					String.format(""), "*");//this is unused
			ClientUI.clientController.accept(getItemsBelowThreshold);
		} else {
			RequestObjectClient getItemsBelowThreshold = new RequestObjectClient("#REFILL_GET_PRODUCTS",
					String.format("'%s'#", userAreaName), "*");
			ClientUI.clientController.accept(getItemsBelowThreshold);
		}
		arrayLocation = new ArrayList<>();
		arrayName = new ArrayList<>();
		items = new ArrayList<>();
		// sets the comboBoxes
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (!arrayLocation.contains(curr.get(4)))
				arrayLocation.add(curr.get(4));
			if (!arrayName.contains(curr.get(2)))
				arrayName.add(curr.get(2));
			items.add(curr.get(0));
		}

		LocationList = FXCollections.observableArrayList(arrayLocation);
		NameList = FXCollections.observableArrayList(arrayName);
		ItemsList = FXCollections.observableArrayList(items);

		LocationCombo.setItems(LocationList);
		NameCombo.setItems(NameList);
		ItemCombo.setItems(ItemsList);
		LocationCombo.setVisible(true);
		NameCombo.setVisible(true);
	}

}
