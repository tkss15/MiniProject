package common;

import java.io.IOException;
import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneManager 
{
	private double offset_x;
	private double offset_y;
	private FXMLLoader loader;
	private Stack<String> BackList = new Stack<String>();
	
	public void ShowScene(String urlResources) throws IOException
	{	
		BackList.push(urlResources);
        loader = new FXMLLoader(getClass().getResource(urlResources));
        Parent root = loader.load();
        Scene scene = new Scene(root);
		Stage stage = new Stage();
		SceneMovable(scene, stage);
		
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}
	
	public void SceneBack(ActionEvent event)
	{
		if(BackList.isEmpty())
			return;
		((Node) event.getSource()).getScene().getWindow().hide();
		try {
			ShowScene(BackList.pop());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public FXMLLoader getLoader()
	{
		return loader;
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
