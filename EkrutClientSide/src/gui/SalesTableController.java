package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Product;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class SalesTableController implements Initializable, IController {

	/***
	 * inner class for the rows of the table.
	 * SaleRow class represents a row of Sale data. It contains all the details of a
	 * sale such as area, type, start date, start time, end date, end time, active
	 * status, and item name.
	 *
	 */
	public class SaleRow {
		private String area, type, startDate, startTime, endDate, endTime;
		boolean isActive;
		String itemName;
		CheckBox activate;

		/**
		 * Constructor that initialises all the fields with the given parameters
		 *
		 * @param area      The area of the sale
		 * @param type      The type of the sale
		 * @param startDate The start date of the sale
		 * @param startTime The start time of the sale
		 * @param endDate   The end date of the sale
		 * @param endTime   The end time of the sale
		 * @param isActive  The active status of the sale
		 * @param itemName  The item name of the sale
		 */
		public SaleRow(String area, String type, String startDate, String startTime, String endDate, String endTime,
				int isActive, String itemName) {
			this.area = area;
			this.type = type;
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			if (isActive == 1)
				this.isActive = true;
			else
				this.isActive = false;
			this.itemName = itemName;
		}

		/*getters and setters*/
		public CheckBox getActivate() {
			return activate;
		}

		public void setActivate(CheckBox activate) {
			this.activate = activate;
		}

		public String getItem() {
			return itemName;
		}

		public void setItem(String itemName) {
			this.itemName = itemName;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}

	private ArrayList<SaleRow> saleRows; /*array list which saves the sales which are presented in the table.*/

	private String employeeType; /*String which saves the employee type which is received from the query request.*/

	private ArrayList<Product> productsList; /*array list which saves all the existing products.*/

	private LinkedHashMap<SaleRow, Product> map; /*mapping of sales to the products which they are activated on.*/

	@FXML
	private Button deleteRowBTN;
	
	//the table of sales.
	@FXML
	private TableView<SaleRow> salesTable;

	@FXML
	private Button CloseButton;

	@FXML
	private Button backBTN;

	//the columns of the table.
	@FXML
	private TableColumn<SaleRow, String> EndingDateColumn;

	@FXML
	private TableColumn<SaleRow, String> EndingTimeColumn;

	@FXML
	private TableColumn<SaleRow, String> areaColumn;

	@FXML
	private TableColumn<SaleRow, String> saleTypeColumn;

	@FXML
	private TableColumn<SaleRow, String> startingDateColumn;

	@FXML
	private TableColumn<SaleRow, String> startingTimeColumn;

	@FXML
	private TableColumn<SaleRow, Integer> itemColumn;

	@FXML
	private TableColumn<SaleRow, CheckBox> activateColumn;
	@FXML
	private Text areaText;
	@FXML
	private Button refershBTN;
	@FXML
	private Button Logout;

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		if (employeeType.equals("NetworkMarketingManager")) {
			goBack(event);
			return;
		}
		ClientUI.clientController.UserDisconnected(true);
	}

	@Override
	/**
	 * saving all the data which is returned from the DB and relevant for the current controller.
	 * saving the sales in saleRows.
	 * saving the employee type.
	 * saving all existing products in product list.
	 * @param data
	 */
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_ALL_SALES":
				if (serverResponse.Responsedata.size() != 0) {
					saveSalesAndMappingsToProducts(serverResponse);
				}
				break;
			case "#GET_ALL_SALES_IN_AREA":
				if (serverResponse.Responsedata.size() != 0) {
					saveSalesAndMappingsToProducts(serverResponse);

				}
				break;
			case "#GET_EMPLOYEE_TYPE":
				//getting and saving the current logged in employee type.
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);
					employeeType = (String) values[0];
				}
				break;
			case "#GET_ALL_PRODUCTS":
				//getting all products from the DB and saving them in the products list.
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						int productCode = (int) values[0];
						String productName = (String) values[1];
						double productPrice = (double) values[2];
						String productDescription = (String) values[3];
						String productSrc = (String) values[4];

						Product p = new Product(productCode, productName, productDescription, productSrc, productPrice);
						productsList.add(p);
						i++;
					}

				}
				break;

			}
		}

	}

	/**
	 * saves the sales in the saleRows array list and for each sale saves it's mapping to a product.
	 * @param serverResponse an object containing the query output returned from the server.
	 */
	private void saveSalesAndMappingsToProducts(ResponseObject serverResponse) {
		int i = 0;
		while (i < serverResponse.Responsedata.size()) {
			Object[] values = (Object[]) serverResponse.Responsedata.get(i);
			String area = (String) values[0];
			String type = (String) values[1];
			String startDate = (String) values[2];
			String startTime = (String) values[3];
			String endDate = (String) values[4];
			String endTime = (String) values[5];
			int isActive = (Integer) values[6];
			int itemCode = (int) values[7];

			//searching for a product in the product list and adding it to the saleRows and adding mapping of the sale to the product.
			for (Product p : productsList) {
				if (p.getProductCode() == itemCode) {
					//saving all of the sales attributes in a SaleRow object.
					SaleRow saleRow = new SaleRow(area, type, startDate, startTime, endDate, endTime,
							isActive, p.getProductName());
					saleRows.add(saleRow);
					map.putIfAbsent(saleRow, p);
					break;
				}
			}
			i++;
		}
	}


	@Override
	/**
	 * initialises the controller as it is loaded. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		//setting the static clientController to be this controller.
		ClientUI.clientController.setController(this);

		saleRows = new ArrayList<>();
		map = new LinkedHashMap<>();

		Logout.setVisible(false);
		backBTN.setVisible(false);

		productsList = new ArrayList<>();
		//request a query to get all existing products.
		
		RequestObjectClient itemsRequest = new RequestObjectClient("#GET_ALL_PRODUCTS", "", "GET"); 
		ClientUI.clientController.accept(itemsRequest);

		//request a query to get all the employee role type.
		RequestObjectClient employeeTypeRequest = new RequestObjectClient("#GET_EMPLOYEE_TYPE",
				String.format("%s#", ClientUI.clientController.getUser().getUserName()), "GET");
		ClientUI.clientController.accept(employeeTypeRequest);

		//initialising the sales table.
		initSalesTable();

		//initialising the column of the sales table.
		areaColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("area"));
		areaColumn.setResizable(false);

		saleTypeColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("type"));
		saleTypeColumn.setResizable(false);

		startingDateColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("startDate"));
		startingDateColumn.setResizable(false);

		startingTimeColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("startTime"));
		startingTimeColumn.setResizable(false);

		EndingDateColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("endDate"));
		EndingDateColumn.setResizable(false);

		EndingTimeColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, String>("endTime"));
		EndingTimeColumn.setResizable(false);

		itemColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, Integer>("Item"));
		itemColumn.setResizable(false);

		activateColumn.setCellValueFactory(new PropertyValueFactory<SaleRow, CheckBox>("activate"));
		activateColumn.setResizable(false);

		//inserting all the deliveryRows into the table via ObservableList.
		ObservableList<SaleRow> deliveryInfo = FXCollections.observableArrayList(saleRows);
		salesTable.setItems(deliveryInfo);
	}

	@FXML
	/*
	 * invoked when the users clicks on the refresh button.
	 * clears all the table from previous data, and initialises the table from scratch.
	 */
	void refresh(ActionEvent event) {
		saleRows.clear();
		initSalesTable();
		ObservableList<SaleRow> deliveryInfo = FXCollections.observableArrayList(saleRows);
		salesTable.setItems(deliveryInfo);
	}

	/**
	 * the table is constructed differently depending on the employee type:
	 * the manager can only view all the sales but cannot activate or delete them.
	 * the sales employee can activate sales and delete them. 
	 */
	private void initSalesTable() {
		if (employeeType.equals("NetworkMarketingManager")) {
			//setting all the GUI elements which are relevant for the network marketing manager to be visible, and hiding other not necessary elements.
			areaText.setVisible(false);
			backBTN.setVisible(true);
			deleteRowBTN.setVisible(false);
			
			//request a query to get all existing sales in all areas.
			RequestObjectClient salesRequestForManager = new RequestObjectClient("#GET_ALL_SALES", "", "GET"); 
			ClientUI.clientController.accept(salesRequestForManager);
			
			//set all the check boxes disabled so that the manager cannot activate or cancel sales.
			activateColumn.setEditable(false);
			for (SaleRow saleRow : saleRows) {
				CheckBox checkBox = new CheckBox();
				checkBox.setId("box");
				checkBox.setDisable(true);
				checkBox.setSelected(saleRow.isActive);
				saleRow.setActivate(checkBox);
			}
		} else {
			Logout.setVisible(true);
			
			//request a query to get all existing sales from the current logged in user's area.
			RequestObjectClient salesRequest = new RequestObjectClient("#GET_ALL_SALES_IN_AREA", 
					String.format("%s#", ClientUI.clientController.getUser().getArea()), "GET");
			ClientUI.clientController.accept(salesRequest);
			
			//setting all the GUI elements which are relevant for the network marketing employee to be visible, and hiding other not necessary elements.
			areaText.setText("Area: " + ClientUI.clientController.getUser().getArea());
			areaColumn.setVisible(false);
			salesTable.setPrefWidth(1060);
			activateColumn.setPrefWidth(272);
			//set every sale with a check box which indicates the status of the sale
			for (SaleRow saleRow : saleRows) {
				CheckBox checkBox = new CheckBox();
				checkBox.setId("box");
				//set the selected property of the comb box according to the activation status of the sale.
				checkBox.setSelected(saleRow.isActive);
				
				//on action invoked when the user selects or removes his selection of the check box.
				checkBox.setOnAction((e) -> {
					
					//request a query to update the sale's activation status according to the check box selected property.
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS", 
							String.format("%b#%s#%s#%s#%s#%s#%s#%d#", checkBox.isSelected(),
									ClientUI.clientController.getUser().getArea(), saleRow.getType(),
									saleRow.getStartDate(), saleRow.getStartTime(), saleRow.getEndDate(),
									saleRow.getEndTime(), map.get(saleRow).getProductCode()),
							"PUT");
					ClientUI.clientController.accept(activateRequest);

					//showing an alert of information the employee's action of activating or cancelling the chosen sale.
					Alert info = new Alert(AlertType.INFORMATION);
					setAlertIcon(info);
					if (checkBox.isSelected()) {
						info.setContentText("Chosen sale has been activated!");
					} else
						info.setContentText("Chosen sale has been cancelled!");
					info.showAndWait();
				});
				//setting the sale row with the checkBox.
				saleRow.setActivate(checkBox);
			}
		}
	}

	@FXML
	/**
	 * invoked when the user clicks on the delete button(after clicking on a some row from the table).
	 * deletes the sale both from the table and from the DB.
	 * @param event
	 */
	void deleteRow(ActionEvent event) {
		SaleRow selectedItem = salesTable.getSelectionModel().getSelectedItem();
		//if no row was clicked, an error pop up will be shown.
		if (selectedItem == null) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You need to choose a sale!");
			error.showAndWait();
			return;
		}
		//getting the product which is associated with the sale.
		Product product = map.get(selectedItem);

		//showing a confirmation alert pop up, for the user to confirm the deletion of the sale.
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		setAlertIcon(confirm);
		confirm.setContentText("Are you sure you want to delete this sale?");
		Optional<ButtonType> result = confirm.showAndWait();

		if (result.get() == ButtonType.OK) {

			//request a query to delete the sale from the sales table.
			RequestObjectClient salesRequest = new RequestObjectClient("#DELETE_SALE", String.format( 
					"%s#%s#%s#%s#%s#%s#%d#", ClientUI.clientController.getUser().getArea(), selectedItem.getType(),
					selectedItem.getStartDate(), selectedItem.getStartTime(), selectedItem.getEndDate(),
					selectedItem.getEndTime(), product.getProductCode()), "DELETE");
			ClientUI.clientController.accept(salesRequest);

			//after deletion from the DB , the sale is deleted from the table.
			salesTable.getItems().remove(selectedItem);
		}
	}

	/**
	 * setting the icon of the Alert to be the logo of the company.
	 * @param confirm
	 */
	private void setAlertIcon(Alert confirm) {
		ImageView icon = new ImageView(this.getClass().getResource("..\\gui\\pictures\\LogoEKRUT.png").toString());
		icon.setFitWidth(80);
		icon.setFitHeight(60);
		confirm.setGraphic(icon);
	}

	/**
	 * method that triggers when the Logout button has been pressed this method
	 * sends a query to which sets the user status to be Offline and thus log's him
	 * out from his account.
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logOut(ActionEvent event) {
		if (employeeType.equals("NetworkMarketingManager")) {
			goBack(event);
			return;
		}
		
		ClientUI.clientController.UserDisconnected(false);
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	@FXML
	/**
	 * method that triggers when the "Back" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	void goBack(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/NetworkMarketingManagerInterface.fxml", event);
	}

}
