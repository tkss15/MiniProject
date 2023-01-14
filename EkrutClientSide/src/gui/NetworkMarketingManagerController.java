package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class NetworkMarketingManagerController implements Initializable, IController {

	
	/**
	 * 
	 * @author galmu
	 * inner class for row representation in the table view.
	 * this class saves information about sales such as area,type,startDate,startTime,endDate,endTime and if the sale is active or not.
	 * it has a constructor which initialises all the data to be saved for a sale.
	 * it also has getters and setters for all instance variables.
	 * 
	 */
	public class SaleRow {
		private String area, type, startDate, startTime, endDate, endTime;
		boolean isActive;

		public SaleRow(String area, String type, String startDate, String startTime, String endDate, String endTime,
				boolean isActive, String productName) {
			this.area = area;
			this.type = type;
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			this.isActive = isActive;
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

	private LocalDate startDate, endDate;

	/**
	 * boolean variable which indicates that the user tries to save an already existing sale.
	 */
	private boolean rejectionFalg = false;

	/**
	 * itemList saves all the products which exists in a given area. (The area is selected in the AreaCombo by the user).
	 */
	private ArrayList<Product> itemList;

	@FXML
	private ComboBox<String> AreaCombo;

	@FXML
	private Button CloseButton;

	@FXML
	private Button Logout;

	@FXML
	private TableColumn<SaleRow, String> areaColumn;

	@FXML
	private DatePicker datePickerEndDate;

	@FXML
	private DatePicker datePickerStartDate;

	@FXML
	private Label errorLabelEndHour;

	@FXML
	private Label errorLabelEndingDate;

	@FXML
	private Label errorLabelSave;

	@FXML
	private Label errorLabelStartHour;

	@FXML
	private Label errorLabelStartingDate;

	@FXML
	private TextField saleEndHour;

	@FXML
	private TextField saleStartHour;

	@FXML
	private TableColumn<SaleRow, String> saleHoursColumn;

	@FXML
	private TableColumn<SaleRow, String> saleTypeColumn;

	@FXML
	private TableView<SaleRow> salesTable;

	@FXML
	private ComboBox<String> salesTypesCombo;

	@FXML
	private Button saveBTN;

	@FXML
	private HBox textBranch;

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
	private ComboBox<Product> selectItemCombo;

	
	/**
	 * this method is invoked when the user closes the window (when pressing on the X in the top right corner of the screen)
	 * closeWindow closes the current window and logs out from the current account.
	 * @param event
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		logOut(event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * @author galmu
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logOut(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#",ClientUI.clientController.getUser().getUserName()),"PUT"); // DONE
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	@FXML
	void saveSales(ActionEvent event) {

		if (!checkingUserData()) { 
			return;
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("DD-MM-YYYY");
		String formatedStartDate = startDate.format(format);
		String formatedEndDate = startDate.format(format);
		
		RequestObjectClient salesRequest = new RequestObjectClient("#CREATE_NEW_SALE", String.format( // DONE
				"%s#%s#%s#%s#%s#%s#%d#",
				AreaCombo.getValue(), salesTypesCombo.getValue(), formatedStartDate, saleStartHour.getText(),
				formatedEndDate, saleEndHour.getText(), selectItemCombo.getValue().getProductCode()), "POST");
		ClientUI.clientController.accept(salesRequest);
		

		if (!rejectionFalg) {

			Alert info = new Alert(AlertType.INFORMATION);
			info.setContentText("New Sale added to sales table successfully!");
			setAlertIcon(info);
			info.showAndWait();
		} else {
			Alert info = new Alert(AlertType.ERROR);
			setAlertIcon(info);
			info.setContentText("This sale Already exists.");
			info.showAndWait();
		}
		rejectionFalg = false;
	}

	private boolean checkingUserData() {
		
		if (AreaCombo.getValue() == null || salesTypesCombo.getValue() == null) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You need to enter area and sale type!");
			error.showAndWait();
			return false;
		}

		if (selectItemCombo.getValue() == null) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You need to choose Item for sale!");
			error.showAndWait();
			return false;
		}

		if (!isCorrect(saleStartHour.getText())) {
			errorLabelStartHour.setVisible(true);
			return false;
		}
		errorLabelStartHour.setVisible(false);
		if (!isCorrect(saleEndHour.getText())) {
			errorLabelEndHour.setVisible(true);
			return false;
		}
		errorLabelEndHour.setVisible(false);

		startDate = datePickerStartDate.getValue();
		endDate = datePickerEndDate.getValue();

		if (startDate == null) {
			errorLabelStartingDate.setVisible(true);
			return false;
		}
		errorLabelStartingDate.setVisible(false);
		if (endDate == null) {
			errorLabelEndingDate.setVisible(true);
			return false;
		}
		errorLabelEndingDate.setVisible(false);

		if(startDate.isBefore(LocalDate.now())) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You can not enter starting date earlier than today's date!");
			error.showAndWait();
			return false;
		}
		
		if (startDate.isAfter(endDate)) {
			Alert error = new Alert(AlertType.ERROR);
			setAlertIcon(error);
			error.setContentText("You can not enter starting date earlier than ending date");
			error.showAndWait();
			return false;
		}
		if (startDate.equals(endDate)) {
			String startHStr = saleStartHour.getText().substring(0, 2);
			String endHStr = saleEndHour.getText().substring(0, 2);

			int startH = Integer.parseInt(startHStr);
			int endH = Integer.parseInt(endHStr);

			if (startH > endH) {
				Alert error = new Alert(AlertType.ERROR);
				setAlertIcon(error);
				error.setContentText("You can not enter enter starting hour which is later than the ending hour");
				error.showAndWait();
				return false;
			} else if (startH == endH) {

				int startM = Integer.parseInt(saleStartHour.getText().substring(3, 5));
				int endM = Integer.parseInt(saleEndHour.getText().substring(3, 5));

				if (startM == endM) {
					Alert error = new Alert(AlertType.ERROR);
					setAlertIcon(error);
					error.setContentText("You can not enter same hours and same dates");
					error.showAndWait();
					return false;
				} else if (startM > endM) {
					Alert error = new Alert(AlertType.ERROR);
					setAlertIcon(error);
					error.setContentText("You can not enter enter starting hour which is later than the ending hour");
					error.showAndWait();
					return false;
				}
			}
		}
		return true;
	}

	@FXML
	void setEndingDate(ActionEvent event) {
		endDate = datePickerEndDate.getValue();
	}

	@FXML
	void setStartingDate(ActionEvent event) {
		startDate = datePickerStartDate.getValue();
	}

	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_ALL_PRODUCTS_IN_AREA":
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
						itemList.add(p);
						i++;
					}

				}
				break;
			case "#CREATE_NEW_SALE":
				if (serverResponse.Responsedata.size() == 0) {
					rejectionFalg = true;
				}
				break;
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ClientUI.clientController.setController(this);
		errorLabelEndHour.setVisible(false);
		errorLabelEndingDate.setVisible(false);
		errorLabelSave.setVisible(false);
		errorLabelStartHour.setVisible(false);
		errorLabelStartingDate.setVisible(false);
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		itemList = new ArrayList<>();

		ObservableList<String> areas = FXCollections.observableArrayList("North", "South", "UAE");
		AreaCombo.setItems(areas);

		ObservableList<String> salesTypes = FXCollections.observableArrayList("1 + 1", "10% discount",
				"20% discount","30% discount","40% discount","50% discount","60% discount","70% discount","Second Item In Half Price");
		salesTypesCombo.setItems(salesTypes);

		selectItemCombo.setDisable(true);
		
		AreaCombo.setOnAction((e) ->{
			setItemsInCombo();
			selectItemCombo.setDisable(false);
		});
		
	}

	private void setItemsInCombo() {
		
		itemList.clear();
		RequestObjectClient itemsRequest = new RequestObjectClient("#GET_ALL_PRODUCTS_IN_AREA",
				String.format("%s#",AreaCombo.getValue()),
				"*");
		ClientUI.clientController.accept(itemsRequest);

		ObservableList<Product> products = FXCollections.observableArrayList(itemList);
		selectItemCombo.setItems(products);
	}

	private boolean isCorrect(String userInput) {
		String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
		Pattern p = Pattern.compile(regex);

		if (userInput == null) {
			return false;
		}
		Matcher m = p.matcher(userInput);

		return m.matches();
	}

	@FXML
	void showSales(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/NetworkEmployeeInterface.fxml",event);
	}
	private void setAlertIcon(Alert confirm) {
		ImageView icon = new ImageView(
				this.getClass().getResource("..\\gui\\pictures\\LogoEKRUT.png").toString());
		icon.setFitWidth(80);
		icon.setFitHeight(60);
		confirm.setGraphic(icon);
	}
}
