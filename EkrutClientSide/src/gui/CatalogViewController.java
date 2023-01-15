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

	private final String ShekelCode = new String("$");// Sadly the Shekel UTF Code didnt work so its dollar now.
	
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
	    	/***
	    	 * Since we canceling the order we need to cancel the Countdown running currently.
	    	 * further more we need to clear the client cart since the client left the Catalog viewer.
	    	 */
	    	ClientUI.clientController.getTaskCountdown().cancelTask();
	        ClientUI.clientController.getClientOrder().myCart.clear();
	        ClientUI.clientController.setClientOrder(new Order(null,null,null));
	        ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);	    	  
	    } 
	}
	@FXML 
	void ShowPrevPage(ActionEvent event) // Method called when the user clicks the "Back" button
	{
		ClientUI.clientController.getTaskCountdown().cancelTask();// We stopping the countdown of the client and clearnig his cart.
	    ClientUI.clientController.getClientOrder().myCart.clear();
	    ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
	}
	@FXML
	void closeWindow(ActionEvent event) // Method called when the user clicks the "Close" button
	{
	    // Close the application and inform the server that the user has disconnected
		ClientUI.clientController.getTaskCountdown().cancelTask();// we need to stop the countdown in order before we exit.
	    ClientUI.clientController.UserDisconnected(true);
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
	    // Here we stop the contdown and continue to the next page. we will restart it on the next page.
	    ClientUI.clientController.getTaskCountdown().cancelTask();
	    // Otherwise, go to the order invoice scene
	    ClientUI.sceneManager.ShowSceneNew("../views/OrderInvoice.fxml", event);
	}
    
	/***
	 * initialize starts when controller is being first load. in this case there are things we want to change in order to get
	 * the catalog viewer ready.
	 * 
	 * 1. We need to load all the products inside the current looking catalog viewer. Moreover when we open the catalog viewer
	 * we looking at products inside a facility ( no matter if its ek or ol) and the client can choose products out of the available options 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		ClientUI.clientController.setController(this);// Setting the client Controller to be this current controller. this is necessary since we need to send data from there and listen to data 
		
		/***
		 * geTaskCountdown - starts a the countdown of the order termination each time we move from different scenes we stop the count down and re starts it on the different scene
		 * it must start here.
		 */
		ClientUI.clientController.getTaskCountdown().initialize(timerOrder);
		
		/***
		 * @SalesMap - is a Hash map with Integer values as keys and String values as the Map values.
		 * the hashmap contains product codes as is keys and in the values contains the Name of the Sale.
		 * 
		 * @StockItems - is an ArrayList that contains @ProductUI items. StockItems contains dynamic UI from all the products
		 * inside the current facility.
		 * - More information about @ProductUI down over the class of ProductUI.
		 * 
		 * @MyShoppingCart - is an instance of the class @ShoppingCartUI this class will show the client current Shopping cart
		 * it will change dynamically the client UI.  
		 */
		SalesMap = new HashMap<>();
		StockItems = new ArrayList<>();
		myShoppingCart = new ShoppingCartUI();
		/***
		 #SIMPLE_REQUEST asks the server for the current products inside the faclity and 
		 recives all the products information inside the @updatedata
		 * 
		 */
    	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",
    			String.format("%d#", ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID()),"*");  
    	ClientUI.clientController.accept(request);
    	
    	/***
    	 * Sales are only avaliable to subscribers so if the client isnt subscriber we dont show him the sales.
    	 * we using a design patter called Strategy which helps us to change products prices dynmaicly its easy to manipulate 
    	 * and recreate for next sales.
    	 */
    	if(((RegisterClient)ClientUI.clientController.getUser()).getClientStatus() == RegisterClient.ClientStatus.CLIENT_SUBSCRIBER)
    	{
    		// In order to see current live sales we need to get all sales in the same area.
			request = new RequestObjectClient("#GET_ALL_SALES_CLIENT_VIEWER",String.format("%s#", ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityArea()),"GET");  
	    	ClientUI.clientController.accept(request);
	    	
	    	// Looping through all the products in the current faculity
	    	for(int i = 0; i < ClientUI.clientController.getArrProducts().size(); i++)
	    	{
	    		// If the product have a sale going on then we continue
	    		if(SalesMap.containsKey(ClientUI.clientController.getArrProducts().get(i).getProductCode()))
	    		{
	    			// Switch case between all possible sales.
	    			switch(SalesMap.get(ClientUI.clientController.getArrProducts().get(i).getProductCode()))
	    			{
	    			
	    			/***
	    			 * Currently Sales available as part of code are
	    			 * 1 + 1 -> which is using the @PriceStartegyOnePlusOne where it calculates every second item as free to the client
	    			 * Custom Discount -> which is using the @PriceStartegyCustom where it calculates every single item with a custom discount received.
	    			 * Second Item in Half Price -> which is using the @PriceStartegySecondHalfPrice where it caculates the price of every second item as half of the original price.
	    			 * default: caculates all sales that starts with "10% discount" "20% discount" ... etc. and will caculate the discount of it with @PriceStartegyCustom
	    			 */
		    			case"1 + 1":ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegyOnePlusOne());
		    			break;
		    			
		    			case"Second Item In Half Price":ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegySecondHalfPrice());
		    			break;
		    			
		    			default:	
		    			{
							/***
							 * a sale will get here only if it will look like "10% discount","20% discount"... etc. 
							 * then we need to take the perecent value and caculate it in order to use @PriceStartegyCustom
							 */
		    				String key = SalesMap.get(ClientUI.clientController.getArrProducts().get(i).getProductCode());
		    				Integer discount = Integer.valueOf(key.substring(0, 2));
		    				double perecnt = discount/100.0;// Caculating the given discount out of the values
		    				
		    				ClientUI.clientController.getArrProducts().get(i).setPriceStategy(new PriceStartegyCustom(perecnt));
		    				break;
		    			}
	    			}
	    		}
	    	}
    	}
		
    	/***
    	 * UI changes.
    	 * we need to display dynamic items inside the UI then we adding the products inside the current facility
    	 * ( ClientUI.clientController.getArrProducts() )
    	 * 
    	 * for each available product inside our available facility products we want to add it to the UI.
    	 * How it looks:
    	 * we have a @ScrollPane which contains all products. every 2 products are being show in a row.
    	 * for that we using a @HBox that contains every 2 products.
    	 * for each product we set all his information in a @VBox
    	 */
		ScrollPane = new ScrollPane();// Creates the Scrollpane that will contain all the products. it must be scrollpane 
		// if the facility have alot of products we will show them with a scroll option.
		
		/***
		 * Looping through all the available products inside the current facility and creates for each one of the products a ProductUI
		 * my advise is to read the @ProductUI and come back later after reading it.
		 */
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
	/***
	 * ProductUI is a nested class inside this controller which only belongs to the catalogviewer( and maybe catalogviewerOnly)
	 * 
	 * this nested class will create all the UI for a given product. this class have 3 methods
	 * 
	 * @ProductDescription - this method creates a @VBox that contains all the product information.
	 * for each product there will be a  product name, product description product picture, product qunatity amount, product price
	 * if product is on sale it will add label saying the product is on sale and the product sale description.
	 * at the end the method will return the created @VBox
	 * 
	 * @ProductButtons - this method creates a @HBox that contains buttons in order to add the product to the cart. 
	 * at the end the method will return the created @HBox
	 * 
	 * @ProductVBox - this method merges both @ProductDescription and @ProductButtons into one and puts it inside a @VBox
	 *
	 */
	class ProductUI 
	{
		Product Product;// Each ProductUI shows a single product information. we must have the product inside the ProductUI
		Button AddBtn, PlusButton, MinusBtn;
		StringProperty AvailableAmount = new SimpleStringProperty();
		
		/***
		 * Constructor creates a new ProductUI. the constructor gets a product.
		 * @param product
		 */
		public ProductUI(Product product) 
		{
			this.Product = product;
		}
		
		/***
		 * ProductVBox is a method that calls 2 other method @ProductDescription and @ProductButtons 
		 * this method creates the VBox dynamicly changed on the product.
		 * @return a fully created VBox that contains the product information and buttons
		 */
		public VBox ProductVBox()
		{
			VBox gridAnchor = new VBox();
			gridAnchor.setId("gridAnchor");
			gridAnchor.setPrefHeight(300.0);
			gridAnchor.setPrefWidth(300.0);		
			gridAnchor.setPadding(new Insets(5,5,5,5));
			
			VBox VBoxItemDescrption = ProductDescription();
			HBox AddToCartHBox = ProductButtons();
			
			// If product is on sale We need to add onSale label on the top of the VBox 
			if(!(Product.getPriceStategy() instanceof PriceStartegyRegular))
			{
				Label onSale = new Label("On Sale");
				onSale.setId("OnSale");
				gridAnchor.getChildren().add(onSale);
			}
			
			gridAnchor.getChildren().addAll(VBoxItemDescrption, AddToCartHBox);
			return gridAnchor;
		}
		/**
		 * ProductButtons
		 * creates an HBox that contains 3 buttons and 1 text field
		 * 
		 * @AddBtn(button) - this button will add the amount listed inside the @Amount text field if the amount is greater then 
		 * the quantity available inside a pop up error message will appear on screen. if there is already product inside the 
		 * Shopping cart then it will re add to it.
		 * 
		 * @MinusBtn(button) - this button will change the amount listed inside the @Amount text field and decreases it by 1.
		 *  if the amount is lower then 1 then a pop up error message will appear on screen.
		 * 
		 * @PlusBtn(button) - this button will change the amount listed inside the @Amount text field and increase it by 1.
		 *  if the amount is higher then the amount inside the product facility it will show an error message.
		 * 
		 * @Amount(text field) - contains the amount the client choose to add to his Shopping cart. if the client wants he can change the amount
		 * with the buttons or just write it. if the clients enters a value which is larger then the qunatity number or a value which is not a number
		 * it will show an error message.
		 * @return
		 */
		private HBox ProductButtons()
		{
			HBox AddToCartHBox = new HBox();
			// Sets the amount to be the value of product amount which saves the wanted amount to add.
			TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
		    
			AddToCartHBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);// Styling the Buttons to be from left to right.

			// AddBtn
			AddBtn = new Button("Add");// creates a new button with the name of Add
			AddBtn.setId("AddButton");
			AddBtn.setMnemonicParsing(false);
			
			// If user clicks on the Add button this is the action that will trigger.
			AddBtn.setOnAction(event -> 
			{
				// If the the product wanted amount is larger then the amount inside the facility
				// we show an error message
				if(Product.getProductAmount()  > Product.getMaxAmount())
				{
					Alert alert = new Alert(AlertType.ERROR, "Error No enough quntity in the Facility");
					alert.showAndWait();
					return;
				}
				// If product is already inside the cart and we re-add the same product to the cart want to check that the given amount is not larger
				// then the amount of the product inside the facility.
				if(ClientUI.clientController.getClientOrder().myCart.contains(Product))
				{
					int IndeOfProduct = ClientUI.clientController.getClientOrder().myCart.indexOf(Product);// Since is an arrayList we need to find the index.
					Product cartProduct = ClientUI.clientController.getClientOrder().myCart.get(IndeOfProduct);// Gets the current product with the index.
					
					// checks if the amount inside the shopping cart and the amount inside the ui is not larger then the product facility amount
					if(cartProduct.getProductAmount() + Product.getProductAmount()  > Product.getMaxAmount()) 
					{
						Alert alert = new Alert(AlertType.ERROR, "Error No enough quntity in the Facility");
						alert.showAndWait();
						return;
					}
				}
				// adding a new Item to the Shopping cart. Note we cant add the same Item we must use the New key word
				myShoppingCart.addItem(new Product(Product));
			});
			// Styling the button addbtn
			HBox.setHgrow(AddBtn, Priority.ALWAYS);
			HBox.setMargin(AddBtn, new Insets(0, 10.0, 0, 0));		
			
			// MinusBtn
			MinusBtn = new Button("-");// Sets the button label name
			MinusBtn.setId("MinusButton");// Adding Styles to the Button itself
			MinusBtn.setMnemonicParsing(false);
			
			// If user clicks on the the Minus button this is the action that will trigger.
			MinusBtn.setOnAction(event -> 
			{
				// Checks if the UI current amount of product is equals or below one then we need to show an error message.
				if(Product.getProductAmount() <= 1)
				{
					Alert alert = new Alert(AlertType.ERROR, "Error minumum amount must be above 1 ");
					alert.showAndWait();
					return;
				}
				Product.setAmount(Product.getProductAmount() - 1);// Changes the Amount listed inside the Product
				Amount.setText(Integer.toString(Product.getProductAmount()));// we change the Text field amount to be the current amount
			});
			HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));// Styles

			// Plus Button
			PlusButton = new Button("+");
			
			PlusButton.setId("PlusButton");// Adding Styles
			PlusButton.setMnemonicParsing(false);
			
			// If user clicks on the the Plus button this is the action that will trigger.
			PlusButton.setOnAction(event -> 
			{
				Product.setAmount(Product.getProductAmount() + 1);// increases the product qunatity amount by 1.
				Amount.setText(Integer.toString(Product.getProductAmount()));
			});
			HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
			
			// Styling the Amount text Field
			Amount.setAlignment(Pos.CENTER);
			Amount.setPrefHeight(25.0);
			Amount.setPrefWidth(58.0);
			// Every time the Amount text field changes the lambda function triggers
			Amount.textProperty().addListener((obs,oldV,newV) -> {
				// the Function here triggers everytime the Amount text field changes
				// the Function have 3 params that it gets obs, oldV, newV. 
				// oldV - contains the last value before the changed value
				// newV - contains the new value after the change.
				
				if(!newV.matches("^(?:[1-9]|\\d\\d\\d*)$"))// this regex checks if the new value is number
				{
					// if the new value isnt a number we will display an erorr message
					Alert alert = new Alert(AlertType.ERROR, "Error minumum amount must be above 1 or an integer");
					alert.showAndWait();
					Amount.setText(oldV);// and we need to reset the value to be the old value. 
					return;
				}
				Integer intAmount = Integer.valueOf(newV);// gets the Amount inside the new value
				if(Product.getMaxAmount()  < intAmount)// if the Amount is greater then the amount inside the products in facility.
				{
					// We update the product ui to contains the maximum amount of the facility.
					
				    Alert alert = new Alert(Alert.AlertType.INFORMATION);
				    alert.setTitle("Information");
				    alert.setHeaderText("Updated cart!");
				    alert.setContentText(String.format("Shopping cart updated with maximum amount of %s", Product.getProductName()));
				    alert.showAndWait();
				    Amount.setText(String.valueOf(Product.getMaxAmount()));
				    return;
				}
				Product.setAmount(Integer.valueOf(newV));// Sets the product amount to be the new inserted value
			});
			HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));// Styles
			
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
		 * 				Product Amount
		 * 				Product Description
		 * 				Price
		 */
		private VBox ProductDescription()
		{
			VBox VBoxItemDescrption = new VBox();
			
			VBoxItemDescrption.setId("ItemDescription");
			VBox.setVgrow(VBoxItemDescrption,Priority.ALWAYS);
			
			// Creates the product Image And Styles
			ImageView ItemPicture = CreateImage(Product.getPathPicture(),150.0,150.0,true,true, null);
			VBox.setVgrow(ItemPicture, Priority.NEVER);
			
			// Creates the Product name And Styles
			Text ItemName = new Text(Product.getProductName());
			ItemName.setId("ProductName");
			VBox.setVgrow(ItemName, Priority.ALWAYS);
			
			// Creates the Product Price
			Text ItemPrice = new Text((Product.getProductPrice() + ShekelCode)); 
			ItemPrice.setId("ProductName");
			
			
			//Creates the Product Description and Styles
			Text ItemDescription = new Text(Product.getProductDescription());
			ItemDescription.setId("ProductName");

			/***
			 * Explantion about AmountAvailable is a text that writes the current amount of available products in the facility
			 * this text will change live if products are being added \ removed from the facility deepends on other users.
			 * this change ( the live change ) happens since we using the function @AmountAvailable.textProperty().bind(AvailableAmount);
			 * that allows us to change it out of the scope of javafx and using threads.
			 */
			Text AmountAvailable = new Text();
			AvailableAmount.setValue("("+ Product.getMaxAmount() + " Available)");
			AmountAvailable.textProperty().bind(AvailableAmount);
			AmountAvailable.textProperty().addListener((ob, oldValue,newValue) -> {
				// In case the Amount changes ( means there was an update) we will remove the product from the client if and only if its greater then
				// the amount in the current facility
				if(ClientUI.clientController.getClientOrder().myCart.contains(Product))
				{
					int IndexOfProduct = ClientUI.clientController.getClientOrder().myCart.indexOf(Product);
					if(ClientUI.clientController.getClientOrder().myCart.get(IndexOfProduct).getProductAmount() > Product.getMaxAmount())
						myShoppingCart.removeItem(Product);
				}
			});
			ItemPrice.setId("ProductName");
			
			VBoxItemDescrption.getChildren().addAll(ItemPicture,ItemName,AmountAvailable,ItemDescription);// adding all the elements except the price.
			// In case the product have a sale on we will add the sale title.
			if(!(Product.getPriceStategy() instanceof PriceStartegyRegular))
			{	
				Text ItemSale = new Text(Product.getPriceStategy().toString()); 
				ItemSale.setId("SaleTitle");
				VBoxItemDescrption.getChildren().add(ItemSale);
			}
			VBoxItemDescrption.setAlignment(Pos.CENTER);// styling
			VBoxItemDescrption.getChildren().add(ItemPrice);// adding the price to the vbox childern
			return VBoxItemDescrption;
		}
	}
	
	/***
	 * 
	 * @param url - the URL to the picture. url contains the location where the photo located
	 * @param fitHeight - Sets the height of the picture to be a value if the height given is 0 or below then it sets the default amount of the picture
	 * @param fitWidth - sets the width of the picture to be a value if the width given is 0 or below then it sets the default amount of the picture
	 * @param PickOnBounds - Styling for the photo
	 * @param PresarveRatio - is a boolean that if its true it will retain the Ratio of the original photo even after changing is height and width
	 * @param cursor - if we want to change the cursor when hovering on the picture we can set it inside this
	 * CreateImage will return an ImageView with given params this function will return a Styled Image view with the given parramters
	 * @return
	 */
	private ImageView CreateImage(String url, Double fitHeight, Double fitWidth, boolean PickOnBounds, boolean PresarveRatio, Cursor cursor )
	{
		// Creates a new ImageView with the given url
		ImageView ItemPicture = new ImageView(url);
		
		if(fitHeight > 0)// this checks if there is a change to the fit if not it will use the default height of the Image
			ItemPicture.setFitHeight(fitHeight);
		if(fitWidth > 0)// same as before checks if there was a change to the fitwidth
			ItemPicture.setFitWidth(fitWidth);
		if(cursor != null)// this checks if a new cursor have inserted if a null is been inserted then we wont change the cursor
			ItemPicture.setCursor(cursor);
		
		ItemPicture.setPickOnBounds(PickOnBounds);
		ItemPicture.setPreserveRatio(PresarveRatio);
		
		return ItemPicture;
	}
	/***
	 * @data - will contain data recived from the server. this data will be saved on the current controller or on the Clineconsole.
	 * @importent we wont manipulate the data in the javafx UI from there since there will be a thread issue. we will always save the data
	 * and use it with in the controller itself.
	 */
	public void updatedata(Object data) 
	{
		// TODO Auto-generated method stub
		if(data instanceof ResponseObject)// Safety Check to know if the data type is ResponseObject
		{
			ResponseObject serverResponse = (ResponseObject) data;
			// Checking which Request type we get
			switch(serverResponse.getRequest())
			{
				case"#GET_ALL_SALES_CLIENT_VIEWER":
				{// In case we checking the sales for this current facility
					// Loading all Sales
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values = (Object[])serverResponse.Responsedata.get(i);
						String SaleType = (String) values[1];
						Integer ProductSale = (Integer)values[0];
						SalesMap.put(ProductSale, SaleType);// Adding sales to the hashmap
					}
					break;
				}
				case"#UPDATE_PRODUCTS_CLIENT":
				{// In case the Quantity of products changes in the middle of the buy
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						// We will get data for each item inside the facility.
						Object[] values = (Object[])serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						Integer ProductCode = (Integer)values[1];
						Integer ProductAmount = (Integer)values[2];
						// first of all if the current client not in the same facility as the data we wont check.
						if(ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() != FacilityID)
							return;
						// then we need to find the facility products and change them
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
						try 
						{
							fos = new FileOutputStream(anotherProduct.PicturePhoto);
							BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
							
							bos.write(arrayByte, 0, arrayByte.length); /* Write byte array to output stream */
						    bos.flush();
						    fos.flush();
						    
						    fos.close();
						    bos.close();
						} 
						catch (IOException e) 
						{
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

