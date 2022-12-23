package Server;

import gui.ServerInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CatalogViewerClient extends Application
{
	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	}
	
	private double offset_x;
	private double offset_y;
	
	/**
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
//		  SceneManager sceneManager = new SceneManager();
//		  sceneManager.ShowScene("/gui/LoginInterface.fxml");
//		  sercv
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CatalogViewer2.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setOnMousePressed(event -> {
            offset_x = event.getSceneX();
            offset_y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - offset_x);
            primaryStage.setY(event.getScreenY() - offset_y);
        });
        //setAppIcon(primaryStage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

	}
}
