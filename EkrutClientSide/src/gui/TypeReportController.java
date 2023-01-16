package gui;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TypeReportController implements Initializable, IController {

	public static class RandomColor {

		public static String generateRandomHexColor() {
			Random rand = new Random();
			StringBuilder color = new StringBuilder("#");
			for (int i = 0; i < 6; i++) {
				int number = rand.nextInt(16);
				color.append(Integer.toHexString(number));
			}
			return color.toString();
		}
	}

	boolean exists = false;
	private ObservableList<PieChart.Data> pieChartData;
	private ObservableList<PieChart.Data> pieChartDataBelowThreshold;
	private BarChart<String, Number> barChart;
	private String userAreaName;
	private HashMap<String, Long> OrderMap = new HashMap<>();

	private HashMap<String, Long> customerHistogramMap = new HashMap<>();

	private HashMap<String, Long> supplyBelowThresholdMap = new HashMap<>();

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	private HashMap<String, Integer> supplyMap = new HashMap<>();

	private ArrayList<Facility> Facilities = new ArrayList<>();
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;

	ObservableList<String> LocationList;
	ObservableList<String> NameList;

	@FXML
	private Label thresholdLabel;
	
	@FXML
	private Label productsBelowThreshold;
	@FXML
	private Pane paneForBarChart;
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
	private Button showCutomerReportBTN;
	@FXML
	private Label reportTypeText;

	@FXML
	private Button backButton;

	@FXML
	private PieChart pieChartBelowThreshold;

	@FXML
	private PieChart pieChart;

	@FXML
	private Text areaText;

	@FXML
	private Text dateText;

	@FXML
	void back(ActionEvent event) {
		if(ClientUI.clientController.isCeo()) {
			ClientUI.clientController.getUser().setArea("All");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/MonthlyReports.fxml", event);
	}

	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS", // DONE
					String.format("%s#",
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
			case "#GET_COUNT_TRC":
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						OrderMap.put((String) values[0], (Long) values[1]);
					}
				}
				break;
			case "#GET_SUPPLY_REPORT_TRC":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String productName = (String) values[0];
						Integer supply = (Integer) values[1];
						supplyMap.put(productName, supply);
					}
				}
				break;
			case "#GET_ALL_FACILITIES_TRC":
