package common;

import java.io.IOException;

import gui.ServerInterfaceController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneManager 
{
	private double offset_x;
	private double offset_y;
	
	public void ShowScene(String urlResources) throws IOException
	{	
        FXMLLoader loader = new FXMLLoader(getClass().getResource(urlResources));
        Parent root = loader.load();
        Scene scene = new Scene(root);
		Stage stage = new Stage();
		SceneMovable(scene, stage);
		
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}
	
	private void SceneMovable(Scene scene, Stage stage)
	{
        scene.setOnMousePressed(event -> {
            offset_x = event.getSceneX();
            offset_y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
        	stage.setX(event.getScreenX() - offset_x);
        	stage.setY(event.getScreenY() - offset_y);
        });
	}
}
