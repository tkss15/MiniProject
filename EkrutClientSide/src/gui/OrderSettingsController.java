package gui;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entity.Facility;
import Entity.Product;
import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class OrderSettingsController implements Initializable, IController {

	ToggleGroup tg = new ToggleGroup();
	ArrayList<Facility> arrFacility = new ArrayList<>();
	ObservableList<Facility> list;
	
	@FXML
	private ComboBox<Facility> ComboboxFacility;
    @FXML
    private Button CloseButton;
    @FXML
    private ImageView Logo;

  
    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    private ImageView Logo221;
    
    
    @FXML
    private Button BackButton;

    @FXML
    private ImageView Logo1;

    @FXML
    private ImageView Logo11;
    
    @FXML
    private Button CloseButton1;

    @FXML
    private RadioButton radioDelivery;

    @FXML
    private RadioButton radioPickup;
    
    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }
    
    @FXML
    void actionCreateOrder(ActionEvent event)
    {
    	if(ComboboxFacility.getValue() != null)
    	{
        	
        	ClientUI.clientController.getClientOrder().setFacilityType("OL");
        	ClientUI.clientController.getClientOrder().setOrderFacility(ComboboxFacility.getValue());
        	ClientUI.clientController.getClientOrder().setOrderType(((RadioButton)tg.getSelectedToggle()).getText());
        	String sql = "SELECT products.*, productsinfacility.ProductAmount FROM products LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode WHERE productsinfacility.FacilityID = " + ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() + " ORDER BY products.ProductCode";
        	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",sql,"*");  
        	ClientUI.clientController.accept(request);
        	ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);
    	}
    }
    @FXML
    void BackAction(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		radioDelivery.setToggleGroup(tg);
		radioPickup.setToggleGroup(tg);
		for(Facility f : ClientUI.clientController.arrFacility)
		{
			System.out.println(f);
		}
		//System.out.println(ClientUI.clientController.getUser().getArea());
		list = FXCollections.observableArrayList(ClientUI.clientController.arrFacility.stream()
				.filter(fac -> (fac.getFacilityArea().equals(ClientUI.clientController.getUser().getArea())) )
				.collect(Collectors.toList()));
		//list = FXCollections.observableArrayList(ClientUI.clientController.arrFacility);
		ComboboxFacility.setItems(list);
	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		System.out.println("OrderSettingsController");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#SIMPLE_REQUEST":
				{
					ClientUI.clientController.getArrProducts().clear();
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);//Row 1 
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductSrc = (String) values[4];
						Integer ProductAmount = (Integer) values[6];
						
						if(ProductAmount == 0)
							continue;
						
						byte[] arrayByte = serverResponse.ResponsePicture.get(i);// Picture's
						Product anotherProduct = new Product(ProductCode,ProductName,ProductDesc, ProductSrc, ProductPrice, ProductAmount);
						if(anotherProduct.PicturePhoto.exists())
						{
							(ClientUI.clientController.getArrProducts()).add(anotherProduct);
							continue;
						}
						FileOutputStream fos;
						try {
							fos = new FileOutputStream(anotherProduct.PicturePhoto);
							BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
							
							bos.write(arrayByte, 0, arrayByte.length); /* Write byte array to output stream */
							System.out.println(anotherProduct.getPathPicture());
						    bos.flush();
						    fos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /* Create file output stream */
						(ClientUI.clientController.getArrProducts()).add(anotherProduct);
					}
					break;
				}
			}
		}
	}

}
