package gui;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.Order;
import Entity.Product;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import common.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CatalogViewOnlyController implements Initializable, IController
{
	private final String ShekelCode = new String("\u20AA".getBytes(), StandardCharsets.UTF_8);

	ArrayList<ProductUI> StockItems = new ArrayList<>();
    @FXML
    private VBox Item, ProductsVBox, ShoppingCart;
    @FXML
    private VBox ItemSample;
    @FXML
    private ScrollPane ScrollPane, ScrollPaneCart;
    @FXML
    private HBox RowItems;
    @FXML
    private Button GetOrder;
    
    @FXML
    private Button BtnBack;

    @FXML 
    void ShowPrevPage(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
    }
    @FXML
    void closeWindow(ActionEvent event) {
    	System.exit(0);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		ClientUI.clientController.setController(this);
		
    	RequestObjectClient request = new RequestObjectClient("#GET_ALL_PRODUCTS","table=products","GET");  
    	ClientUI.clientController.accept(request);
    	
		ScrollPane = new ScrollPane();
		ScrollPane.setVbarPolicy(ScrollPane.getVbarPolicy().ALWAYS);
		
		ProductsVBox.getChildren().clear();
		HBox RowItems = new HBox();
		int i;
		for(i = 0; i <= StockItems.size(); i++)
		{
			if(i % 3 == 0)
			{
				RowItems.setSpacing(20);
				ProductsVBox.getChildren().add(RowItems);
				RowItems = new HBox();
				VBox.setMargin(RowItems, new Insets(20,0,0,0));
			}
			if(i == StockItems.size())
				break;
			RowItems.getChildren().add(StockItems.get(i).ProductVBox());
		}
		if(i % 3 == 1 || i % 3 == 2)
		{
			VBox.setMargin(RowItems, new Insets(20,0,50,0));
			ProductsVBox.getChildren().add(RowItems);
		}
		
		ProductsVBox.setFillWidth(true);
		ScrollPane.setContent(ProductsVBox);
		ScrollPane.setFitToWidth(true);
	}
	class ProductUI 
	{
		Product Product;
		
		public ProductUI(Product product) 
		{
			this.Product = product;
		}
		
		public VBox ProductVBox()
		{
			VBox gridAnchor = new VBox();
			gridAnchor.setId("gridAnchor");
			gridAnchor.setPadding(new Insets(5,5,5,5));
			HBox.setMargin(gridAnchor, new Insets(0,0,0,20));
			VBox VBoxItemDescrption = ProductDescription();

			
			gridAnchor.getChildren().addAll(VBoxItemDescrption);
			return gridAnchor;
		}
		
		private VBox ProductDescription()
		{
			VBox VBoxItemDescrption = new VBox();
			VBoxItemDescrption.setId("ItemDescription");
			VBox.setVgrow(VBoxItemDescrption,Priority.ALWAYS);
			
			ImageView ItemPicture = CreateImage(Product.getPathPicture(),150.0,150.0,true,true, null);
			VBox.setVgrow(ItemPicture, Priority.NEVER);

			Text ItemName = new Text(Product.getProductName());
			ItemName.setId("ProductName");
			VBox.setVgrow(ItemName, Priority.ALWAYS);
			
			Text ItemDescription = new Text(Product.getProductDescription());
			ItemDescription.setId("ProductName");
			
			Text ItemPrice = new Text((Product.getProductPrice() + ShekelCode)); 
			ItemPrice.setId("ProductName");
			
			VBoxItemDescrption.getChildren().addAll(ItemPicture,ItemName,ItemDescription,ItemPrice);
			return VBoxItemDescrption;
		}
	}
	
	private ImageView CreateImage(String url, Double fitHeight, Double fitWidth, boolean PickOnBounds, boolean PresarveRatio, Cursor cursor )
	{
		ImageView ItemPicture = new ImageView(url);
		
		if(fitHeight > 0)
			ItemPicture.setFitHeight(fitHeight);
		if(fitWidth > 0)
			ItemPicture.setFitWidth(fitWidth);
		if(cursor != null)
			ItemPicture.setCursor(cursor);
		
		ItemPicture.setPickOnBounds(PickOnBounds);
		ItemPicture.setPreserveRatio(PresarveRatio);
		
		return ItemPicture;
	}
	@Override
	public void updatedata(Object data) {
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#GET_ALL_PRODUCTS":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);//Row 1 
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductSrc = (String) values[4];
						
						
						byte[] arrayByte = serverResponse.ResponsePicture.get(i);// Picture's
						Product anotherProduct = new Product(ProductCode,ProductName,ProductDesc, ProductSrc, ProductPrice, 1);
						FileOutputStream fos;
						try {
							fos = new FileOutputStream(anotherProduct.PicturePhoto);
							BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
							
							bos.write(arrayByte, 0, arrayByte.length); /* Write byte array to output stream */
							System.out.println(anotherProduct.getPathPicture());
							bos.flush();
							fos.flush();
							
							fos.close();
							bos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /* Create file output stream */
						StockItems.add(new ProductUI(anotherProduct));
					}
					break;
				}
			}
			
		}
	}
/*
 * 
 * SELECT products.*, productsinfacility.ProductAmount
FROM products
LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode
ORDER BY products.ProductCode;
 * */
}