//				Facilities = new ArrayList<>();
				if (serverResponse.Responsedata.size() != 0) {
					saveFacilities(serverResponse);
				}
				break;
			case"#GET_FACILITIES_FROM_AREA_TRC":
				if (serverResponse.Responsedata.size() != 0) {
					saveFacilities(serverResponse);
				}
				break;
			case "#GET_CUTOMER_HISTOGRAM":
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						customerHistogramMap.put((String) values[0], (long) values[1]);
					}
				}
				break;
			case "#GET_SUPPLY_BELOW_THRESHOLD_TRC":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						supplyBelowThresholdMap.put((String) values[0], (Long) values[1]);
					}
				}
			}
		}
	}

	private void saveFacilities(ResponseObject serverResponse) {
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		pieChart.setVisible(false);
		pieChartBelowThreshold.setVisible(false);
//		supplyChart.setVisible(false);
		nameComboBox.setVisible(false);
		nameComboBox.setDisable(true);
		locationComboBox.setVisible(false);
		productsBelowThreshold.setVisible(false);
		showCutomerReportBTN.setVisible(false);
		thresholdLabel.setVisible(false);

		userAreaName = ClientUI.clientController.getUser().getArea();
		areaText.setText(userAreaName);

		String date = ClientUI.clientController.getReportMonth() + "/" + ClientUI.clientController.getReportYear();
		dateText.setText(date);

		String type = ClientUI.clientController.getReportType();
		reportTypeText.setText(type + " Report");

//		Facilities = ClientUI.clientController.getFacilities();
		switch (type) {
		case "Orders":
//			if (userAreaName.equals("All")) {
//				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
//						String.format("table=facilities", userAreaName), "GET");
//				ClientUI.clientController.accept(getFacilities);
//			} else {
//				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
//						String.format("table=facilities#condition=FacilityArea=%s", userAreaName), "GET");
//				ClientUI.clientController.accept(getFacilities);
//			}
//			
			showBTN.setVisible(false);
			pieChart.setVisible(true);
			RequestObjectClient request = new RequestObjectClient("#GET_COUNT_TRC", // DONE
					String.format(
							("%s#%s#%s#"),
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

//			pieChart.setLegendSide(Side.LEFT);

			pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", "Orders")));
			final Label caption = new Label("");

			caption.setTextFill(Color.DARKORANGE);
			caption.setStyle("-fx-font: 24 arial;");
			Tooltip container = new Tooltip();
			container.setGraphic(caption);

			pieChart.getData().forEach((data) -> {
				data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
//					if (container.isShowing()) {
//						container.hide();
//					}
					double x = e.getScreenX() + 10;
					double y = e.getScreenY() + 10;
					caption.setText(Integer.valueOf((int) data.getPieValue()) + " Orders");
					container.show(pane, x, y);
				});
			});
			pieChart.setOnMouseExited((e) -> {
				container.hide();
			});
			break;
		case "Supply":
//			supplyChart.setVisible(true);
			if (userAreaName.equals("All")) {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_ALL_FACILITIES_TRC","", "GET"); //DONE
				ClientUI.clientController.accept(getFacilities);
			} else {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITIES_FROM_AREA_TRC", // DONE
						String.format("%s#", userAreaName), "GET");
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
			pieChartBelowThreshold.setVisible(true);

			break;
		case "Customers":
			locationComboBox.setVisible(true);
			nameComboBox.setVisible(true);
			showCutomerReportBTN.setVisible(true);

			if (userAreaName.equals("All")) {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_ALL_FACILITIES_TRC","", "GET"); //DONE
				ClientUI.clientController.accept(getFacilities);
			} else {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITIES_FROM_AREA_TRC", // DONE
						String.format("%s#", userAreaName), "GET");
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
		nameComboBox.setDisable(false);
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

		supplyMap.clear();
		RequestObjectClient supplyRequest = new RequestObjectClient("#GET_SUPPLY_REPORT_TRC", // DONE
				String.format(("%s#%s#%s#"), ClientUI.clientController.getUser().getArea(),
						locationComboBox.getValue(), nameComboBox.getValue()),
				"*");
		ClientUI.clientController.accept(supplyRequest);

		Facility currentFacility = null;
		// find the facility by name
		for (Facility currFac : Facilities) {
			if (currFac.getFacilityName().equals(nameComboBox.getValue())) {
				currentFacility = currFac;
				break;
			}
		}
		// evalute for each product if its amount is less then threshold.

		thresholdLabel.setTextFill(Color.RED);
		thresholdLabel.setText("Current Facility\nThreshold: " + currentFacility.getFacilityThresholder()+"");
		thresholdLabel.setVisible(true);
		
		StringBuilder s = new StringBuilder();
		s.append("Products which are below threshold : \n");
		for (String str : supplyMap.keySet()) {
			if (supplyMap.get(str).compareTo(currentFacility.getFacilityThresholder()) <= 0) {
				s.append(str + ", ");
			}
		}
		s.deleteCharAt(s.length() - 2);
		productsBelowThreshold.setVisible(true);
		productsBelowThreshold.setText(s.toString());

		createBarChart("Products", "Supply", supplyMap);
		System.out.println((barChart.getLayoutX() + " " + barChart.getLayoutY()));

		supplyBelowThresholdMap.clear();
		RequestObjectClient supplyBelowThresholdRequest = new RequestObjectClient("#GET_SUPPLY_BELOW_THRESHOLD_TRC", // DONE
				String.format("%s#",ClientUI.clientController.getUser().getArea()),"*");

		ClientUI.clientController.accept(supplyBelowThresholdRequest);

		ArrayList<PieChart.Data> dataArr = new ArrayList<>(supplyBelowThresholdMap.size());

		for (String key : supplyBelowThresholdMap.keySet()) {
			dataArr.add(new Data(key, supplyBelowThresholdMap.get(key)));
		}

		pieChartDataBelowThreshold = FXCollections.observableArrayList(dataArr);
		pieChartBelowThreshold.setData(pieChartDataBelowThreshold);

//		pieChart.setLegendSide(Side.LEFT);

		pieChartDataBelowThreshold
				.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", "Products")));
		final Label caption = new Label("");

		caption.setTextFill(Color.DARKORANGE);
		caption.setStyle("-fx-font: 24 arial;");
		Tooltip container = new Tooltip();
		container.setGraphic(caption);

		pieChartBelowThreshold.getData().forEach((data) -> {
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
//				if (container.isShowing()) {
//					container.hide();
//				}
				double x = e.getScreenX() + 10;
				double y = e.getScreenY() + 10;
				caption.setText(Integer.valueOf((int) data.getPieValue()) + " Products");
				container.show(pane, x, y);
			});
		});
		pieChartBelowThreshold.setOnMouseExited((e) -> {
			container.hide();
		});

	}

	@FXML
	void showCustomerReport(ActionEvent event) {
		String facilityName = nameComboBox.getValue();
		String month = ClientUI.clientController.getReportMonth();

		if (facilityName == null) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Please choose facility name");
			error.showAndWait();
			return;
		}
		customerHistogramMap.clear();
		RequestObjectClient customerHistogram = new RequestObjectClient("#GET_CUTOMER_HISTOGRAM", // DONE
				String.format("%s#%s#",facilityName, month), "*");
		ClientUI.clientController.accept(customerHistogram);

		createBarChart("Users", "Purchases", customerHistogramMap);
	}

	private void createBarChart(String xAxisName, String yAxisName, HashMap<String, ? extends Number> map) {
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel(xAxisName);
		yAxis.setLabel(yAxisName);
		yAxis.setTickUnit(10);
		yAxis.setTickLabelFont(new Font(12));
		xAxis.setTickLabelFont(new Font(12));
		barChart = new BarChart<>(xAxis, yAxis);
		XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
		for (String product : map.keySet()) {
			dataSeries.getData().add(new XYChart.Data<>(product, map.get(product)));
		}
		barChart.getData().add(dataSeries);
		barChart.setAlternativeColumnFillVisible(true);
		Node n;
		int countColor = 0;
		for (String product : map.keySet()) {
			n = barChart.lookup(String.format(".data%d.chart-bar", countColor));
			countColor += 1;
			String randomColor = RandomColor.generateRandomHexColor();

			String style = String.format("-fx-bar-fill: %s", randomColor);
			n.setStyle(style);

		}
		barChart.setLegendVisible(false);
		VBox vbox = new VBox(barChart);
		vbox.setLayoutX(187);
		vbox.setLayoutY(275);
		vbox.setPrefWidth(800);
		vbox.setPrefHeight(550);
		paneForBarChart.getChildren().clear();
		paneForBarChart.getChildren().add(vbox);
	}
}