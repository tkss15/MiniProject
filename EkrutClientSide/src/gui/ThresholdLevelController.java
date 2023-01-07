package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ThresholdLevelController implements Initializable, IController {

	     
	String userAreaName;
	private String ID;
	private String Location;
	boolean exists = false;
	private ArrayList<Facility> Facilities;
	private ArrayList<String> arrayId;
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;

	ObservableList<String> IDList;
	ObservableList<String> LocationList;
	ObservableList<String> NameList;

	@FXML
	private Button backButton;

	@FXML
	private Button CloseButton;

	@FXML
	private Button approveButton;

	@FXML
	private Text facilityNameText;

	@FXML
	private Text errorMessageText;

	@FXML
	private Text successMessageText;

	@FXML
	private ComboBox<String> IDCombo;

	@FXML
	private ComboBox<String> LocationCombo;

	@FXML
	private ComboBox<String> NameCombo;

	@FXML
	private TextField SetThresholdTextField;

	@FXML
	private TextField setCurrentFacilityThreshold;

	@FXML
	void approve(ActionEvent event) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);

		String Location = LocationCombo.getValue();
		String Name = NameCombo.getValue();

		String ID = IDCombo.getValue();

		if (Location == null || Name == null || ID == null) {
			errorMessageText.setVisible(true);
			errorMessageText.setText("All Selections must be filled!");
			return;
		}

		String thresholdValueFieldString = SetThresholdTextField.getText();

		if (thresholdValueFieldString.isEmpty()) {
			errorMessageText.setVisible(true);
			errorMessageText.setText("Please Select Threshold Level Value!");
			return;
		}

		int thresholdValueField = Integer.parseInt(SetThresholdTextField.getText());
		RequestObjectClient updateFacility = new RequestObjectClient("#UPDATE_FACILITY",
				String.format("table=facilities#condition=FacilityID=%s#values=FacilityThreshholdLevel=%s", ID,
						thresholdValueField),
				"PUT");
		ClientUI.clientController.accept(updateFacility);
		successMessageText.setVisible(true);
		successMessageText.setText("Facility Threshold Updated!");
		setCurrentFacilityThreshold.setText(SetThresholdTextField.getText());
		return;

	}

	/**
	 * This method is called when the user selects a location from the LocationCombo
	 * dropdown menu. It hides any success or error messages that might have been
	 * previously displayed, and clears the contents of the NameCombo and IDCombo
	 * dropdown menus. It then iterates through a list of facilities and adds the
	 * names of those that are located in the selected location to an
	 * ObservableList, which is then displayed in the NameCombo dropdown menu.
	 *
	 * @param event the ActionEvent triggered by the user selecting a location from
	 *              the LocationCombo dropdown menu
	 */
	@FXML
	void SelectedLocation(ActionEvent event) {
		// Hide any success or error messages that might have been previously displayed
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);

		// Clear the contents of the SetThresholdTextField and
		// setCurrentFacilityThreshold text fields
		SetThresholdTextField.setText(null);
		setCurrentFacilityThreshold.setText(null);

		// Create an ArrayList to store the names of facilities located in the selected
		// location
		ArrayList<String> names = new ArrayList<>();

		// Clear the contents of the NameCombo and IDCombo dropdown menus
		NameCombo.getItems().clear();
		IDCombo.getItems().clear();

		// Iterate through the list of facilities and add the names of those that are
		// located in the selected location
		// to the names ArrayList
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityLocation().equals(LocationCombo.getValue())) {
				names.add(currFac.getFacilityName());
			}
		}

		// Convert the names ArrayList to an ObservableList
		ObservableList<String> namesList = FXCollections.observableArrayList(names);

		// If the namesList is empty, display an error message and return
		if (namesList.isEmpty()) {
			errorMessageText.setText("No Facilities in the Location!");
			errorMessageText.setVisible(true);
			return;
		}

		// Otherwise, add the names in the namesList to the NameCombo dropdown menu and
		// make it visible
		NameCombo.getItems().addAll(namesList);
		NameCombo.setVisible(true); // select Name
	}

	@FXML
	void selectName(ActionEvent event) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		IDCombo.getItems().clear();
		ArrayList<String> IDs = new ArrayList<>();
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityName().equals(NameCombo.getValue())) {
				IDs.add(String.valueOf(currFac.getFacilityID()));
			}
			ObservableList<String> IDList = FXCollections.observableArrayList(IDs);

			IDCombo.setItems(IDList);
			IDCombo.setVisible(true);

		}
	}

	@FXML
	void selectID(ActionEvent event) {
		String ID = IDCombo.getValue();
		if (ID == null) {
			return;
		}
		int intID = Integer.parseInt(ID);
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityID() == intID) {
				int currThreshold = currFac.getFacilityThresholder();
				String strThreshold = String.valueOf(currThreshold);
				setCurrentFacilityThreshold.setText(strThreshold);
			}
		}
	}

	@FXML
	void back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
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
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_FACILITY":
				Facilities = new ArrayList<>();
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
			case "#UPDATE_FACILITY":
				break;

			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ClientUI.clientController.setController(this);

		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);

		Facilities = new ArrayList<>();
		userAreaName = ClientUI.clientController.getUser().getArea();
		facilityNameText.setText(String.format("%s Area", userAreaName));
		if (userAreaName.equals("All")) {
			facilityNameText.setText("All Areas");
			RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
					String.format("table=facilities", userAreaName), "GET");
			ClientUI.clientController.accept(getFacilities);
		} else {
			RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
					String.format("table=facilities#condition=FacilityArea=%s", userAreaName), "GET");
			ClientUI.clientController.accept(getFacilities);
		}

		arrayId = new ArrayList<>();
		arrayLocation = new ArrayList<>();
		arrayName = new ArrayList<>();

		System.out.println(Facilities);

		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			arrayId.add(String.valueOf(currFac.getFacilityID()));
			arrayLocation.add(currFac.getFacilityLocation());
			arrayName.add(currFac.getFacilityName());
		}
		IDList = FXCollections.observableArrayList(arrayId);
		LocationList = FXCollections.observableArrayList(arrayLocation);
		NameList = FXCollections.observableArrayList(arrayName);

		IDCombo.setItems(IDList);
		LocationCombo.setItems(LocationList);
		NameCombo.setItems(NameList);

		IDCombo.setVisible(false);
		NameCombo.setVisible(false);

	}

}
