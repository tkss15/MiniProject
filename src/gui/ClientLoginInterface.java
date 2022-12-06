package gui;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientLoginInterface {
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

	private double offset_x;

	private double offset_y;

	@FXML
	void ConnectToDB(ActionEvent event) throws Exception {
		String ipaddress = textFiledIP.getText();

		((Node) event.getSource()).getScene().getWindow().hide();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ClientInterface.fxml"));
		Parent root = loader.load();

		ClientInterfaceController studentFormController = loader.getController();
		ClientUI.ConnectToServer(ipaddress, studentFormController);
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		scene.setOnMousePressed(eventC -> {
			offset_x = eventC.getSceneX();
			offset_y = eventC.getSceneY();
		});

		scene.setOnMouseDragged(eventD -> {
			primaryStage.setX(eventD.getScreenX() - offset_x);
			primaryStage.setY(eventD.getScreenY() - offset_y);
		});
		//setAppIcon(primaryStage);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void setAppIcon(Stage primaryStage) {
		primaryStage.setTitle("EKrut System");
		primaryStage.getIcons().add(new Image("\\pictures\\LogoEKRUT.png"));
	}

	@FXML
	void exitFromLogin(ActionEvent event) {
		System.exit(0);
	}

}
