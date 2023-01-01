package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Delivery;
import Entity.Delivery.DeliveryStatus;
import Entity.Employee;
import client.ClientUI;
import Entity.DeliveryRow;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class DeliveryOperatorInterfaceController implements IController, Initializable {

	private ArrayList<DeliveryRow> deliveryRows;
	@FXML
	private Button CloseButton;

	private ComboBox<DeliveryStatus> combo;

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
	private TableColumn<Delivery, Integer> orderCodeColumn;

	@FXML
	private TableColumn<Delivery, Boolean> customerApprovalStatusColumn;

	@FXML
	private TableColumn<Delivery, ComboBox<DeliveryStatus>> deliveryStatusColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);

		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());

		Employee curr = (Employee) ClientUI.clientController.getUser();
		branch.setText(curr.getBranch());

		deliveryRows = new ArrayList<>();

		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<Delivery, Integer>("orderCode"));
		customerApprovalStatusColumn
				.setCellValueFactory(new PropertyValueFactory<Delivery, Boolean>("costumerApproval"));
		deliveryStatusColumn.setCellValueFactory(
				new PropertyValueFactory<Delivery, ComboBox<DeliveryStatus>>("deliveryStatusCombobox"));

		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_ORDERS_DELIVERY",
				"table=virtualorders#condition=HasDelivery=1#", "GET");

		// -
		// table=users#condition=userName=%s#values=userName=username&userPassword=password
		ClientUI.clientController.accept(deliveries);
		final ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(deliveryRows);
//		new DeliveryRow(0, false, DeliveryStatus.DONE, new ComboBox<DeliveryStatus>()),
//		new DeliveryRow(0, false, DeliveryStatus.DISPATCHED, new ComboBox<DeliveryStatus>()),
//		new DeliveryRow(0, true, DeliveryStatus.RECEIVED, new ComboBox<DeliveryStatus>()));

		this.DeliveryOrdersTable.setItems(deliveryInfo);
		this.DeliveryOrdersTable.refresh();
		// show all the deliveries in table.
		//
		/**
		 * TableView<Person> table = new TableView<>();
		 * 
		 * TableColumn<Person, String> comboColumn = new TableColumn<>("Combo Column");
		 * comboColumn.setCellFactory(ComboBoxTableCell.forTableColumn(options));
		 * comboColumn.setOnEditCommit(event -> { Person person = event.getRowValue();
		 * person.setComboOption(event.getNewValue()); });
		 * 
		 * table.getColumns().add(comboColumn);
		 * 
		 */
	}

//	private void setDeliveryStatusComboBox(ComboBox<DeliveryStatus> combo) {
//		combo.getItems().addAll(DeliveryStatus.valueOf("Dispatched"), DeliveryStatus.valueOf("Received"),
//				DeliveryStatus.valueOf("Done"));
//
////		deliveryStatusCombo.setEditable(true);
//	}

	@Override
	public void updatedata(Object data) {
		// DOES NOT ENTER HERE
		System.out.println("DeliveryOpeartorInterfaceController");
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#DELIVERY_OPERATOR_ORDERS_DELIVERY":
				Delivery[] delivery = new Delivery[serverResponse.Responsedata.size()]; // what if serverResponse is
																						// null?
				if (serverResponse.Responsedata.size() != 0) {
					int i = 0;
					while (i < serverResponse.Responsedata.size()) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);// Row 1
						delivery[i] = new Delivery();
						delivery[i].setOrderCode((Integer) values[0]);
						String deliveryStatus = (String) values[3];

						System.out.println(deliveryStatus);

						DeliveryStatus d = DeliveryStatus.valueOf(deliveryStatus);

						delivery[i].setDeliveryStatus(d);
						delivery[i].setCostumerApproval((Integer) values[4]);

						combo = new ComboBox<>();
						DeliveryRow deliveryRow = new DeliveryRow(delivery[i], combo);
						combo.setPromptText(deliveryStatus);
						deliveryRows.add(deliveryRow);
						i++;

					}

				}
				break;

			}
//			this.DeliveryOrdersTable.getItems()
//					.add(new DeliveryRow(100, false, DeliveryStatus.DONE, new ComboBox<DeliveryStatus>()));
//			this.DeliveryOrdersTable.refresh();
		}
		// WAY TO ADD MULTIPLE ROWS.
//		final ObservableList<Delivery> data = FXCollections.observableArrayList(
//			    new Person("Jacob", "Smith", "jacob.smith@example.com"),
//			    new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
//			    new Person("Ethan", "Williams", "ethan.williams@example.com"),
//			    new Person("Emma", "Jones", "emma.jones@example.com"),
//			    new Person("Michael", "Brown", "michael.brown@example.com")
//			);
	}

	@FXML
	void applyChangesToTable(ActionEvent event) {
		System.out.println(111);
	}

	@FXML
	public void closeWindow(ActionEvent e) {
		System.exit(0);
	}

	@FXML
	public void logOut(ActionEvent e) {
		closeWindow(e);
	}

}
