package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EKrutInstallController implements Initializable,IController {
	ArrayList<String> arrayListLocation = new ArrayList<>();
	
	ObservableList<String> Arealist;
	ObservableList<String> Locationlist;
	ObservableList<Facility> Facilitylist;
	
	ToggleGroup tg = new ToggleGroup();
    @FXML
    private RadioButton EKRadio;
//knnhjnj
    @FXML
    private RadioButton OLRadio;

    @FXML
    private HBox EKOptions;

    @FXML
    private VBox EKArea;

    @FXML
    private ComboBox<String> ComboBoxArea;

    @FXML
    private VBox EKLocation;

    @FXML
    private ComboBox<String> ComboBoxLocation;

    @FXML
    private VBox EKMachine;

    @FXML
    private ComboBox<Facility> ComboBoxFacility11;
    
    @FXML
    private Label labelError;
    
    @FXML
    private Button BtnInstall;

    
	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		System.out.println("EKrutInstallController");
	}
	
    @FXML
    void buttonInstall(ActionEvent event) 
    {
    	File firstInstallDir, firstInstalltion;
    	firstInstallDir = new File(ClientUI.clientController.ApplicationConfig);
    	firstInstalltion = new File(ClientUI.clientController.ApplicationConfig + "config.cfg");
    	Boolean Iscreated;
    	
    	if(((RadioButton)tg.getSelectedToggle()).equals(OLRadio))
    	{
        	try {
        		firstInstallDir.mkdir();
    			Iscreated = firstInstalltion.createNewFile();
    			
    			ClientUI.clientController.getEKFacility().setFacilityEK(false);
    			
    			if(Iscreated)
    			{
    				FileOutputStream fos = new FileOutputStream(firstInstalltion,false);
    				String configSettings = "OL";
    				byte[] byteOutput = (configSettings.toString()).getBytes();
    				fos.write(byteOutput);
    				fos.close();
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			System.out.println("Here IOException ?");
    			e.printStackTrace();
    		}
        	catch(Exception e)
        	{
    			System.out.println("Here Exception ?");
    			e.printStackTrace();
        	}
    		
    	}
    	else
    	{
    		System.out.println("Here by mistake ?");
    		if(ComboBoxFacility11.getValue() == null || ComboBoxLocation.getValue() == null || ComboBoxArea.getValue() == null )
    			return;
    		
    		try {
    			firstInstallDir.mkdir();
    			Iscreated = firstInstalltion.createNewFile();
    			
    			if(Iscreated)
    			{
    				FileOutputStream fos = new FileOutputStream(firstInstalltion,false);
    				
    				ClientUI.clientController.setEKFacility(ComboBoxFacility11.getValue());
    				ClientUI.clientController.getEKFacility().setFacilityEK(true);
    				
    				Facility selectedFacility = ClientUI.clientController.getEKFacility();
    				StringBuilder configSettings = new StringBuilder();
    				if(((RadioButton)tg.getSelectedToggle()).equals(EKRadio))
    				{
    					configSettings.append("EK" + "#");
    					configSettings.append(selectedFacility.getFacilityID() + "#");
    					configSettings.append(selectedFacility.getFacilityArea() + "#");
    					configSettings.append(selectedFacility.getFacilityLocation() + "#");
    					configSettings.append(selectedFacility.getFacilityName() + "#");
    					configSettings.append(selectedFacility.getFacilityThresholder() + "#");
    				}
    				else
    				{
    					configSettings.append("OL" + "#");
    				}
    				byte[] byteOutput = (configSettings.toString()).getBytes();
    				fos.write(byteOutput);
    				fos.close();
    				
    				RequestObjectClient request = new RequestObjectClient("#FACILITY_EKUPDATE",String.format("%s#", ClientUI.clientController.getEKFacility().getFacilityID()),"PUT");    	
    				ClientUI.clientController.accept(request);
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			
    			e.printStackTrace();
    		}
    	}
    	ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml", event);	
    }
    
    @FXML
    void UpdateMachine(ActionEvent event) {
    	if(arrayListLocation.isEmpty())
    		return;
    	ComboBoxFacility11.setVisible(true);
    	
		Facilitylist = FXCollections.observableArrayList(ClientUI.clientController.getArrFacility().stream()
				.filter(fac -> (ComboBoxArea.getValue().equals(fac.getFacilityArea()) && ComboBoxLocation.getValue().equals(fac.getFacilityLocation()) && !fac.isFacilityEK() ) )
				.collect(Collectors.toList()));
		
		if(Facilitylist.isEmpty())
		{
			ComboBoxFacility11.setVisible(false);
			labelError.setVisible(true);
		}
		ComboBoxFacility11.setItems(Facilitylist);
    }

    @FXML
    void UpdatedArea(ActionEvent event) {
		arrayListLocation.clear();
		
		ComboBoxLocation.setVisible(false);
		ComboBoxFacility11.setVisible(false);
		
		ComboBoxLocation.getItems().clear();
		ComboBoxFacility11.getItems().clear();
		for(Facility tempfacility : ClientUI.clientController.getArrFacility())
		{
			if(!tempfacility.getFacilityArea().equals(ComboBoxArea.getValue()))
				continue;
			arrayListLocation.add(tempfacility.getFacilityLocation());
		}
		if(!arrayListLocation.isEmpty())
		{
			ComboBoxLocation.setItems(FXCollections.observableArrayList(arrayListLocation));
			ComboBoxLocation.setVisible(true);
		}
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		OLRadio.setToggleGroup(tg);
		EKRadio.setToggleGroup(tg);
		
		labelError.setVisible(false);
		EKOptions.setVisible(false);
		ComboBoxLocation.setVisible(false);
		ComboBoxFacility11.setVisible(false);
		
		tg.selectedToggleProperty().addListener((obs, lastToggle, currentToggle) -> {
			RadioButton currentT = (RadioButton) tg.getSelectedToggle();
			if(currentT.equals(EKRadio))
			{
				EKOptions.setVisible(true);
			}
			else
			{
				EKOptions.setVisible(false);
			}
		});
		
		Arealist = FXCollections.observableArrayList(Arrays.asList("North","South","UAE"));
		ComboBoxArea.setItems(Arealist);
	}

}
