package common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
	private Map<String,Scene> BackList = new HashMap<>();
	public void ShowScene(String urlResources)
	{	
		Scene scene;
        loader = new FXMLLoader(getClass().getResource(urlResources));
        Parent root;
		try {
			root = loader.load();
			Stage stage = new Stage();
	        scene = new Scene(root);
	        if(BackList.containsKey(urlResources))
	        {
	        	scene = BackList.get(urlResources);
	        }
			SceneMovable(scene, stage);
			
			BackList.put(urlResources, scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 
	 * @param event
	 * @param urlResources
	 */
	public void SceneBack(ActionEvent event, String urlResources )
	{
		if(BackList.isEmpty())
			return;
		((Node) event.getSource()).getScene().getWindow().hide();
		ShowScene(urlResources);
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
