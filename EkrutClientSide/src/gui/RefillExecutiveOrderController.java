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
	private HashMap<String,String> productsOfFacility = new HashMap<>();
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
	void sendExecutiveOrder(ActionEvent event) {
		errorMessageText.setVisible(false);
		if(productsToRefill.isEmpty()) {
			errorMessageText.setText("No items to send!");
			errorMessageText.setVisible(true);
			return;
		}
			
		
		for (int i = 0; i < productsToRefill.size(); i++) {
			ProductToRefill curr = productsToRefill.get(i);
			RequestObjectClient CheckItemInExec = new RequestObjectClient("#CHECK_EXEC_ORDER_ITEM",
					String.format("table=executiveorders#condition=ProductName=%s&"
							+ "ProductQuantity=%s&Area=%s&FacilityLocation=%s&"
							+ "FacilityName=%s&FacilityThresholdLevel=%s",
							curr.getProductName(),curr.getProductQuantity(),
							curr.getArea(),curr.getFacilityLocation(),curr.getFacilityName(),
							curr.getFacilityThresholdLevel()),"GET");
			ClientUI.clientController.accept(CheckItemInExec);
			
			if(!itemExistsInOrderTable) {
				RequestObjectClient putToExecOrder = new RequestObjectClient("#PUT_TO_EXEC_ORDER",
						String.format("table=executiveorders#values=ProductName=%s&"
								+ "ProductQuantity=%s&Area=%s&FacilityLocation=%s&"
								+ "FacilityName=%s&FacilityThresholdLevel=%s",
								curr.getProductName(),curr.getProductQuantity(),
								curr.getArea(),curr.getFacilityLocation(),curr.getFacilityName(),
								curr.getFacilityThresholdLevel()),"POST");
				
				ClientUI.clientController.accept(putToExecOrder);
			}
			itemExistsInOrderTable = false;
			
		}
		productsToRefill.clear();
		Alert info = new Alert(AlertType.INFORMATION);
		info.setContentText("Executive order has been sent to the employee!");
		info.showAndWait();
		itemsListView.getItems().clear();itemsListView.refresh();
	}

	@FXML
	void addToExecutiveOrder(ActionEvent event) {
		errorMessageText.setVisible(false);
		if(ItemCombo.getValue() == null)
			return;
		ArrayList<String> items = new ArrayList<>();
		String toOrder = ItemCombo.getValue();
		String toOrderInList = toOrder + "-" +  NameCombo.getValue();
		itemsToListView.add(toOrderInList);
		itemsListView.setItems(itemsToListView);
		productsOfFacility.put(toOrderInList, NameCombo.getValue());
		String[] parts = itemQuantityText.getText().split(" ");
		ProductToRefill product = new ProductToRefill(toOrder, parts[2], ClientUI.clientController.getUser().getArea(),
				LocationCombo.getValue(), NameCombo.getValue(), null);

		productsToRefill.add(product);

		ItemsList.clear();
		for (int i = 0; i < productsToRefill.size(); i++) {
			ProductToRefill curr = productsToRefill.get(i);
			RequestObjectClient getFacilityThresholdLevel = new RequestObjectClient("#GET_THRESHOLD",
					String.format("table=facilities#condition=FacilityLocation=%s&FacilityName=%s",
							curr.getFacilityLocation(), curr.getFacilityName()),"GET");
			ClientUI.clientController.accept(getFacilityThresholdLevel);
			productsToRefill.get(i).setFacilityThresholdLevel(String.valueOf(tempThreshold));
			existsThreshold = false;
		}
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

	@FXML
	void selectLocation(ActionEvent event) {
		errorMessageText.setVisible(false);
		itemQuantityText.setVisible(false);
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		ArrayList<String> names = new ArrayList<>();
		ItemCombo.setVisible(false);
//		ItemCombo.getItems().clear();
		NameCombo.getItems().clear();

		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(4).equals(LocationCombo.getValue())) {
				if (!names.contains(curr.get(2)))
					names.add(curr.get(2));
			}
		}

		ObservableList<String> namesList = FXCollections.observableArrayList(names);

		NameCombo.getItems().addAll(namesList);

		NameCombo.setVisible(true); // select Name

	}

	@FXML
	void selectName(ActionEvent event) {
		itemQuantityText.setVisible(false);
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		ArrayList<String> items = new ArrayList<>();
		ItemCombo.getItems().clear();
		ItemCombo.setVisible(true);
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(2).equals(NameCombo.getValue())) {
				if (!items.contains(curr.get(0)))
					if(!productsOfFacility.containsKey(curr.get(0) + "-" + NameCombo.getValue()))
						items.add(curr.get(0));
			}
		}

		ObservableList<String> itemsList = FXCollections.observableArrayList(items);

		ItemCombo.getItems().addAll(itemsList);
	}

	@FXML
	void selectItem(ActionEvent event) {
		itemQuantityText.setVisible(true);
		errorMessageText.setVisible(false);
		for (int i = 0; i < productsBelowThreshold.size(); i++) {
			ArrayList<String> curr = productsBelowThreshold.get(i);
			if (curr.get(2).equals(NameCombo.getValue()) && curr.get(4).equals(LocationCombo.getValue())
					&& curr.get(0).equals(ItemCombo.getValue())) {
				itemQuantityText.setText(String.format("Item Quantity: %s", curr.get(1)));
			}
		}

	}

	@FXML
	void back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("table=users#condition=userName=%s#values=userOnline=\"Offline\"",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_PRODUCTS":
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

			case "#GET_THRESHOLD":
				if (serverResponse.Responsedata.size() != 0) {
					existsThreshold = true;
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);
					tempThreshold = (Integer) values[4];

				}
			case "#PUT_TO_EXEC_ORDER":
				break;
			case "#CHECK_EXEC_ORDER_ITEM":
				if (serverResponse.Responsedata.size() != 0)
					itemExistsInOrderTable = true;
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		itemQuantityText.setVisible(false);
		ItemCombo.setVisible(false);
		ClientUI.clientController.setController(this);

		userAreaName = ClientUI.clientController.getUser().getArea();

		facilityNameText.setText(String.format("%s Area", userAreaName));

		if (userAreaName.equals("All")) {
			facilityNameText.setText("All Areas");
			RequestObjectClient getItemsBelowThreshold = new RequestObjectClient("#GET_PRODUCTS", String.format(
					"SELECT products.ProductName, productsinfacility.ProductAmount, facilities.FacilityName, facilities.FacilityThreshholdLevel,facilities.FacilityLocation,facilities.FacilityID "
							+ "FROM  products " + "INNER JOIN facilities " + "INNER JOIN productsinfacility "
							+ "ON productsinfacility.ProductCode = products.ProductCode and productsinfacility.FacilityID = facilities.FacilityID "
							+ "where facilities.FacilityThreshholdLevel >= productsinfacility.ProductAmount "),
					"*");
			ClientUI.clientController.accept(getItemsBelowThreshold);
		} else {
			RequestObjectClient getItemsBelowThreshold = new RequestObjectClient("#GET_PRODUCTS", String.format(
					"SELECT products.ProductName, productsinfacility.ProductAmount, facilities.FacilityName, facilities.FacilityThreshholdLevel,facilities.FacilityLocation,facilities.FacilityID "
							+ "FROM  products " + "INNER JOIN facilities " + "INNER JOIN productsinfacility "
							+ "ON productsinfacility.ProductCode = products.ProductCode and productsinfacility.FacilityID = facilities.FacilityID "
							+ "where FacilityArea = '%s' and facilities.FacilityThreshholdLevel >= productsinfacility.ProductAmount",
					userAreaName), "*");
			ClientUI.clientController.accept(getItemsBelowThreshold);
		}
		arrayLocation = new ArrayList<>();
		arrayName = new ArrayList<>();
		items = new ArrayList<>();

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
