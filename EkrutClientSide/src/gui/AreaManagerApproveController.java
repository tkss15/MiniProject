package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class AreaManagerApproveController implements Initializable, IController {

    @FXML
    private Text textUserlogin;

    @FXML
    private Text textFirstName;

    @FXML
    private Text textLastName;

    @FXML
    private Text textID;

    @FXML
    private Text textTelephone;

    @FXML
    private Text textEmail;

    @FXML
    private HBox textBranch;

    @FXML
    private Button Back;

    @FXML
    private Button CloseButton;

    @FXML
    private TableView<?> ApproveTable;

    @FXML
    private TableColumn<?, ?> IDColumn;

    @FXML
    private TableColumn<?, ?> NameColumn;

    @FXML
    private TableColumn<?, ?> RegistrationTypeColumn;

    @FXML
    private TableColumn<?, ?> StatusColumn;

    @FXML
    private Button changesBTN;

    @FXML
    void Back(ActionEvent event) {
    	ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
    }

    @FXML
    void applyChangesToTable(ActionEvent event) {

    }

    @FXML
    void closeWindow(ActionEvent event) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
		textUserlogin.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
	}

}
