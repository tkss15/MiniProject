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

	/**
	 * triggers when the approve button is pressed. this method changes the
	 * threshold level of the selected facility
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 * 
	 */
	@FXML
	void approve(ActionEvent event) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);

		String Location = LocationCombo.getValue();

		String Name = NameCombo.getValue();

		String ID = IDCombo.getValue();
		// checks if all balues have been selected
		if (Location == null || Name == null || ID == null) {
			errorMessageText.setVisible(true);
			errorMessageText.setText("All Selections must be filled!");
			return;
		}

		// get the threshold desired
		String thresholdValueFieldString = SetThresholdTextField.getText();

		if (thresholdValueFieldString.isEmpty()) {
			errorMessageText.setVisible(true);
			errorMessageText.setText("Please Select Threshold Level Value!");
			return;
		}
		// update the threshold level in the facilities table
		int thresholdValueField = Integer.parseInt(SetThresholdTextField.getText());
		RequestObjectClient updateFacility = new RequestObjectClient("#THRESHOLD_UPDATE_FACILITY",
				String.format("%s#%s#", ID, thresholdValueField), "PUT");

		ClientUI.clientController.accept(updateFacility);

		successMessageText.setVisible(true);
		successMessageText.setText("Facility Threshold Updated!");
		// update the faciliy threshold text
		setCurrentFacilityThreshold.setText(SetThresholdTextField.getText());
		return;

	}

	/**
	 * This method is called when the user selects a location from the LocationCombo
	 * after selected location, the NameCombo will display all of the facilities in
	 * the selected location
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
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

	/**
	 * after a Facility Name is selected, the id of the facility will be set in the
	 * IDCombo
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void selectName(ActionEvent event) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		IDCombo.getItems().clear();
		ArrayList<String> IDs = new ArrayList<>();
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			// get the correct facility id based on the location and name
			if (currFac.getFacilityName().equals(NameCombo.getValue())) {
				IDs.add(String.valueOf(currFac.getFacilityID()));
			}

			ObservableList<String> IDList = FXCollections.observableArrayList(IDs);
			// set the id
			IDCombo.setItems(IDList);
			IDCombo.setVisible(true);

		}
	}

	/**
	 * after selecting the facility id, its threshold will be displayed in a text
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
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

	/**
	 * method that triggers when the "Back" button has been pressed
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	/**
	 * retrieves the query data from the server to this method, where the query
	 * result set is distributed between all of the cases.
	 * 
	 * @author David
	 * @param data that returns from the server
	 * 
	 */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#THRESHOLD_GET_FACILITY":
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
			case "#THRESHOLD_UPDATE_FACILITY":
				break;

			}
		}
	}

	/**
	 * Initialize the controller, so that all of the initial settings will be as we
	 * wish. This method actives as the controller activates
	 * 
	 * @author David
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ClientUI.clientController.setController(this);

		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		setCurrentFacilityThreshold.setDisable(true);
		Facilities = new ArrayList<>();
		userAreaName = ClientUI.clientController.getUser().getArea();
		facilityNameText.setText(String.format("%s Area", userAreaName));
		if (userAreaName.equals("All")) { // this part is implemented so it will be easier to continue to the second
											// phase, it is not neccessary yet.
			facilityNameText.setText("All Areas");
			RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
					String.format("table=facilities", userAreaName), "GET");//unused
			ClientUI.clientController.accept(getFacilities);
		} else {// in our implementation, the else statement will be activated, but the if
				// statement is implemented well
			RequestObjectClient getFacilities = new RequestObjectClient("#THRESHOLD_GET_FACILITY",
					String.format("%s#", userAreaName), "GET");
			ClientUI.clientController.accept(getFacilities);
		}

		arrayId = new ArrayList<>();
		arrayLocation = new ArrayList<>();
		arrayName = new ArrayList<>();

		// System.out.println(Facilities);// check the facilities

		// set the combo boxes
		for (int i = 0; i < Facilities.size(); i++) {
			Facility currFac = Facilities.get(i);
			arrayId.add(String.valueOf(currFac.getFacilityID()));
			if (!arrayLocation.contains(currFac.getFacilityLocation()))
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
