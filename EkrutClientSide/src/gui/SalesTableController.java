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

	public class SaleRow {
		private String area, type, startDate, startTime, endDate, endTime;
		boolean isActive;
		String itemName;
		CheckBox activate;

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

	private ArrayList<SaleRow> saleRows;

	private String employeeType;

	private ArrayList<Product> productsList;

	private LinkedHashMap<SaleRow, Product> map;

	@FXML
	private Button deleteRowBTN;
	@FXML
	private TableView<SaleRow> salesTable;

	@FXML
	private Button CloseButton;

	@FXML
	private Button backBTN;

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
	 * @author galmu
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		if (employeeType.equals("NetworkMarketingManager")) {
			goBack(event);
			return;
		}

		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#",ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}


	@Override
	public void updatedata(Object data) {
		System.out.println(111111111);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_ALL_SALES":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
//						private String area, type, startDate, startTime, endDate, endTime;
						String area = (String) values[0];
						String type = (String) values[1];
						String startDate = (String) values[2];
						String startTime = (String) values[3];
						String endDate = (String) values[4];
						String endTime = (String) values[5];
						int isActive = (Integer) values[6];
						int itemCode = (int) values[7];

						for (Product p : productsList) {
							if (p.getProductCode() == itemCode) {
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
				break;
			case "#GET_ALL_SALES_IN_AREA":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
//						private String area, type, startDate, startTime, endDate, endTime;
						String area = (String) values[0];
						String type = (String) values[1];
						String startDate = (String) values[2];
						String startTime = (String) values[3];
						String endDate = (String) values[4];
						String endTime = (String) values[5];
						int isActive = (Integer) values[6];
						int itemCode = (int) values[7];

						for (Product p : productsList) {
							if (p.getProductCode() == itemCode) {
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
				break;
			case "#GET_EMPLOYEE_TYPE":
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);
					employeeType = (String) values[0];
				}
				break;
			case "#GET_ALL_PRODUCTS":
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
//						private String area, type, startDate, startTime, endDate, endTime;
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

//	private void getItemsToList() {
//		itemList = new ArrayList<>();
//
//		RequestObjectClient itemsRequest = new RequestObjectClient("#GET_ITEMS", "table=products", "GET");
//		ClientUI.clientController.accept(itemsRequest);
//
//	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);

		saleRows = new ArrayList<>();
		map = new LinkedHashMap<>();
		
		Logout.setVisible(false);
		backBTN.setVisible(false);

		productsList = new ArrayList<>();
		RequestObjectClient itemsRequest = new RequestObjectClient("#GET_ALL_PRODUCTS", "", "GET"); // DONE
		ClientUI.clientController.accept(itemsRequest);

//		select employees.Employeerole
//		from employees
//		where employees.userName = 'nen'

//		 * 		- table=users#condition=userName=%s#values=userName=username&userPassword=password
//				 * 		- SELECT userPassword, userName FROM users WHERE userName = "tkss15"

//		getItemsToList();

		RequestObjectClient employeeTypeRequest = new RequestObjectClient("#GET_EMPLOYEE_TYPE", // DONE
				String.format("%s#",ClientUI.clientController.getUser().getUserName()),"GET");
		ClientUI.clientController.accept(employeeTypeRequest);

		initSalesTable();

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

		ObservableList<SaleRow> deliveryInfo = FXCollections.observableArrayList(saleRows);
		salesTable.setItems(deliveryInfo);
	}

	@FXML
	void refresh(ActionEvent event) {
		saleRows.clear();
		initSalesTable();
		ObservableList<SaleRow> deliveryInfo = FXCollections.observableArrayList(saleRows);
		salesTable.setItems(deliveryInfo);
	}

	private void initSalesTable() {
		if (employeeType.equals("NetworkMarketingManager")) {
			areaText.setVisible(false);
			backBTN.setVisible(true);
			deleteRowBTN.setVisible(false);
			RequestObjectClient salesRequestForManager = new RequestObjectClient("#GET_ALL_SALES", "", "GET"); //DONE
			ClientUI.clientController.accept(salesRequestForManager);
			activateColumn.setEditable(false);
			for (SaleRow saleRow : saleRows) {
				CheckBox checkBox = new CheckBox();
				checkBox.setDisable(true);
				checkBox.setSelected(saleRow.isActive);
				saleRow.setActivate(checkBox);
			}
		} else {
			Logout.setVisible(true);
			RequestObjectClient salesRequest = new RequestObjectClient("#GET_ALL_SALES_IN_AREA", //DONE
					String.format("%s#", ClientUI.clientController.getUser().getArea()),"GET");
			ClientUI.clientController.accept(salesRequest);
			areaText.setText("Area: " + ClientUI.clientController.getUser().getArea());
			areaColumn.setVisible(false);
			salesTable.setPrefWidth(1060);
			activateColumn.setPrefWidth(272); /// do not change!!
			for (SaleRow saleRow : saleRows) {
				CheckBox checkBox = new CheckBox();
				checkBox.setSelected(saleRow.isActive);
				checkBox.setOnAction((e) -> {
					System.out.println(checkBox.isSelected());
					RequestObjectClient activateRequest = new RequestObjectClient("#UPDATE_ACTIVE_STATUS", //DONE
							String.format(
									"%b#%s#%s#%s#%s#%s#%s#%d#",
									checkBox.isSelected(), ClientUI.clientController.getUser().getArea(),
									saleRow.getType(), saleRow.getStartDate(), saleRow.getStartTime(),
									saleRow.getEndDate(), saleRow.getEndTime(), map.get(saleRow).getProductCode()),
							"PUT");
					ClientUI.clientController.accept(activateRequest);

					Alert info = new Alert(AlertType.INFORMATION);
					setAlertIcon(info);
					if (checkBox.isSelected()) {
						info.setContentText("Chosen sale has been activated!");
					} else
						info.setContentText("Chosen sale has been cancelled!");
					info.showAndWait();
				});
				saleRow.setActivate(checkBox);
			}
		}
	}

	@FXML
	void deleteRow(ActionEvent event) {
		SaleRow selectedItem = salesTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You need to choose a sale!");
			error.showAndWait();
			return;
		}
		Product product = map.get(selectedItem);

		Alert confirm = new Alert(AlertType.CONFIRMATION);
		setAlertIcon(confirm);
		confirm.setContentText("Are you sure you want to delete this sale?");
		Optional<ButtonType> result = confirm.showAndWait();

//    	 * Delete - DELETE - 
//    	 * 		- table=subscriber#values=id=3
//    	 * 		- DELETE FROM subscriber WHERE id=3
		
		if (result.get() == ButtonType.OK) {

			RequestObjectClient salesRequest = new RequestObjectClient("#DELETE_SALE", String.format( // DONE
					"%s#%s#%s#%s#%s#%s#%d#",
					ClientUI.clientController.getUser().getArea(), selectedItem.getType(), selectedItem.getStartDate(),
					selectedItem.getStartTime(), selectedItem.getEndDate(), selectedItem.getEndTime(),
					product.getProductCode()), "DELETE");
			ClientUI.clientController.accept(salesRequest);

			salesTable.getItems().remove(selectedItem);
		}
	}

	private void setAlertIcon(Alert confirm) {
		ImageView icon = new ImageView(this.getClass().getResource("..\\gui\\pictures\\LogoEKRUT.png").toString());
		icon.setFitWidth(80);
		icon.setFitHeight(60);
		confirm.setGraphic(icon);
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
		closeWindow(event);
	}

	@FXML
	void goBack(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/NetworkMarketingManagerInterface.fxml", event);
	}

}
