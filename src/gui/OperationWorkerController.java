package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Entity.DeliveryOrder;
import common.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class OperationWorkerController implements Initializable{

    @FXML
    private Button CloseButton;

    @FXML
    private Text IDNumberLabel;

    @FXML
    private Text emailLabel;

    @FXML
    private Text firstNameLabel;

    @FXML
    private Text greetingNameLabel;

    @FXML
    private Text lastNameLabel;

    @FXML
    private Text locationLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Text phoneNumberLabel;

    @FXML
    private TableView<DeliveryOrder> table;
    
	private SceneManager sceneManager;
    
	@FXML
    void closeWindow(ActionEvent event) {
		//fix BUGS
		System.exit(0);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sceneManager = new SceneManager();
	}

}
