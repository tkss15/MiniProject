package gui;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TypeReportController implements Initializable, IController {
	boolean exists = false;
	 private static final Random RANDOM = new Random();
	ObservableList<PieChart.Data> pieChartData;
	BarChart<String, Number> barChart;
	String userAreaName;
	private HashMap<String, Long> OrderMap = new HashMap<>();

	private CategoryAxis xAxis = new CategoryAxis();
	private NumberAxis yAxis = new NumberAxis();

	private HashMap<String, Integer> supplyMap = new HashMap<>();
	private ArrayList<Facility> Facilities = new ArrayList<>();
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;

	ObservableList<String> LocationList;
	ObservableList<String> NameList;

	@FXML
	private Button showBTN;
	
	@FXML
    private Pane pane;

	@FXML
	private Button CloseButton;

	@FXML
	private ImageView Logo;
	@FXML
	private ComboBox<String> locationComboBox;

	@FXML
	private ComboBox<String> nameComboBox;

	@FXML
	private Label reportTypeText;

	@FXML
	private Button backButton;

	@FXML
	private PieChart pieChart;

	@FXML
	private Text areaText;

	@FXML
	private Text dateText;

	@FXML
	void back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/MonthlyReports.fxml", event);
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
		System.out.println(1111);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_COUNT":
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						OrderMap.put((String) values[0], (Long) values[1]);
					}
				}
				break;
			case "#GET_SUPPLY":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String productName = (String) values[0];
						Integer supply = (Integer) values[1];
						supplyMap.put(productName, supply);
					}
				}
				break;
			case "#GET_FACILITY":
//				Facilities = new ArrayList<>();
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer) values[0];
						String FacilityArea = (String) values[1];
						String FacilityLocation = (String) values[2];
						String FacilityName = (String) values[3];
						Integer FacilityThreshholdLevel = (Integer) values[4];
						Facility facility = new Facility(FacilityID, FacilityArea, FacilityLocation, FacilityName,
								FacilityThreshholdLevel, false);
						Facilities.add(facility);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		pieChart.setVisible(false);
//		supplyChart.setVisible(false);
		nameComboBox.setVisible(false);
		locationComboBox.setVisible(false);

		userAreaName = ClientUI.clientController.getUser().getArea();
		areaText.setText(userAreaName);

		String date = ClientUI.clientController.getReportMonth() + "/" + ClientUI.clientController.getReportYear();
		dateText.setText(date);

		String type = ClientUI.clientController.getReportType();
		reportTypeText.setText(type + " Report");

