package gui;

import java.io.File;
import java.io.FileReader;

import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientLoginInterface implements IController {
	private RequestObjectClient request;
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
		
//		request = new RequestObjectClient("USER_RETURN_DATA","table=users#condition=userName=tkss15","GET");    	
//		ClientUI.clientController.accept(request);
		
    	
    	System.out.println("Here ?");
		File firstInstalltion = new File(ClientUI.clientController.ApplicationConfig + "config.cfg");
		System.out.println(firstInstalltion.getAbsolutePath());
		if(firstInstalltion.exists())
		{
			FileReader reader = new FileReader(firstInstalltion);
	        char[] chars = new char[(int) firstInstalltion.length()];
	        reader.read(chars);
	        String configSettings = new String(chars);
	        reader.close();
	        String[] arraySettings = configSettings.split("#");
	        System.out.println(arraySettings.length);
	        //EK 3 UAE Kirat-ata Rogozin School 45 
	        if(arraySettings[0].equals("OL"))
	        	ClientUI.clientController.getEKFacility().setFacilityEK(false);
	        else
	        {
	        	ClientUI.clientController.getEKFacility().setFacilityEK(true);
	        	ClientUI.clientController.getEKFacility().setFacilityID(Integer.valueOf(arraySettings[1]));
	        	ClientUI.clientController.getEKFacility().setFacilityArea(arraySettings[2]);
	        	ClientUI.clientController.getEKFacility().setFacilityLocation(arraySettings[3]);
	        	ClientUI.clientController.getEKFacility().setFacilityName(arraySettings[4]);
	        	ClientUI.clientController.getEKFacility().setFacilityThresholder(Integer.valueOf(arraySettings[5]));
	        }

	        ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml");		
			return;
		}
    	RequestObjectClient request = new RequestObjectClient("#FIRST_INSTALL",String.format("table=facilities"),"GET");    	
    	ClientUI.clientController.accept(request);
    	System.out.println("Here 2");
		ClientUI.sceneManager.ShowScene("../views/EKrutInstall.fxml");
		
		
	}


	@FXML
	void exitFromLogin(ActionEvent event) {
		System.exit(0);
	}


	@Override
	public void updatedata(Object data) {
	}

}
