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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class monthlyReportsController implements Initializable, IController {

	boolean exists = false;
	private ArrayList<Report> reports;
	private HashMap<String,String> monthToStrMap;
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
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	@FXML
	void closeWindow(ActionEvent event) {
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

	@FXML
	void watchReport(ActionEvent event) {
		errorMessage.setVisible(false);
		String Year = selectYear.getValue();
		String Month = selectMonth.getValue();
		String Type = selectType.getValue();
		if (Year == null || Month == null || Type == null) {
			errorMessage.setText("Year, Month or Type has not been selected! ");
			errorMessage.setVisible(true);
			return;
		}
		RequestObjectClient request = new RequestObjectClient("#GET_REPORTS",
				String.format("table=reports#condition=Area=%s", ClientUI.clientController.getUser().getArea()), "GET");
		ClientUI.clientController.accept(request);
		String date = Year + "-" + Month;
		if(exists) {
			for(int i = 0; i< reports.size(); i++) {
				Report currReport = reports.get(i);
				if(currReport.getReportDate().equals(date) && currReport.getReportType().equals(Type)) {
					ClientUI.clientController.setReportYear(Year);
					ClientUI.clientController.setReportMonth(ClientUI.clientController.getHashMapMonths().get(Month));
					ClientUI.clientController.setReportType(Type);
					
					ClientUI.sceneManager.ShowSceneNew("../views/TypeReportUI.fxml", event);
				}
			}
		}
	
	}

	
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_REPORTS":
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
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		monthToStrMap = new HashMap<>();
		errorMessage.setVisible(false);

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
	}
}
