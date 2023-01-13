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

	public class DeliveryRow {
		private int orderCode;

		private boolean customerApproval;
		private DeliveryStatus deliveryStatus;
		private ComboBox<DeliveryStatus> deliveryStatusCombobox;

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

		public void setDeliveryStatusCombobox(ComboBox<DeliveryStatus> deliveryStatusCombobox,String curr) {
			//curr must be in right syntax.
			this.deliveryStatusCombobox = deliveryStatusCombobox;
			this.deliveryStatusCombobox.getItems().addAll(DeliveryStatus.valueOf(curr),DeliveryStatus.valueOf("Done"));
			deliveryStatusCombobox.setValue(deliveryStatus);
		}

	}

	private String userAreaStr;
	private String userRoleStr;
	private ArrayList<DeliveryRow> deliveryRows;

	@FXML
	private Button CloseButton;

	private ComboBox<DeliveryStatus> combo;

	@FXML
	private Text DeliveryOperatorAreaText;

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

	@FXML
	private TableView<DeliveryRow> DeliveryOrdersTable;

	@FXML
	private TableColumn<DeliveryRow, Integer> orderCodeColumn;

	@FXML
	private TableColumn<DeliveryRow, Boolean> customerApprovalStatusColumn;

	@FXML
	private TableColumn<DeliveryRow, ComboBox<DeliveryStatus>> deliveryStatusColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);

		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		RequestObjectClient userArea = new RequestObjectClient("#USER_AREA_DELOP", String.format( //DONE
				"%s#",ClientUI.clientController.getUser().getUserName()), "*");
		ClientUI.clientController.accept(userArea);
		System.out.println(userRoleStr);

		DeliveryOperatorAreaText.setText("Delivery Operator: " + userAreaStr);

//		Employee curr = (Employee) ClientUI.clientController.getUser();
		deliveryRows = new ArrayList<>();

		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_ORDERS_DELIVERY", //DONE
				String.format("%s#",userAreaStr),"*");
		ClientUI.clientController.accept(deliveries);

		for (DeliveryRow d : deliveryRows) {
			combo = new ComboBox<>();
			combo.setOnAction((e) -> {
				d.setDeliveryStatus(d.getDeliveryStatusCombobox().getValue());
				System.out.println(d.getDeliveryStatusCombobox().getValue().toString());

			});
			if (!d.isCustomerApproval()) {
				combo.setDisable(true);
				combo.setOpacity(100);
			}
			combo.setPromptText(d.deliveryStatus.toString());
			d.setDeliveryStatusCombobox(combo,d.deliveryStatus.toString());
		}

		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Integer>("orderCode"));
		orderCodeColumn.setResizable(false);
		customerApprovalStatusColumn
				.setCellValueFactory(new PropertyValueFactory<DeliveryRow, Boolean>("customerApproval"));
		customerApprovalStatusColumn.setResizable(false);
		deliveryStatusColumn.setCellValueFactory(
				new PropertyValueFactory<DeliveryRow, ComboBox<DeliveryStatus>>("deliveryStatusCombobox"));
		deliveryStatusColumn.setResizable(false);

		final ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);

		DeliveryOrdersTable.setItems(deliveryInfo);
		DeliveryOrdersTable.refresh();

	}

	@Override
	public void updatedata(Object data) {
		System.out.println("DeliveryOpeartorInterfaceController");
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
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
						System.out.println("KUKU");
						deliveryRows.add(deliveryRow);
						i++;

					}

				}
				break;
			case "#USER_AREA_DELOP":
				if (serverResponse.Responsedata.size() != 0) {
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);// Row 1
					userAreaStr = (String) values[0];
				}
			}
		}
	}

	@FXML
	void applyChangesToTable(ActionEvent event) {
		for (DeliveryRow d : deliveryRows) {
			if (!d.getDeliveryStatusCombobox().isDisable()) {
				String deliveryStatus = d.getDeliveryStatusCombobox().getValue().toString();
				RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_SET_DELIVERY_STATUS",
						String.format(
								"%s#%s#",
								Integer.toString(d.getOrderCode()), deliveryStatus),
						"PUT");
				ClientUI.clientController.accept(deliveries);
			}
//			 * Update - PUT - 
//			 * 		- URL: table=subscriber#condition=id=4#values=creditcardnumber=3&subscribernumber=4
//			 * 		- UPDATE subscriber SET subscribernumber = 4, creditcardnumber = 3 WHERE id = 4;
		}

		final ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
		this.DeliveryOrdersTable.setItems(deliveryInfo);
		this.DeliveryOrdersTable.refresh();

		Alert confirmationMsg = new Alert(AlertType.INFORMATION);
		confirmationMsg.setContentText("Changes have been successfully saved.");

//		Image image = new Image("https://as1.ftcdn.net/v2/jpg/02/01/30/82/1000_F_201308263_ylhTkL69sCEDKWXlXu2S4rumX4JZqb4f.jpg");
//		ImageView imageView = new ImageView(image);
//		confirmationMsg.setGraphic(imageView);
		confirmationMsg.showAndWait();

	}

	@FXML
	public void closeWindow(ActionEvent e) {
		logOut(e);
	}

	@FXML
	public void logOut(ActionEvent e) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",  // DONE
					String.format("%s#",ClientUI.clientController.getUser().getUserName()),"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", e);
	}

}
