package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class TypeReportController implements Initializable, IController {
	boolean exists = false;
	ObservableList<PieChart.Data> pieChartData;
	String userAreaName;
	private HashMap<String, Long> OrderMap = new HashMap<>();

	@FXML
	private Button CloseButton;

	@FXML
	private ImageView Logo;

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
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		userAreaName = ClientUI.clientController.getUser().getArea();
		areaText.setText(userAreaName);

		String date = ClientUI.clientController.getReportMonth() + "/" + ClientUI.clientController.getReportYear();
		dateText.setText(date);

		String type = ClientUI.clientController.getReportType();
		reportTypeText.setText(type + " Report");

		switch (type) {
		case "Orders":
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
			
			break;
		case "customers":
			
			break;
		}

	}
}