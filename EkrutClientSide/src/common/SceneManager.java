package common;

import java.io.IOException;
import java.net.URL;
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

/***
 * 
 * @author eldad
 *This code defines a SceneManager class that is used to manage different scenes in a JavaFX application. 
 *The class has several methods that can be used to show different scenes
 *
 */
public class SceneManager 
{
	
	// Each scene has x and y offsets that are used in order to make the scenes movable.
	private double offset_x;
	private double offset_y;
	
	private FXMLLoader loader; 
	// In order to hide scenes from client anywhere in the project we saving the stage and scene as attributes.
	private Stage stage;
	private Scene scene;
	
	// this map saves all the URL's of different scenes we have in the project each URL connected with a string.
	private Map<String,Scene> BackList = new HashMap<>();
	
	public SceneManager() {}
	
	/***
	 * @param urlResources - is the URL string to different scenes
	 * ShowPopup is a function that will show a screen on the user application.
	 * @important ShowPopup have no controller setup like other functions.
	 */
	public void ShowPopup(String urlResources) // Method to show a new popup window
	{	
	    URL url = this.getClass().getResource("/src"+urlResources.substring(2)); 
        FXMLLoader loader = new FXMLLoader(url);
        
        loader = new FXMLLoader(getClass().getResource(urlResources)); // Create a new FXMLLoader with the specified resource URL
        Parent root;
		try {
			root = loader.load(); // Load the FXML file
			stage = new Stage(); // Create a new stage
			
	        scene = new Scene(root); // Create a new scene with the loaded root
			SceneMovable(scene, stage); // Make the scene movable
			
			BackList.put(urlResources, scene); // Add the scene to the BackList
			stage.initStyle(StageStyle.UNDECORATED); // Set the stage style to undecorated
			stage.setScene(scene); // Set the scene to the stage
			stage.show(); // Show the stage
		} catch (IOException e) {
			// Handle IOException
			e.printStackTrace();
		}
	}
	/***
	 * @param urlResources - is the URL string to different scenes
	 * ShowSceneNew is a function that will show a new screen on the user Application
	 * 
	 * @question What is the difference between ShowSceneNew and @ShowScene ?
	 * @anwser ShowSceneNew will show a new stage and scene to the client. while ShowScene will search if the stage or scene have already been created and will reshow them.
	 */
	public void ShowSceneNew(String urlResources)
	{	
	    URL url = this.getClass().getResource("/src"+urlResources.substring(2)); 
        FXMLLoader loader = new FXMLLoader(url);
        Parent root;
		try {
			root = loader.load();// Load the FXML file
			ClientUI.clientController.setController(loader.getController());// Sets the Client Current Controller to be the loaded FXML file controller.
			stage = new Stage();
			
	        scene = new Scene(root); // Create a new scene with the loaded root
			SceneMovable(scene, stage);// Make the scene movable
			
			BackList.put(urlResources, scene); // Add the scene to the BackList
			stage.initStyle(StageStyle.UNDECORATED); // Set the stage style to undecorated
			stage.setScene(scene); // Set the scene to the stage
			stage.show(); // Show the stage
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 
	 * @param urlResources - is the URL string to different scenes
	 * ShowScene will show a scene on the User application if the scene have already been created in the past( which means the user already used it )
	 * it will show the used stage and scene that are saved in the @BackList.
	 * 
	 */
	public void ShowScene(String urlResources)
	{	
	    URL url = this.getClass().getResource("/src"+urlResources.substring(2)); 
        FXMLLoader loader = new FXMLLoader(url);
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
	
	/***
	 * @param urlResources  - is the URL string to different scenes
	 * @param event - an event that will help to hide current window.
	 * 
	 * an overloaded method of function ShowScene with the option of hiding current window to show a new window.
	 */
	public void ShowScene(String urlResources, Event event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		ShowScene(urlResources);
	}
	/***
	 * @param urlResources  - is the URL string to different scenes
	 * @param event - an event that will help to hide current window.
	 * 
	 * an overloaded method of function ShowSceneNew with the option of hiding current window to show a new window.
	 */
	public void ShowSceneNew(String urlResources, Event event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		ShowSceneNew(urlResources);
	}
	/***
	 * 
	 * @param urlResources  - is a string which holds the URL of a wanted hide screen
	 * @param event - an event that will help to hide current window.
	 * Sceneback will hide the scene which in @urlResources. this function is necessary if we need to hide windows without knowing if they already hidden
	 */
	public void SceneBack(Event event, String urlResources )
	{
		if(BackList.isEmpty() || !BackList.containsKey(urlResources))
			return;
		Scene scene = BackList.get(urlResources);// Gets the matching stage from the Map
		scene.getWindow().hide();// hides it.
	}
	/***
	 * 
	 * @return returns the FXMLLoader 
	 */
	public FXMLLoader getLoader()
	{
		return loader;
	}
	
	/***
	 * SceneMovable is a function which allows a scene and stage to be movable around the user screen.
	 * @param scene
	 * @param stage
	 */
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
