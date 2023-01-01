package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.ImportedUser;
import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ThresholdLevelController implements Initializable, IController {
	
	String userAreaName;
	boolean exists = false;
	private ArrayList<Facility> Facilities;
	private Facility facility;
	
    @FXML
    private Button backButton;
    
    @FXML
    private TextField facilityLocation;

    @FXML
    private TextField facilityID;

    @FXML
    private TextField facilityName;

    @FXML
    private Button CloseButton;

    @FXML
    private Button approveButton;

    @FXML
    private Slider thresholdSlider;

    @FXML
    private Text facilityNameText;
    
    @FXML
    private Text errorMessageText;
    
    @FXML
    private Text successMessageText;
	

    @FXML
    void approve(ActionEvent event) {
    	successMessageText.setVisible(false);
    	errorMessageText.setVisible(false);
    	String name = facilityName.getText();
    	String ID = facilityID.getText();
    	int sliderValue = (int) thresholdSlider.getValue();
    	String sliderValueSTR = Integer.toString(sliderValue);
    	if(ID.isEmpty()) {
    		errorMessageText.setVisible(true);
			errorMessageText.setText("One or more fields are empty!");
			return;
    	}
    	int intID = Integer.parseInt(ID);
    	String location = facilityLocation.getText(); 
    	if(name.isEmpty() || intID == 0 || location.isEmpty()) {
    		errorMessageText.setVisible(true);
    		errorMessageText.setText("One or more fields are empty!");
    		return;
    	}
		RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
				"table=facilities#condition="+String.format("FacilityArea=%s",userAreaName ), "GET");
		ClientUI.clientController.accept(getFacilities);
		if(exists == true) {
			for(int i = 0; i< Facilities.size();i++) {
				Facility currFac = Facilities.get(i);
				if(currFac.getFacilityID() == intID && currFac.getFacilityLocation().equals(location)
						&& currFac.getFacilityName().equals(name)) {
					RequestObjectClient updateFacility = new RequestObjectClient("#UPDATE_FACILITY",
							String.format("table=facilities#condition=FacilityID=%s#values=FacilityThreshholdLevel=%s",
									ID,sliderValueSTR), "PUT");
					ClientUI.clientController.accept(updateFacility);
					successMessageText.setVisible(true);
					successMessageText.setText("Facility Threshold Updated!");
					return;
				}
				errorMessageText.setVisible(true);
				errorMessageText.setText("Facility Does not exist");
					
				
			}
		}
    	
    	
    	}

    @FXML
    void back(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/AreaManagerInterface.fxml");
    }

    @FXML
    void close(ActionEvent event) {
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
							int FacilityID = (int) values[0];
							String FacilityArea = (String) values[1];
							String FacilityLocation = (String) values[2];
							String FacilityName = (String) values[3];
							int FacilityThreshholdLevel = (int) values[4];
							//boolean FacilityEK = (boolean) values[5];
							Facility facility = new Facility(FacilityID, FacilityArea, FacilityLocation, FacilityName, FacilityThreshholdLevel,true);
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
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		userAreaName = ClientUI.clientController.getUser().getArea();
		facilityNameText.setText(String.format("%s Area", userAreaName));
		
	}

}
