package gui;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientLoginInterface implements IController {
	@FXML
	private Button UpdateButton;

	@FXML
	private ImageView ConnectLogo;
	@FXML
	private TextField textFiledIP;
	@FXML
	private ImageView Logo;

	@FXML
	private Button CloseButton;
	
	@FXML
	void ConnectToDB(ActionEvent event) throws Exception 
	{
		String ipaddress = textFiledIP.getText();

		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.ConnectToServer(ipaddress);
		ClientUI.clientController.setController(this);
    	
		File firstInstalltion = new File(ClientUI.clientController.ApplicationConfig + "config.cfg");
		System.out.println(firstInstalltion.getAbsolutePath());
		
		RequestObjectClient request = new RequestObjectClient("#FIRST_INSTALL","","GET");    	
		ClientUI.clientController.accept(request);
		
		if(firstInstalltion.exists())
		{
			FileReader reader = new FileReader(firstInstalltion);
	        char[] chars = new char[(int) firstInstalltion.length()];
	        reader.read(chars);
	        String configSettings = new String(chars);
	        reader.close();
	        String[] arraySettings = configSettings.split("#");
	        System.out.println(arraySettings.length);

	        if(arraySettings[0].equals("OL"))
	        	ClientUI.clientController.getEKFacility().setFacilityEK(false);
	        else
	        {
	        	ClientUI.clientController.setEKFacility(new Facility(Integer.valueOf(arraySettings[1]),arraySettings[2],arraySettings[3],arraySettings[4],Integer.valueOf(arraySettings[5]), true));
	        }

	        ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml");		
			return;
		}
    	System.out.println("Here 2");
		ClientUI.sceneManager.ShowScene("../views/EKrutInstall.fxml");
		
		
	}


	@FXML
	void exitFromLogin(ActionEvent event) {
		System.exit(0);
	}


	@Override
	public void updatedata(Object data) 
	{
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			
			switch(serverResponse.getRequest())
			{	
				case"#FIRST_INSTALL":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						String FacilityArea = (String)values[1];
						String FacilityLocation = (String)values[2];
						String FacilityName = (String)values[3];
						Integer FacilityThresholder = (Integer)values[4];
						Integer FacilityEK = (Integer) values[5];

						ClientUI.clientController.getArrFacility().add(new Facility(FacilityID,FacilityArea, FacilityLocation, FacilityName, FacilityThresholder, FacilityEK == 0 ? false : true
						));
					}					
					break;
				}
			}
		}
	}

}
