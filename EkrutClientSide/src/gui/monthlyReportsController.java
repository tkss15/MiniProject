package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entity.Report;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class monthlyReportsController implements Initializable, IController {

	boolean exists = false;
	private ArrayList<Report> reports;
	private HashMap<String, String> monthToStrMap;
	@FXML
	private Button CloseButton;

	@FXML
	private Text errorMessage;

	@FXML
	private ImageView EKrutLogo;

	@FXML
	private Label selectAreaText;

	@FXML
	private Button watchReportButton;

	@FXML
	private ComboBox<String> selectYear;

	@FXML
	private ComboBox<String> selectMonth;

	@FXML
	private ComboBox<String> selectType;

	@FXML
	private ComboBox<String> selectArea;

	@FXML
	private ImageView ReportLogo;

	@FXML
	private Button backButton;
	/**
	 * after the back button has been pressed, this method starts a new window called
	 * CEOInterface if the initiall log-in was the ceo.
	 * else, goes back to the areaManagerInterface.
	 * @param : event triggers when the back button has been pressed.
	 * */
	@FXML
	void back(ActionEvent event) {
		if(ClientUI.clientController.isCeo())
			ClientUI.sceneManager.ShowSceneNew("../views/CEOInterface.fxml", event);
		else
			ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}
	/**
	 * this method is called when area is selected.
	 * 
	 * */
	@FXML
	void AreaSelect(ActionEvent event) {

	}
	
	/**
	 * this method is called when the "X" button has been pressed.
	 * @param event
	 * */
	@FXML
	void closeWindow(ActionEvent event) {
		back(event);
	}

	/**
	 * when the user presses on the button watch report, if the report exists the relevent report
	 * will be shown in a new screen.
	 * all fields must be selected in order to watch a specific report
	 * @param event 
	 * */
	@FXML
	void watchReport(ActionEvent event) {
		boolean isCEO = false;
		if(ClientUI.clientController.getUser().getArea().equals("All"))
			isCEO = true;
			
		errorMessage.setVisible(false);
		String Year = selectYear.getValue();
		String Month = selectMonth.getValue();
		String Type = selectType.getValue();
		//check if all of the fields have been selected
		if (Year == null || Month == null || Type == null) {
			errorMessage.setText("Year, Month or Type has not been selected! ");
			errorMessage.setVisible(true);
			return;
		}
		//string to get the date format
		String date = Year + "-" + Month;
		//if the login is from the ceo, then the ceo must also choose the area to watch the report from.
		if(isCEO) {
			String Area = selectArea.getValue();
			if(Area == null) {
				errorMessage.setText("You must pick an area to watch a report from!");
				errorMessage.setVisible(true);
				return;
			}
			//query to get the reports
			RequestObjectClient request = new RequestObjectClient("#GET_REPORTS_MRC", 
					String.format("%s#%s#%s#", Area, Type, date), "GET");
			ClientUI.clientController.accept(request);
		}else {
			//query to get the reports
			RequestObjectClient request = new RequestObjectClient("#GET_REPORTS_MRC", 
					String.format("%s#%s#%s#", ClientUI.clientController.getUser().getArea(), Type, date), "GET");
			ClientUI.clientController.accept(request);
		}
		
		if (exists) {
			//search the specific report from the returned query
			for (int i = 0; i < reports.size(); i++) {
				Report currReport = reports.get(i);
				if (currReport.getReportDate().equals(date) && currReport.getReportType().equals(Type)) {
					ClientUI.clientController.setReportYear(Year);
					ClientUI.clientController.setReportMonth(ClientUI.clientController.getHashMapMonths().get(Month));
					ClientUI.clientController.setReportType(Type);

					ClientUI.sceneManager.ShowSceneNew("../views/TypeReportUI.fxml", event);
				}
			}
		} else {
			errorMessage.setText("No such report!");
			errorMessage.setVisible(true);
		}
		exists = false;

	}
	/**
	 * all of the information from the queries is returned here
	 * */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_REPORTS_MRC":
				reports = new ArrayList<>();
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String reportType = (String) values[0];
						String reportDate = (String) values[1];
						String area = (String) values[2];
						Report report = new Report(reportType, reportDate, area);
						reports.add(report);
					}
				} else {
					exists = false;
				}
				break;
			}
		}
	}
	/**
	 * this method is being called when the controller starts
	 * */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		monthToStrMap = new HashMap<>();
		errorMessage.setVisible(false);
		selectAreaText.setVisible(false);
		selectArea.setVisible(false);
		String userArea = ClientUI.clientController.getUser().getArea();
		if (ClientUI.clientController.isCeo()) {
			selectArea.setVisible(true);
			selectAreaText.setVisible(true);
		}

		String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		monthToStrMap.put("January", "01");
		monthToStrMap.put("February", "02");
		monthToStrMap.put("March", "03");
		monthToStrMap.put("April", "04");
		monthToStrMap.put("May", "05");
		monthToStrMap.put("June", "06");
		monthToStrMap.put("July", "07");
		monthToStrMap.put("August", "08");
		monthToStrMap.put("September", "09");
		monthToStrMap.put("October", "10");
		monthToStrMap.put("November", "11");
		monthToStrMap.put("December", "12");

		ClientUI.clientController.setHashMapMonths(monthToStrMap);

		ArrayList<String> months = new ArrayList<>();
		for (int j = 0; j < 12; j++) {
			months.add(monthNames[j]);
		}
		ArrayList<String> years = new ArrayList<>();
		for (int i = 2010; i < 2024; i++)
			years.add("" + i);

		ArrayList<String> areas = new ArrayList<>();
		areas.add("North");
		areas.add("South");
		areas.add("UAE");

		ArrayList<String> types = new ArrayList<>();
		types.add("Orders");
		types.add("Supply");
		types.add("Customers");

		ObservableList<String> monthsList = FXCollections.observableArrayList(months);
		selectMonth.setItems(monthsList);

		ObservableList<String> yearsList = FXCollections.observableArrayList(years);
		selectYear.setItems(yearsList);

		ObservableList<String> typesList = FXCollections.observableArrayList(types);
		selectType.setItems(typesList);
		ObservableList<String> areasList = FXCollections.observableArrayList(areas);
		selectArea.setItems(areasList);
		
		selectArea.setOnAction((e) ->{
			ClientUI.clientController.getUser().setArea(selectArea.getValue());
			
		});
	}
}
