package common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
	private Stage stage;
	private Scene scene;
	private Map<String,Scene> BackList = new HashMap<>();
	
	public SceneManager()
	{
	}
	public void ShowSceneNew(String urlResources)
	{	
        loader = new FXMLLoader(getClass().getResource(urlResources));
        Parent root;
		try {
			root = loader.load();
			ClientUI.clientController.setController(loader.getController());
			stage = new Stage();
			
	        scene = new Scene(root);
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
	public void ShowScene(String urlResources)
	{	
        loader = new FXMLLoader(getClass().getResource(urlResources));
        Parent root;
		try {
			root = loader.load();
			ClientUI.clientController.setController(loader.getController());
			stage = new Stage();
			
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
	public void ShowScene(String urlResources, Event event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		ShowScene(urlResources);
	}
	public void ShowSceneNew(String urlResources, Event event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		ShowSceneNew(urlResources);
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
