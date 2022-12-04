package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientInterfaceController implements Initializable {

	private ClientUI clientUI;
	@FXML
	private TextField ClientIDTextField;

	@FXML
	private TextArea clientTextArea;

	@FXML
	private Button CloseButton;

	@FXML
	private ImageView ConnectLogo;

	@FXML
	private ImageView Logo;

	@FXML
	private Button SearchButton;

	@FXML
	private Button UpdateButton;

	@FXML
	private void closeWindow(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	private void updateDetails(ActionEvent event) {

	}

	@FXML
	private void searchUser(ActionEvent event) {
		String id = ClientIDTextField.getText();
		if (id.trim().isEmpty()) {
			System.out.println("You must enter an id number");
		}
		clientUI.clientController.accept(id);
	}

	@FXML
	public void writeToClientTextArea(Object message) {
		if (message instanceof HashMap) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> subscriber = (HashMap<String, String>) message;
			StringBuilder str = new StringBuilder();
			for (String s : subscriber.keySet()) {
				str.append(s + ": " + subscriber.get(s) + "\n");
			}
			this.clientTextArea.setText(str.toString());
		}
	}

	public TextField getClientIDTextField() {
		return ClientIDTextField;
	}

	public void setClientIDTextField(TextField clientIDTextField) {
		ClientIDTextField = clientIDTextField;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clientTextArea.setEditable(false);
	}

}
