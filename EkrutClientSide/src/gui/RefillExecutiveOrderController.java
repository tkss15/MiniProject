package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.ProductInFacility;
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
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class RefillExecutiveOrderController implements Initializable, IController {
	
	String userAreaName;
	private String ID;
	private String Location;
	boolean exists = false;
	boolean existsItems = false;
	private ArrayList<Facility> Facilities;
	private ArrayList<ProductInFacility> ItemsInFacility;
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;
	private ArrayList<String> items;
	
	ObservableList<String> IDList;
	ObservableList<String> LocationList;
	ObservableList<String> NameList;
	ObservableList<String> ItemsList;
	
    @FXML
    private Text facilityNameText;

    @FXML
    private Button backButton;

    @FXML
    private Button CloseButton;

    @FXML
    private Text errorMessageText;

    @FXML
    private Button sendExecutiveOrderButton;

    @FXML
    private Text successMessageText;

    @FXML
    private ComboBox<String> LocationCombo;

    @FXML
    private ComboBox<String> NameCombo;
    
    @FXML
    private ComboBox<String> ItemCombo;

    @FXML
    private ListView<String> itemsListView;

    @FXML
    void sendExecutiveOrder(ActionEvent event) {

    }
    
    @FXML
    void selectLocation(ActionEvent event) {
    	successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		ArrayList<String> names = new ArrayList<>();
		NameCombo.getItems().clear();
		for (int i = 0; i < Facilities.size(); i++) 
		{
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityLocation().equals(LocationCombo.getValue())) {
				names.add(currFac.getFacilityName());
			}
		}
			ObservableList<String> namesList = FXCollections.observableArrayList(names);
			if (namesList.isEmpty()) {
				errorMessageText.setText("No Facilities in the Location!");
				errorMessageText.setVisible(true);
				return;
			}
			NameCombo.getItems().addAll(namesList);
			NameCombo.setVisible(true); //select Name
    }
    
    @FXML
    void selectItem(ActionEvent event) {
    	
    }
    
    
    @FXML
    void selectName(ActionEvent event) {
    	int facilityID = -1;
    	for (int i = 0; i < Facilities.size(); i++) 
		{
			Facility currFac = Facilities.get(i);
			if (currFac.getFacilityName().equals(NameCombo.getValue())) {
				facilityID = currFac.getFacilityID();
			}
		}
    	RequestObjectClient getItems = new RequestObjectClient("#GET_FACILITY",
				String.format("table=productsinfacility#condition=FacilityID=%d", facilityID), "GET");
		ClientUI.clientController.accept(getItems);
    }
    

    @FXML
    void back(ActionEvent event) {
    	ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
    }

    @FXML
    void close(ActionEvent event) {
    	if(ClientUI.clientController.getUser().getOnlineStatus() == null)
		{
			System.out.println("Not updated");
		}
		if(ClientUI.clientController.getUser().getOnlineStatus().equals("Online"))
		{
	    	RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",String.format("table=users#condition=userName=%s#values=userOnline=\"Offline\"", ClientUI.clientController.getUser().getUserName()),"PUT");    
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
			case "#GET_ITEMS":
				ItemsInFacility = new ArrayList<>();
				if (serverResponse.Responsedata.size() != 0) {
					existsItems = true;
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						Integer ProductCode = (Integer) values[0];
						Integer ProductAmount = (Integer) values[1];
						Integer FacilityID = (Integer) values[2];
						ProductInFacility item = new ProductInFacility(ProductCode, ProductAmount, FacilityID);
						ItemsInFacility.add(item);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		successMessageText.setVisible(false);
		errorMessageText.setVisible(false);
		
		ClientUI.clientController.setController(this);
		
		Facilities = new ArrayList<>();
		
		userAreaName = ClientUI.clientController.getUser().getArea();
		
		facilityNameText.setText(String.format("%s Area", userAreaName));
		
		if(userAreaName.equals("All")) {
			facilityNameText.setText("All Areas");
			RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
					String.format("table=facilities", userAreaName), "GET");
			ClientUI.clientController.accept(getFacilities);
		}else {
			RequestObjectClient getFacilities = new RequestObjectClient("#GET_FACILITY",
					String.format("table=facilities#condition=FacilityArea=%s", userAreaName), "GET");
			ClientUI.clientController.accept(getFacilities);
		}
		
		
		arrayLocation = new ArrayList<>();
		arrayName = new ArrayList<>();
		
		System.out.println(Facilities);
		
		for (int i = 0; i < Facilities.size(); i++)
		{
			Facility currFac = Facilities.get(i);
			arrayLocation.add(currFac.getFacilityLocation());
			arrayName.add(currFac.getFacilityName());
		}
		LocationList = FXCollections.observableArrayList(arrayLocation);
		NameList = FXCollections.observableArrayList(arrayName);
		
		LocationCombo.setItems(LocationList);
		NameCombo.setItems(NameList);
		LocationCombo.setVisible(true);
		NameCombo.setVisible(true);
	}

}
