package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ClientInterfaceController implements Initializable{
	
	private HashMap<String,String> data;
    @FXML
    private TextField ClientIDTextField;
    
    @FXML
    private Text IDNotFoundMSG;

	@FXML
    private Text missingIDField;
   
	@FXML
    private TextField textAreaFirstName;
    @FXML
    private TextField textAreaLastName;
    @FXML
    private TextField textAreaPhoneNumber;
    @FXML
    private TextField textAreaEmail;
    @FXML
    private TextField textAreacredit;
    @FXML
    private TextField textFieldCreditCardChange;
    @FXML
    private TextField textFieldSubNumberChange;
    @FXML
    private TextField textAreasub;
	@FXML
    private Button CloseButton;

    @FXML
    private ImageView ConnectLogo;
    @FXML
    private Pane DetailsUpdatePane;
    @FXML
    private Pane DataPane;
    @FXML
    private Pane DataViewPane;
    @FXML
    private ImageView Logo;

    @FXML
    private Button SearchButton;

    @FXML
    private Button UpdateButton;
    
    @FXML
    private Text updateError;


    @FXML
    private void closeWindow(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    private void updateDetails(ActionEvent event) {
    	if(!(textFieldCreditCardChange.isVisible() && textFieldSubNumberChange.isVisible()))
    	{
    		return;
    	}
    	else {
	    	updateError.setVisible(false);
	    	String Updatecreditcard = textFieldCreditCardChange.getText();
	    	String Updatesubnumber = textFieldSubNumberChange.getText();
	    	System.out.println(Updatecreditcard + "OK->" + Updatesubnumber);
	    	if(Updatecreditcard.trim().isEmpty() && Updatesubnumber.trim().isEmpty())
	    	{
	    		updateError.setVisible(true);
	    		System.out.println("You must atleast one of the options in order to update");
	    	}
	    	else
	    	{
	    		if(!Updatecreditcard.trim().equals(""))
	    		{
	    			data.put("Credit Card Number", Updatecreditcard);
	    		}
	    		if(!Updatesubnumber.trim().equals(""))
	    		{
	    			data.put("Subscriber Number", Updatesubnumber);
	    		}
	    		ClientUI.clientController.accept(data);
	    	}
    	}
    	searchUser(event);
    }
    
    @FXML
    private void searchUser(ActionEvent event) {
    	
    	String id = ClientIDTextField.getText();
    	if(id.trim().isEmpty())
		{
			System.out.println("You must enter an id number");
			DataViewPane.setVisible(false);
			missingIDField.setVisible(true);
			IDNotFoundMSG.setVisible(false);
		}else {
			missingIDField.setVisible(false);
			IDNotFoundMSG.setVisible(false);
			ClientUI.clientController.accept(id);
			
		}
    	
    }
    
    public void writeToClientTextArea(Object message) {
    	if(message instanceof HashMap) {
    		data = (HashMap<String,String>) message;
    		for(String key : data.keySet())
    		{
    			switch(key) {
					case"First Name":
						textAreaFirstName.setText(data.get(key));
						break;
					case"Last Name":
						textAreaLastName.setText(data.get(key));
						break;
					case"Phone Number":
						textAreaPhoneNumber.setText(data.get(key));
						break;
					case"Email Address":
						textAreaEmail.setText(data.get(key));
						break;
					case"Credit Card Number":
						textAreacredit.setText(data.get(key));
						break;
					case"Subscriber Number":
						textAreasub.setText(data.get(key));
						break;
    			}

    		}
    	}
	}


    public TextField getClientIDTextField() {
		return ClientIDTextField;
	}
	public void setClientIDTextField(TextField clientIDTextField) {
		ClientIDTextField = clientIDTextField;
	}
	
	public Text getIDNotFoundMSG() {
		return IDNotFoundMSG;
	}

	public void setIDNotFoundMSG(Text iDNotFoundMSG) {
		IDNotFoundMSG = iDNotFoundMSG;
	}
	
	public Text getMissingIDField() {
		return missingIDField;
	}

	public void setMissingIDField(Text missingIDField) {
		this.missingIDField = missingIDField;
	}
	
	public void setPanesAfterSearch(boolean setVisable)
	{
		this.DataViewPane.setVisible(setVisable);
		this.DetailsUpdatePane.setVisible(setVisable);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textAreaFirstName.setText("");
		DataViewPane.setVisible(false);
		updateError.setVisible(false);
		IDNotFoundMSG.setVisible(false);
		missingIDField.setVisible(false);
		DetailsUpdatePane.setVisible(false);
		textAreaFirstName.setEditable(false);
		textAreaLastName.setEditable(false);
		textAreaEmail.setEditable(false);
		textAreacredit.setEditable(false);
		textAreasub.setEditable(false);
		textAreaPhoneNumber.setEditable(false);
	}

}