//		Facilities = ClientUI.clientController.getFacilities();
		switch (type) {
		case "Orders":
			pieChart.setVisible(true);
			RequestObjectClient request = new RequestObjectClient("#GET_COUNT",
					String.format(
							("SELECT facilities.FacilityName, COUNT(*) " + "FROM facilities " + "INNER JOIN orders "
									+ "ON orders.facilityID = facilities.FacilityID "
									+ "WHERE FacilityArea = '%s'  and SUBSTRING(orderdate, 4, 2) = '%s' "
									+ " and SUBSTRING(orderdate, 7, 4) = '%s' " + "GROUP BY facilities.FacilityName;"),
							ClientUI.clientController.getUser().getArea(), ClientUI.clientController.getReportMonth(),
							ClientUI.clientController.getReportYear()),
					"*");
			ClientUI.clientController.accept(request);

			ArrayList<PieChart.Data> dataArr = new ArrayList<>(OrderMap.size());

			for (String key : OrderMap.keySet()) {
				dataArr.add(new Data(key, OrderMap.get(key)));
			}

			pieChartData = FXCollections.observableArrayList(dataArr);
			pieChart.setData(pieChartData);

			pieChartData.forEach(data -> data.nameProperty()
					.bind(Bindings.concat(data.getName(), " ", data.pieValueProperty(), " Orders")));
			break;
		case "Supply":
//			supplyChart.setVisible(true);

			if (userAreaName.equals("All")) {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
						String.format("table=facilities", userAreaName), "GET");
				ClientUI.clientController.accept(getFacilities);
			} else {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
						String.format("table=facilities#condition=FacilityArea=%s", userAreaName), "GET");
				ClientUI.clientController.accept(getFacilities);
			}

			arrayLocation = new ArrayList<>();
			arrayName = new ArrayList<>();

			for (int i = 0; i < Facilities.size(); i++) {
				Facility currFac = Facilities.get(i);
				if (!arrayLocation.contains(currFac.getFacilityLocation()))
					arrayLocation.add(currFac.getFacilityLocation());
				arrayName.add(currFac.getFacilityName());
			}
			LocationList = FXCollections.observableArrayList(arrayLocation);
			NameList = FXCollections.observableArrayList(arrayName);

			locationComboBox.setItems(LocationList);
			nameComboBox.setItems(NameList);

			nameComboBox.setVisible(true);
			locationComboBox.setVisible(true);

			break;
		case "customers":

			break;
		}

	}

	@FXML
	void selectLocation(ActionEvent event) {

		// Create an ArrayList to store the names of facilities located in the selected
		// location
		ArrayList<String> names = new ArrayList<>();

		// Clear the contents of the NameComboBox
		nameComboBox.getItems().clear();

		// Iterate through the list of facilities and add the names of those that are
		// located in the selected location
		// to the names ArrayList
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityLocation().equals(locationComboBox.getValue())) {
				names.add(currFac.getFacilityName());
			}
		}

		// Convert the names ArrayList to an ObservableList
		ObservableList<String> namesList = FXCollections.observableArrayList(names);

		// Otherwise, add the names in the namesList to the NameCombo dropdown menu and
		// make it visible
		nameComboBox.getItems().addAll(namesList);
	}

	@FXML
	void showSupplyReports(ActionEvent event) {
		String facilityName = nameComboBox.getValue();

		if (facilityName == null) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Please choose facility name");
			error.showAndWait();
			return;
		}
		RequestObjectClient supplyRequest = new RequestObjectClient("#GET_SUPPLY",
				String.format(("SELECT products.ProductName,productsinfacility.ProductAmount " + "FROM products "
						+ "INNER JOIN facilities " + "INNER JOIN productsinfacility "
						+ "ON productsinfacility.ProductCode = products.ProductCode AND productsinfacility.FacilityID = facilities.FacilityID "
						+ "WHERE FacilityArea = '%s' AND facilities.FacilityLocation = '%s' AND facilities.FacilityName = '%s' "
						+ "GROUP BY products.ProductName"), ClientUI.clientController.getUser().getArea(),
						locationComboBox.getValue(), nameComboBox.getValue()),
				"*");
		ClientUI.clientController.accept(supplyRequest);
		xAxis.setLabel("Products");
		yAxis.setLabel("Supply");
		yAxis.setTickUnit(10);
		yAxis.setTickLabelFont(new Font(12));
		xAxis.setTickLabelFont(new Font(12));
		barChart = new BarChart<>(xAxis, yAxis);
		XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
		for (String product : supplyMap.keySet()) {
			dataSeries.getData().add(new XYChart.Data<>(product, supplyMap.get(product)));
		}
		barChart.getData().add(dataSeries);
		barChart.setAlternativeColumnFillVisible(true);
		Node n;
		int countColor = 0;
		String[] colors = {"red", "orange", "yellow", "green", "blue", "purple", "pink", "brown",  "gray"};
		 ArrayList<String> colorList = new ArrayList<>(Arrays.asList(colors));
		for (String product : supplyMap.keySet()) {
			n = barChart.lookup(String.format(".data%d.chart-bar", countColor));
			countColor += 1;
			int random = RANDOM.nextInt(colorList.size());
			String color = colorList.get(random);
			colorList.remove(random);
			String style = String.format("-fx-bar-fill: %s", color);
			n.setStyle(style);
			
		}
		barChart.setLegendVisible(false);
		VBox vbox = new VBox(barChart);
		vbox.setLayoutX(200);
		vbox.setLayoutY(100);
		vbox.setPrefWidth(800);
		vbox.setPrefHeight(650);
		pane.getChildren().add(vbox);

	
	}
}