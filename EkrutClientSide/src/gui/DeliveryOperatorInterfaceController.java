package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.Client;

import Entity.Delivery;
import Entity.Delivery.DeliveryStatus;
import client.ClientUI;
import common.DeliveryRow;
import common.IController;
import common.RequestObjectClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeliveryOperatorInterfaceController implements IController, Initializable {
	
//	private Delivery delivery;
	@FXML
	private Button CloseButton;

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
		
		updatedata(null);
		orderCodeColumn.setCellValueFactory(new PropertyValueFactory<Delivery, Integer>("orderCode"));
		customerApprovalStatusColumn
				.setCellValueFactory(new PropertyValueFactory<Delivery, Boolean>("costumerApproval"));
		deliveryStatusColumn.setCellValueFactory(
				new PropertyValueFactory<Delivery, ComboBox<DeliveryStatus>>("deliveryStatusCombobox"));
		
		RequestObjectClient deliveries = new RequestObjectClient("#DELIVERY_OPERATOR_ORDERS_DELIVERY"
				,"table=virtualorders#","GET");
		
//		ClientUI.clientController.accept(deliveries);
		
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

//	private void setDeliveryStatusComboBox() {
//		deliveryStatusCombo.getItems().addAll(DeliveryStatus.valueOf("DISPATCHED"), DeliveryStatus.valueOf("RECEIVED"),
//				DeliveryStatus.valueOf("DONE"));
//		
////		deliveryStatusCombo.setEditable(true);
//	}

	@Override
	public void updatedata(Object data) {
		if (data instanceof Delivery) {
			final ObservableList<DeliveryRow> deliveryInfo = FXCollections.observableArrayList(
					new DeliveryRow(0, false, DeliveryStatus.DONE, new ComboBox<DeliveryStatus>()),
					new DeliveryRow(0, false, DeliveryStatus.DISPATCHED, new ComboBox<DeliveryStatus>()),
					new DeliveryRow(0, true, DeliveryStatus.RECEIVED, new ComboBox<DeliveryStatus>()));

			this.DeliveryOrdersTable.setItems(deliveryInfo);
			this.DeliveryOrdersTable.refresh();
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
	public void closeWindow(ActionEvent e) {
		System.exit(0);
	}

}
