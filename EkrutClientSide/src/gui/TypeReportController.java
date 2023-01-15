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

	/**
	 * helper class to generate random colours.
	 *
	 */
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

	
	private ObservableList<PieChart.Data> pieChartData; 
	private ObservableList<PieChart.Data> pieChartDataBelowThreshold;
	private BarChart<String, Number> barChart;
	private String userAreaName;
	private HashMap<String, Long> OrderMap = new HashMap<>();/*hash map which saves facilities and the number of orders in them.*/

	private HashMap<String, Long> customerHistogramMap = new HashMap<>();/*hash map which saves mapping of customers to their amount of purchases per facility. */

	private HashMap<String, Long> supplyBelowThresholdMap = new HashMap<>();/*hash map which saves mapping of products and their supply */

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	private HashMap<String, Integer> supplyMap = new HashMap<>();/**/

	private ArrayList<Facility> Facilities = new ArrayList<>();/**/
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

	/**
	 * method that triggers when the "Back" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void back(ActionEvent event) {
		if(ClientUI.clientController.isCeo()) {
			ClientUI.clientController.getUser().setArea("All");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/MonthlyReports.fxml", event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			/**request a query to update the user status to be "Offline". */
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS", 
					String.format("%s#",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	@Override
	/**
	 * saving all the data which is returned from the DB and relevant for the current controller.
	 * saving all the data of the charts:
	 * 1. OrderMap saves the facility and its number of purchases in a given month and a given year from the current user area.
	 * 2. supplyMap saves the products and their supply in a given facility from the area.
	 * 3. customerHistogramMap saves the customers and their activity level (aka number of purchases).
	 * 4. supplyBelowThresholdMap saves all the products which are below threshold and their supply in a given facility from the area.
	 * 
	 * saving all the facilities according the the current user area.
	 * 
	 */
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_COUNT_TRC":
				/**saving the data for the order report in OrderMap*/
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						OrderMap.put((String) values[0], (Long) values[1]);
					}
				}
				break;
			case "#GET_SUPPLY_REPORT_TRC":
				/*saving the data for the supply report in the supplyMap*/
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
				//saving all of the facilities in the Facilities array list.
				if (serverResponse.Responsedata.size() != 0) {
					saveFacilities(serverResponse);
				}
				break;
				//saving all of the facilities in the area in the array list.
			case"#GET_FACILITIES_FROM_AREA_TRC":
				if (serverResponse.Responsedata.size() != 0) {
					saveFacilities(serverResponse);
				}
				break;
			case "#GET_CUTOMER_HISTOGRAM":
				/*saving the data for the customers report in the customerHistogramMap*/
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						customerHistogramMap.put((String) values[0], (long) values[1]);
					}
				}
				break;
			case "#GET_SUPPLY_BELOW_THRESHOLD_TRC":
				/*saving the data for the supply report in the supplyBelowThresholdMap*/
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						supplyBelowThresholdMap.put((String) values[0], (Long) values[1]);
					}
				}
			}
		}
	}

	/**
	 * saves all facilities which return from a query request in the Facilities Array List.
	 * @param serverResponse  - response object which is returned from the server which contains the query's output. 
	 */
	private void saveFacilities(ResponseObject serverResponse) {
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
		//setting the static clientController to be this controller.
		ClientUI.clientController.setController(this);
		
		/**
		 * setting all GUI elements not to be visible.
		 * they will be set visible only when a certain report type is chosen.
		 * */
		pieChart.setVisible(false);
		pieChartBelowThreshold.setVisible(false);
		nameComboBox.setVisible(false);
		nameComboBox.setDisable(true);
		locationComboBox.setVisible(false);
		productsBelowThreshold.setVisible(false);
		showCutomerReportBTN.setVisible(false);
		thresholdLabel.setVisible(false);

		
		/**
		 * saving the user area, date and type.
		 */
		
		userAreaName = ClientUI.clientController.getUser().getArea();
		areaText.setText(userAreaName);

		String date = ClientUI.clientController.getReportMonth() + "/" + ClientUI.clientController.getReportYear();
		dateText.setText(date);

		String type = ClientUI.clientController.getReportType();
		reportTypeText.setText(type + " Report");


		/**
		 * we build and show the report according to the type. type can be one of the following: 
		 * Orders, Supply , Customers.
		 */
		switch (type) {
		case "Orders":
			//first set the relevant GUI elements to be visible for the user.
			showBTN.setVisible(false);
			pieChart.setVisible(true);
			/**
			 * request a query to get the data for the orders report.
			 * OrderMap saves the facility and its number of purchases in a given month and a given year from the current user area.
			 */
			RequestObjectClient request = new RequestObjectClient("#GET_COUNT_TRC",
					String.format(
							("%s#%s#%s#"),
							ClientUI.clientController.getUser().getArea(), ClientUI.clientController.getReportMonth(),
							ClientUI.clientController.getReportYear()),
					"*");
			ClientUI.clientController.accept(request);

			//building the pie chart.
			
			ArrayList<PieChart.Data> dataArr = new ArrayList<>(OrderMap.size());

			//adding all the data from the OrderMap to the dataArr array list.
			for (String key : OrderMap.keySet()) {
				dataArr.add(new Data(key, OrderMap.get(key)));
			}

			//setting the data of the pie chart to be the data from the data array.
			pieChartData = FXCollections.observableArrayList(dataArr);
			pieChart.setData(pieChartData);

			//bind each data with the word "Orders" in the end.
			//This will result in the name of each element in the pieChartData collection being 
			//updated to include "Orders" at the end.
			
			pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", "Orders")));
			final Label caption = new Label("");

			/**
			 * setting effects on the chart when the mouse enters a certain data and when the mouse exists the chart.
			 * when the mouse enters a certain slice of the pie chart - a label will pop up with the amount of orders which were made.
			 * when the mouse exists the charts area the label will disappear.
			 */
			caption.setTextFill(Color.DARKORANGE);
			caption.setStyle("-fx-font: 24 arial;");
			Tooltip container = new Tooltip();
			container.setGraphic(caption);

			pieChart.getData().forEach((data) -> {
				data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
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
			/**
			 * request a query to get all facilities from the user's area.
			 */
			if (userAreaName.equals("All")) {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_ALL_FACILITIES_TRC","", "GET");
				ClientUI.clientController.accept(getFacilities);
			} else {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITIES_FROM_AREA_TRC", 
						String.format("%s#", userAreaName), "GET");
				ClientUI.clientController.accept(getFacilities);
			}

			//setting the combo boxes data with the locations and names of the facilities.
			
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

			//lastly set the relevant GUI elements to be visible for the user.
			nameComboBox.setVisible(true);
			locationComboBox.setVisible(true);
			pieChartBelowThreshold.setVisible(true);

			break;
		case "Customers":
			//first set the relevant GUI elements to be visible for the user.
			locationComboBox.setVisible(true);
			nameComboBox.setVisible(true);
			showCutomerReportBTN.setVisible(true);

			/**
			 * request a query to get all facilities from the user's area.
			 */
			if (userAreaName.equals("All")) {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_ALL_FACILITIES_TRC","", "GET"); 
				ClientUI.clientController.accept(getFacilities);
			} else {
				RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITIES_FROM_AREA_TRC",
						String.format("%s#", userAreaName), "GET");
				ClientUI.clientController.accept(getFacilities);
			}

			//setting the combo boxes data with the locations and names of the facilities.
			
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
	/**
	 * this method is invoked when the user (some manager) chooses a location from the locationComboBox.
	 * @param event
	 */
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

		// Otherwise, add the names in the namesList to the NameCombo drop down menu and
		// make it visible
		nameComboBox.getItems().addAll(namesList);
		nameComboBox.setDisable(false);
	}

	@FXML
	/**
	 * this method is invoked when the user presses on the "Show Reports" button.
	 * @param event
	 */
	void showSupplyReports(ActionEvent event) {
		String facilityName = nameComboBox.getValue();
		
		//checking the user input, of the facility name (with the combo box) and if it is null - an error pop up will show up and the method returns;
		if (facilityName == null) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Please choose facility name");
			error.showAndWait();
			return;
		}

		supplyMap.clear();
		
		/**
		 * request a query to get the amount of supply in the manager's area, in the chosen location at a chosen facility.
		 * the manager's area is = ClientUI.clientController.getUser().getArea().
		 * the location is chosen be the manager via the locationComboBox.
		 * the facility name is chosen by the manager via the nameComboBox.
		 */
		RequestObjectClient supplyRequest = new RequestObjectClient("#GET_SUPPLY_REPORT_TRC",
				String.format(("%s#%s#%s#"), ClientUI.clientController.getUser().getArea(),
						locationComboBox.getValue(), nameComboBox.getValue()),
				"*");
		ClientUI.clientController.accept(supplyRequest);

		// find the facility by name
		Facility currentFacility = null;
		for (Facility currFac : Facilities) {
			if (currFac.getFacilityName().equals(nameComboBox.getValue())) {
				currentFacility = currFac;
				break;
			}
		}
		
		//creating a label to show all the products below the threshold level.
		thresholdLabel.setTextFill(Color.RED);
		thresholdLabel.setText("Current Facility\nThreshold: " + currentFacility.getFacilityThresholder()+"");
		thresholdLabel.setVisible(true);
		
		StringBuilder s = new StringBuilder();
		s.append("Products which are below threshold : \n");
		
		// evaluate for each product if its amount is less then threshold.
		for (String str : supplyMap.keySet()) {
			if (supplyMap.get(str).compareTo(currentFacility.getFacilityThresholder()) <= 0) {
				s.append(str + ", ");
			}
		}
		//finishing building the string which represents the amount of products which is less then the threshold,
		//and setting the text label to show that string.
		s.deleteCharAt(s.length() - 2);
		productsBelowThreshold.setVisible(true);
		productsBelowThreshold.setText(s.toString());

		createBarChart("Products", "Supply", supplyMap);

		
		supplyBelowThresholdMap.clear();
		/**
		 * request a query to get all the supply below threshold of all facilities in the current area.
		 */
		RequestObjectClient supplyBelowThresholdRequest = new RequestObjectClient("#GET_SUPPLY_BELOW_THRESHOLD_TRC",
				String.format("%s#",ClientUI.clientController.getUser().getArea()),"*");

		ClientUI.clientController.accept(supplyBelowThresholdRequest);

		//building the pie chart based on the data received from the query request.
		//exactly the same way as before.
		
		ArrayList<PieChart.Data> dataArr = new ArrayList<>(supplyBelowThresholdMap.size());

		for (String key : supplyBelowThresholdMap.keySet()) {
			dataArr.add(new Data(key, supplyBelowThresholdMap.get(key)));
		}

		pieChartDataBelowThreshold = FXCollections.observableArrayList(dataArr);
		pieChartBelowThreshold.setData(pieChartDataBelowThreshold);

		
		pieChartDataBelowThreshold
				.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", "Products")));
		final Label caption = new Label("");

		caption.setTextFill(Color.DARKORANGE);
		caption.setStyle("-fx-font: 24 arial;");
		Tooltip container = new Tooltip();
		container.setGraphic(caption);

		pieChartBelowThreshold.getData().forEach((data) -> {
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
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
	/**
	 * this method is invoked when the user presses on the "Show Reports" button.
	 * @param event
	 */
	void showCustomerReport(ActionEvent event) {
		String facilityName = nameComboBox.getValue();
		String month = ClientUI.clientController.getReportMonth();

		//checking the user input and if it is null - an error pop up will show up and the method returns;
		if (facilityName == null) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Please choose facility name");
			error.showAndWait();
			return;
		}
		customerHistogramMap.clear();
		/**
		 * request a query to get the customers who made purchases in the selected facility (facilityName) in the selected month (month),
		 * and how many purchases were made by them.
		 * from the data retrieved we build the chart to show the level of activity of each customer in the given facility and given month.
		 */
		
		RequestObjectClient customerHistogram = new RequestObjectClient("#GET_CUTOMER_HISTOGRAM",
				String.format("%s#%s#",facilityName, month), "*");
		ClientUI.clientController.accept(customerHistogram);

		//creates the customer histogram bar chart.
		createBarChart("Users", "Purchases", customerHistogramMap);
	}

	/**
	 * creates a bar chart with xAxisName,yAxisName names for the X and Y axis using a map which contains the data of the chart, the keys are X's and values are Y's.
	 * @param xAxisName - name of the x Axis elements.
	 * @param yAxisName - name of the y Axis elements.
	 * @param map - a map of Strings which represent the x axis elements Keys and values of Numbers (Long or Integer).
	 */
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
			//building the bar chart from the values of the hashMap as a key value function.
			dataSeries.getData().add(new XYChart.Data<>(product, map.get(product)));
		}
		//adding the data series to the bar chart.
		barChart.getData().add(dataSeries);
		barChart.setAlternativeColumnFillVisible(true);
		Node n;
		int countColor = 0;
		//generating different colours for each bar in the graph.
		for (String product : map.keySet()) {
			n = barChart.lookup(String.format(".data%d.chart-bar", countColor));
			countColor += 1;
			String randomColor = RandomColor.generateRandomHexColor();

			String style = String.format("-fx-bar-fill: %s", randomColor);
			n.setStyle(style);

		}
		barChart.setLegendVisible(false);
		//adding the barChart to a new VBox.
		VBox vbox = new VBox(barChart);
		vbox.setLayoutX(187);
		vbox.setLayoutY(275);
		vbox.setPrefWidth(800);
		vbox.setPrefHeight(550);
		//clearing the pane of previous bar chart and adding the new bar chart.
		paneForBarChart.getChildren().clear();
		paneForBarChart.getChildren().add(vbox);
	}
}