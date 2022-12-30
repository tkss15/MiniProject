package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class monthlyReportsController implements Initializable, IController {

	@FXML
	private Button CloseButton;

	@FXML
	private Text errorMessage;

	@FXML
	private ImageView EKrutLogo;

	@FXML
	private Button watchReportButton;

	@FXML
	private ComboBox<String> selectYear;

	@FXML
	private ComboBox<String> selectMonth;

	@FXML
	private ComboBox<String> selectType;

	@FXML
	private ImageView ReportLogo;

	@FXML
	private Button backButton;

	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/AreaManagerInterface.fxml");
	}

	@FXML
	void closeWindow(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void watchReport(ActionEvent event) {

	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub

	}

	@FXML
	void selectMonth(ActionEvent event) {
		String selectedMonth = selectMonth.getValue();
	}

	@FXML
	void selectType(ActionEvent event) {
		String selectedYear = selectYear.getValue();
	}

	@FXML
	void selectYear(ActionEvent event) {
		String selectedType = selectType.getValue();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		errorMessage.setVisible(false);

		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

		ArrayList<String> months = new ArrayList<>();
		for (int j = 0; j < 12; j++) {
		    months.add(monthNames[j]);
		}
		ArrayList<String> years = new ArrayList<>();
		for (int i = 2010; i < 2024; i++)
			years.add("" + i);

		ArrayList<String> types = new ArrayList<>();
		types.add("Orders");
		types.add("Supply");
		types.add("Costumers");

		ObservableList<String> monthsList = FXCollections.observableArrayList(months);
		selectMonth.setItems(monthsList);

		ObservableList<String> yearsList = FXCollections.observableArrayList(years);
		selectYear.setItems(yearsList);

		ObservableList<String> typesList = FXCollections.observableArrayList(types);
		selectType.setItems(typesList);
		}	
}
