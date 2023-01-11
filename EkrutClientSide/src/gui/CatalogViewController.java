package gui;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Order;
import Entity.PriceStartegyCustom;
import Entity.PriceStartegyOnePlusOne;
import Entity.PriceStartegyRegular;
import Entity.PriceStartegySecondHalfPrice;
import Entity.Product;
import Entity.RegisterClient;
import client.ClientUI;
import common.CountdownOrder;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class CatalogViewController implements Initializable, IController
{
	// Private fields for storing images and data about products and the shopping cart
	private final String TrashcanImage = "trashcan.png"; // Image for deleting items from the shopping cart
	private final String PlusImage = "Plus.png"; // Image for increasing the quantity of a product in the shopping cart
	private final String MinusImage = "Minus.png"; // Image for decreasing the quantity of a product in the shopping cart

	private final String ShekelCode = new String("\u20AA".getBytes(), StandardCharsets.UTF_8);
	
	private HashMap<Integer,String> SalesMap; // Map to store active sales data
	private ArrayList<ProductUI> StockItems; // List to store product data
	private ShoppingCartUI myShoppingCart; // Object to store shopping cart data
    @FXML
    private Label timerOrder;
    
	// JavaFX elements for the layout and UI of the scene
	@FXML
	private VBox Item, ProductsVBox, ShoppingCart, ItemSample;
	@FXML
	private ScrollPane ScrollPane, ScrollPaneCart;
	@FXML
	private HBox RowItems;
	@FXML
	private Button GetOrder, BtnBack;

  	
	@FXML 
	void CancelOrder(ActionEvent event) // Method called when the user clicks the "Cancel Order" button
	{
	    // Display a confirmation alert to the user
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    alert.setHeaderText("Are you sure you want to cancel your Order?");
	    alert.setContentText("This action cannot be undone. Please confirm your choice.");
	    Optional<ButtonType> result = alert.showAndWait();
	    // If the user confirms, clear the shopping cart and return to the homepage
	    if (result.get() == ButtonType.OK) 
	    {
	    	ClientUI.clientController.getTaskCountdown().cancelTask();
	        ClientUI.clientController.getClientOrder().myCart.clear();
	        ClientUI.clientController.setClientOrder(new Order(null,null,null));
	        ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);	    	  
	    } 
	}
	@FXML 
	void ShowPrevPage(ActionEvent event) // Method called when the user clicks the "Back" button
	{
	    // Return to the homepage without clearing the shopping cart
		ClientUI.clientController.getTaskCountdown().cancelTask();
	    ClientUI.clientController.getClientOrder().myCart.clear();
	    ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
	}
	@FXML
	void closeWindow(ActionEvent event) // Method called when the user clicks the "Close" button
	{
	    // Close the application and inform the server that the user has disconnected
		ClientUI.clientController.getTaskCountdown().cancelTask();
	    ClientUI.clientController.UserDissconnected();
	    System.exit(0);
	}

	@FXML
	void printElements(ActionEvent event) // Method called when the user clicks the "Get Order" button
	{
	    // If the shopping cart is empty, display an error message and return
	    if(ClientUI.clientController.getClientOrder().myCart.isEmpty())
	    {
	        Alert alert = new Alert(AlertType.ERROR, "In order to continue you must insert 1 product");
	        alert.showAndWait();
	        return;
	    }
	    ClientUI.clientController.getTaskCountdown().cancelTask();
	    // Otherwise, go to the order invoice scene
	    ClientUI.sceneManager.ShowSceneNew("../views/OrderInvoice.fxml", event);
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		ClientUI.clientController.setController(this);
		
		ClientUI.clientController.getTaskCountdown().initialize(timerOrder);
		
		SalesMap = new HashMap<>();
		StockItems = new ArrayList<>();
		myShoppingCart = new ShoppingCartUI();
		
    	String sql = "SELECT products.*, productsinfacility.ProductAmount FROM products LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode WHERE productsinfacility.FacilityID = " + ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() + " ORDER BY products.ProductCode";
    	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",sql,"*");  
    	ClientUI.clientController.accept(request);
    	// 
    	
    	if(((RegisterClient)ClientUI.clientController.getUser()).getClientStatus() == RegisterClient.ClientStatus.CLIENT_SUBSCRIBER)
    	{
			request = new RequestObjectClient("#GET_ALL_SALES",String.format("table=sales#values=saleType=saleType&Item=Item#condition=area=%s&isActive=1", ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityArea()),"GET");  
	    	ClientUI.clientController.accept(request);
	    	
	    	for(int i = 0; i < ClientUI.clientController.getArrProducts().size(); i++)
	    	{
	    		if(SalesMap.containsKey(ClientUI.clientController.getArrProducts().get(i).getProductCode()))
	    		{
	    			switch(SalesMap.get(ClientUI.clientController.getArrProducts().get(i).getProductCode()))
	    			{
		    			case"1 + 1":ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegyOnePlusOne());
		    			break;
		    			
		    			case"Custom Discount":ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegyCustom(0.25));
		    			break;
		    			
		    			case"Second Item In Half Price":ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegySecondHalfPrice());
		    			break;
	    			}
	    		}
	    	}
    	}
    	// 
		
		ScrollPane = new ScrollPane();
		for(int i = 0; i < ClientUI.clientController.getArrProducts().size(); i ++)
		{	
			ProductUI productSample = new ProductUI(ClientUI.clientController.getArrProducts().get(i));
			StockItems.add(productSample);
		}
		ShoppingCart.setSpacing(10.0);
		ScrollPane.setVbarPolicy(ScrollPane.getVbarPolicy().ALWAYS);
		
		ProductsVBox.getChildren().clear();
		HBox RowItems = new HBox();
		int i;
		for(i = 0; i <= StockItems.size(); i++)
		{
			if(i % 2 == 0)
			{
				RowItems.setSpacing(20);
				ProductsVBox.getChildren().add(RowItems);
				RowItems = new HBox();
				VBox.setMargin(RowItems, new Insets(20,30,0,10));
			}
			if(i == StockItems.size())
				break;
			RowItems.getChildren().add(StockItems.get(i).ProductVBox());
		}
		if(i % 2 == 1)
		{
			VBox.setMargin(RowItems, new Insets(20,0,50,0));			
			ProductsVBox.getChildren().add(RowItems);
		}
		
		ProductsVBox.setFillWidth(true);
		ScrollPane.setContent(ProductsVBox);
		ScrollPane.setFitToWidth(true);
		myShoppingCart.ShowCart();
	}
	class ShoppingCartUI
	{
		public ShoppingCartUI() {
			// TODO Auto-generated constructor stub
		}
		
		public void addItem(Product product)
		{
			ClientUI.clientController.getClientOrder().addItem(product);
			ShowCart();
		}
	
		public void removeItem(Product product)
		{
			ClientUI.clientController.getClientOrder().removeItem(product);
			ShowCart();
		}
		
		public void ShowCart()
		{
			ShoppingCart.getChildren().clear();
			VBox.setVgrow(ShoppingCart, Priority.ALWAYS);
			for(Product product : ClientUI.clientController.getClientOrder().myCart)
			{
				ShoppingCart.getChildren().add(ProductInCart(product));
			}
		}
		public HBox ProductInCart(Product product)
		{
			HBox ProductInCart = new HBox();
			ProductInCart.setId("ShoppingCartHBox");
			HBox.setHgrow(ProductInCart, Priority.ALWAYS);
			HBox ProductDescription = new HBox();
			ImageView ItemPicture = new ImageView();
			
			ItemPicture.setFitHeight(80);
			ItemPicture.setFitWidth(80);
			ItemPicture.setPickOnBounds(true);
			ItemPicture.setPreserveRatio(true);
			VBox.setVgrow(ItemPicture, Priority.ALWAYS);
			
			Image image = new Image(product.getPathPicture());
			ItemPicture.setImage(image);
			
			VBox ProductNameAndButtons =  new VBox();
			Text ProductName = new Text(product.getProductName());
			ProductName.setId("ProductName");
			
			HBox ProductButtons = new HBox();
			
			VBox ProductAmount = new VBox();
			Text ProductAmountText = new Text("Amount");
			
			TextField textFieldAmount = new TextField(Integer.toString(product.getProductAmount()));
			textFieldAmount.setPrefWidth(34);
			textFieldAmount.setPrefHeight(25);
			ProductAmount.getChildren().addAll(ProductAmountText, textFieldAmount);
			
			ImageView PlusBtn = CreateImage(PlusImage,28.0,22.0,true,true, Cursor.HAND);
			PlusBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				if(product.getProductAmount() + 1 > product.getMaxAmount())
				{
					Alert alert = new Alert(AlertType.ERROR, "Error Currently Avaliable only" + product.getMaxAmount());
					alert.showAndWait();
					return;
				}
				ClientUI.clientController.getClientOrder().UpdateItem(product, product.getProductAmount() + 1);
				textFieldAmount.setText(Integer.toString(product.getProductAmount()));
				event.consume();
			});	
			
			ImageView MinusBtn = CreateImage(MinusImage,34.0,34.0,true,true, Cursor.HAND);
			
			MinusBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				if(product.getProductAmount() <= 1)
				{
					removeItem(product);
					return;					
				}
				ClientUI.clientController.getClientOrder().UpdateItem(product, product.getProductAmount() - 1);
				textFieldAmount.setText(Integer.toString(product.getProductAmount()));
				event.consume();				
			});
			
			ImageView Trashbtn = CreateImage(TrashcanImage,27.0,27.0,true,true, Cursor.HAND);
			Trashbtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				removeItem(product);
				event.consume();
			});
			
			ProductAmount.setAlignment(Pos.BOTTOM_LEFT);
			ProductButtons.getChildren().addAll(ProductAmount, PlusBtn, MinusBtn,Trashbtn);
			
			ProductButtons.setAlignment(Pos.BOTTOM_LEFT);
			ProductNameAndButtons.getChildren().addAll(ProductName, ProductButtons);
			
			ProductNameAndButtons.setPadding(new Insets(5));
			HBox.setHgrow(ProductButtons, Priority.ALWAYS);
			
			ProductDescription.getChildren().addAll(ItemPicture,ProductNameAndButtons);
			HBox.setHgrow(ProductDescription, Priority.ALWAYS);
			
			Text price = new Text(String.format("%.2f", product.getProductPrice()));
			price.setId("ProductName");
			VBox Prices = new VBox(price);
			Prices.setAlignment(Pos.CENTER);
			ProductInCart.getChildren().addAll(ProductDescription,Prices);
			
			return ProductInCart;
		}
	}
	class ProductUI 
	{
		Product Product;
		Button AddBtn, PlusButton, MinusBtn;
		StringProperty AvailableAmount = new SimpleStringProperty();
		
		public ProductUI(Product product) 
		{
			this.Product = product;
		}
		
		public VBox ProductVBox()
		{
			VBox gridAnchor = new VBox();
			gridAnchor.setId("gridAnchor");
			gridAnchor.setPrefHeight(300.0);
			gridAnchor.setPrefWidth(300.0);		
			gridAnchor.setPadding(new Insets(5,5,5,5));
			VBox VBoxItemDescrption = ProductDescription();
			HBox AddToCartHBox = ProductButtons();
			
			if(!(Product.getPriceStategy() instanceof PriceStartegyRegular))
			{
				System.out.println("Hey this is on Sale");
				Label onSale = new Label("On Sale");
				onSale.setId("OnSale");
				gridAnchor.getChildren().add(onSale);
			}
			
			gridAnchor.getChildren().addAll(VBoxItemDescrption, AddToCartHBox);
			return gridAnchor;
		}
		/**
		 * @return
		 */
		private HBox ProductButtons()
		{
			HBox AddToCartHBox = new HBox();
			TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
		    
			AddToCartHBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

			AddBtn = new Button("Add");
			AddBtn.setId("AddButton");
			AddBtn.setMnemonicParsing(false);
			AddBtn.setOnAction(event -> {
				if(Product.getProductAmount()  > Product.getMaxAmount())
				{
					Alert alert = new Alert(AlertType.ERROR, "Error No enough quntity in the Facility");
					alert.showAndWait();
					return;
				}
				if(ClientUI.clientController.getClientOrder().myCart.contains(Product))
				{
					int IndeOfProduct = ClientUI.clientController.getClientOrder().myCart.indexOf(Product);
					Product cartProduct = ClientUI.clientController.getClientOrder().myCart.get(IndeOfProduct);
					if(cartProduct.getProductAmount() + Product.getProductAmount()  > Product.getMaxAmount()) 
					{
						Alert alert = new Alert(AlertType.ERROR, "Error No enough quntity in the Facility");
						alert.showAndWait();
						return;
					}
				}
				myShoppingCart.addItem(new Product(Product));
			});
			
			HBox.setHgrow(AddBtn, Priority.ALWAYS);
			HBox.setMargin(AddBtn, new Insets(0, 10.0, 0, 0));		
			
			MinusBtn = new Button("-");
			MinusBtn.setId("MinusButton");
			MinusBtn.setMnemonicParsing(false);
			MinusBtn.setOnAction(event -> {
				if(Product.getProductAmount() <= 1)
				{
					Alert alert = new Alert(AlertType.ERROR, "Error minumum amount must be above 1 ");
					alert.showAndWait();
					return;
				}
				Product.setAmount(Product.getProductAmount() - 1);
				Amount.setText(Integer.toString(Product.getProductAmount()));
			});
			HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));

			PlusButton = new Button("+");
			PlusButton.setId("PlusButton");
			PlusButton.setMnemonicParsing(false);
			PlusButton.setOnAction(event -> {
				Product.setAmount(Product.getProductAmount() + 1);
				Amount.setText(Integer.toString(Product.getProductAmount()));
			});
			HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
			
			Amount.setAlignment(Pos.CENTER);
			Amount.setPrefHeight(25.0);
			Amount.setPrefWidth(58.0);
			Amount.textProperty().addListener((obs,oldV,newV) -> {
				if(!newV.matches("^(?:[1-9]|\\d\\d\\d*)$"))
				{
					Alert alert = new Alert(AlertType.ERROR, "Error minumum amount must be above 1 or an integer");
					alert.showAndWait();
					Amount.setText(oldV);
					return;
				}
				Integer intAmount = Integer.valueOf(newV);
				if(Product.getMaxAmount()  < intAmount)
				{
				    Alert alert = new Alert(Alert.AlertType.INFORMATION);
				    alert.setTitle("Information");
				    alert.setHeaderText("Updated cart!");
				    alert.setContentText(String.format("Shopping cart updated with maximum amount of %s", Product.getProductName()));
				    alert.showAndWait();
				    Amount.setText(String.valueOf(Product.getMaxAmount()));
				    return;
				}
				Product.setAmount(Integer.valueOf(newV));
			});
			HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
			
			AddToCartHBox.getChildren().addAll(AddBtn, MinusBtn, Amount, PlusButton);
			HBox.setMargin(AddToCartHBox, new Insets(0, 5.0, 5.0, 5.0));
			return AddToCartHBox;
		}
		/***
		 * ProductDescription - 
		 * Function creates VBox with description about a Product. function will return a VBox contains all information of the product
		 * Structure: 
		 * 		-VBox:
		 * 				Image 
		 * 				Product Name
		 * 				Product Description
		 * 				Price
		 */
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
			
			Text AmountAvailable = new Text();
			AvailableAmount.setValue("("+ Product.getMaxAmount() + " Available)");
			AmountAvailable.textProperty().bind(AvailableAmount);
			AmountAvailable.textProperty().addListener((ob, oldValue,newValue) -> {
				if(ClientUI.clientController.getClientOrder().myCart.contains(Product))
				{
					myShoppingCart.removeItem(Product);
				}
			});
			ItemPrice.setId("ProductName");
			
			VBoxItemDescrption.getChildren().addAll(ItemPicture,ItemName,AmountAvailable,ItemDescription);
			if(!(Product.getPriceStategy() instanceof PriceStartegyRegular))
			{	
				Text ItemSale = new Text(Product.getPriceStategy().toString()); 
				ItemSale.setId("SaleTitle");
				VBoxItemDescrption.getChildren().add(ItemSale);
			}
			VBoxItemDescrption.setAlignment(Pos.CENTER);
			VBoxItemDescrption.getChildren().add(ItemPrice);
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
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		System.out.println("OrderSettingsController");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{
				case"#GET_ALL_SALES":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values = (Object[])serverResponse.Responsedata.get(i);
						String SaleType = (String) values[1];
						Integer ProductSale = (Integer)values[0];
						System.out.println(ProductSale + " " + SaleType);
						SalesMap.put(ProductSale, SaleType);
					}
					break;
				}
				case"#UPDATE_PRODUCTS_CLIENT":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values = (Object[])serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						Integer ProductCode = (Integer)values[1];
						Integer ProductAmount = (Integer)values[2];
						
						if(ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() != FacilityID)
							return;
						
						for(Product tempProduct : ClientUI.clientController.getArrProducts())
						{
							if(tempProduct.getProductCode() != ProductCode)
								continue;
							
							Integer ProductIndex = ClientUI.clientController.getArrProducts().indexOf(tempProduct);
							tempProduct.setMaxAmount(ProductAmount);
							
							ClientUI.clientController.getArrProducts().set(ProductIndex, tempProduct);
						}
					}
					Platform.runLater(() -> {
						for(ProductUI productTemp : StockItems)
						{							
							productTemp.AvailableAmount.setValue("("+ productTemp.Product.getMaxAmount() + " Available)");
						}
					});
					break;
				}
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
						    
						    fos.close();
						    bos.close();
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

