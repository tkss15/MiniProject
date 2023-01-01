package gui;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.Order;
import Entity.Product;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
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

public class CatalogViewController implements Initializable, IController
{
	private final String TrashcanImage = "trashcan.png";
	private final String PlusImage = "Plus.png";
	private final String MinusImage = "Minus.png";
	ArrayList<ProductUI> StockItems = new ArrayList<>();
	ShoppingCartUI myShoppingCart = new ShoppingCartUI(null,null,null);
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
    	ClientUI.sceneManager.SceneBack(event, "/gui/Homepage.fxml");
    }
    @FXML
    void closeWindow(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void printElements(ActionEvent event) 
    {
    	System.out.println("d");
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		//ClientUI.clientController.setController(this);
		ScrollPane = new ScrollPane();

//		ProductUI Cola = new ProductUI(new Product(1,"Coke Cola Zero","350 mill coke zero glass", "cola.png", 7.5));
//		ProductUI Bisli = new ProductUI(new Product(2,"Cruch Chips Original","Classic Potato Chips flavor", "chips.png", 12.90));
//		ProductUI Bisli2 = new ProductUI(new Product(3,"Cruch Chips Original2","Classic Potato Chips flavor", "chips.png", 12.90));
		
		for(int i = 0; i < ClientUI.clientController.getClientOrder().myCart.size(); i ++)
		{	
			ProductUI productSample = new ProductUI(ClientUI.clientController.getClientOrder().myCart.get(i));
			StockItems.add(productSample);
		}
		ShoppingCart.setSpacing(10.0);
		ScrollPane.setVbarPolicy(ScrollPane.getVbarPolicy().ALWAYS);
		
		ProductsVBox.getChildren().clear();
		HBox RowItems = new HBox();
		int i = 0;
		for(i = 0; i < StockItems.size(); i++)
		{
			if( i % 2 == 0 )
			{
				RowItems.setSpacing(20);
				ProductsVBox.getChildren().add(RowItems);
				RowItems = new HBox();
				VBox.setMargin(RowItems, new Insets(20,0,0,0));
			}
			RowItems.getChildren().add((StockItems.get(i)).ProductVBox());
		}
		if(i % 2 == 1)
			ProductsVBox.getChildren().add(RowItems);
		
		//HBox Items = new HBox(Cola.ProductVBox(),Item);
		//HBox.setMargin(Items, new Insets(30));
		ProductsVBox.setFillWidth(true);
		ScrollPane.setContent(ProductsVBox);
		ScrollPane.setFitToWidth(true);
		myShoppingCart.ShowCart();
	}
	class ShoppingCartUI extends Order
	{
		public ShoppingCartUI(Facility orderFacility, String orderType, String FacilityType) {
			super(orderFacility, orderType, FacilityType);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void addItem(Product product)
		{
			super.addItem(product);
			ShowCart();
		}
		@Override
		public void removeItem(Product product)
		{
			super.removeItem(product);
			ShowCart();
		}
		
		public void ShowCart()
		{
			ShoppingCart.getChildren().clear();
			VBox.setVgrow(ShoppingCart, Priority.ALWAYS);
			for(Product product : myCart)
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
				product.setAmount(product.getProductAmount() + 1);
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
				product.setAmount(product.getProductAmount() - 1);
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
			
			Text price = new Text("12.90");
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
			
			gridAnchor.getChildren().addAll(VBoxItemDescrption, AddToCartHBox);
			return gridAnchor;
		}
		/**
		 * @return
		 */
		private HBox ProductButtons()
		{
			HBox AddToCartHBox = new HBox();
			Label AmountDesc = new Label();
			TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
		    
			AddToCartHBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

			AddBtn = new Button("Add");
			AddBtn.setId("AddButton");
			AddBtn.setMnemonicParsing(false);
			AddBtn.setOnAction(event -> myShoppingCart.addItem(new Product(Product)));
			
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
				Product.setAmount(Integer.valueOf(newV));
			});
			HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
			
			AmountDesc.setText("יח'");
			HBox.setMargin(AmountDesc, new Insets(5.0, 5.0, 5.0, 5.0));
			
			AddToCartHBox.getChildren().addAll(AddBtn, MinusBtn, AmountDesc, Amount, PlusButton);
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
			
			Text ItemPrice = new Text((Product.getProductPrice() + "₪")); 
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
		// TODO Auto-generated method stub
		
	}
/*
 * 
 * SELECT products.*, productsinfacility.ProductAmount
FROM products
LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode
ORDER BY products.ProductCode;
 * */
}

