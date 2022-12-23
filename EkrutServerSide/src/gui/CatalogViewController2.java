package gui;
import javafx.scene.control.ScrollPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CatalogViewController2 implements Initializable 
{
//public class CatalogViewController {
	private final String TrashcanImage = "trashcan.png";
	private final String PlusImage = "Plus.png";
	private final String MinusImage = "Minus.png";
	ArrayList<Product> StockItems = new ArrayList<Product>();
	ShoppingCart myShoppingCart = new ShoppingCart();
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
    void closeWindow(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void printElements(ActionEvent event) {
    	System.out.println(myShoppingCart.myCart.toString());
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		ScrollPane = new ScrollPane();

		Product Cola = new Product("Coke Cola Zero","350 mill coke zero glass", "cola.png", 7.5);
		Product Bisli = new Product("Cruch Chips Original","Classic Potato Chips flavor", "chips.png", 12.90);
		Product Bisli2 = new Product("Cruch Chips Original2","Classic Potato Chips flavor", "chips.png", 12.90);
		
		StockItems.add(Cola);
		StockItems.add(Bisli);
		StockItems.add(Bisli2);
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
		
		HBox Items = new HBox(Cola.ProductVBox(),Item);
		HBox.setMargin(Items, new Insets(30));
		ProductsVBox.setFillWidth(true);
		ScrollPane.setContent(ProductsVBox);
		ScrollPane.setFitToWidth(true);
		myShoppingCart.ShowCart();
	}
	class ShoppingCart 
	{
		ArrayList<Product> myCart = new ArrayList<>();
		public void addItem(Product product)
		{
			if(myCart.contains(product))
			{
				int ProductIndex = myCart.indexOf(product);
				Product ProductUpdate = myCart.get(ProductIndex);
				ProductUpdate.setAmount(ProductUpdate.getAmount() + product.getAmount()); 
				myCart.set(ProductIndex, ProductUpdate);
			}
			else
				myCart.add(product);
			ShowCart();
		}
		public void removeItem(Product product)
		{
			if(myCart.contains(product))
				myCart.remove(product);
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
			
			Image image = new Image(product.ProductSrc);
			ItemPicture.setImage(image);
			
			VBox ProductNameAndButtons =  new VBox();
			Text ProductName = new Text(product.ProductName);
			ProductName.setId("ProductName");
			
			HBox ProductButtons = new HBox();
			
			VBox ProductAmount = new VBox();
			Text ProductAmountText = new Text("Amount");
			
			TextField textFieldAmount = new TextField(Integer.toString(product.getAmount()));
			textFieldAmount.setPrefWidth(34);
			textFieldAmount.setPrefHeight(25);
			ProductAmount.getChildren().addAll(ProductAmountText, textFieldAmount);
			
			ImageView PlusBtn = CreateImage(PlusImage,28.0,22.0,true,true, Cursor.HAND);
			PlusBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					product.setAmount(product.getAmount() + 1);
					textFieldAmount.setText(Integer.toString(product.getAmount()));
					event.consume();
				}
			});	
			
			ImageView MinusBtn = CreateImage(MinusImage,34.0,34.0,true,true, Cursor.HAND);
			MinusBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if(product.getAmount() <= 1)
					{
						removeItem(product);
						return;
					}
					product.setAmount(product.getAmount() - 1);
					textFieldAmount.setText(Integer.toString(product.getAmount()));
					event.consume();
				}
			});	
			
			ImageView Trashbtn = CreateImage(TrashcanImage,27.0,27.0,true,true, Cursor.HAND);
			
			Trashbtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					removeItem(product);
					event.consume();
				}
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
	class Product 
	{
		@Override
		public String toString() {
			return "Product [ProductAmount=" + ProductAmount + ", ProductName=" + ProductName + ", ProductDescription="
					+ ProductDescription + ", ProductPrice=" + ProductPrice + "]";
		}
		private int ProductAmount = 1;
		private String ProductName, ProductDescription, ProductSrc;
		private Double ProductPrice;
		
		Button AddBtn, PlusButton,MinusBtn;
		
		public Product(String ProductName,String ProductDescription,String ProductSrc,Double ProductPrice)
		{
			this.ProductName = ProductName;
			this.ProductDescription = ProductDescription;
			this.ProductSrc = ProductSrc;
			this.ProductPrice = ProductPrice;
		}
		public Product(Product Oldproduct)
		{
			this(Oldproduct.getProductName(),Oldproduct.getProductDescription(),Oldproduct.getProductSrc(),Oldproduct.getProductPrice());
			this.setAmount(Oldproduct.getAmount());
		}
		public int getProductAmount() {
			return ProductAmount;
		}
		public String getProductName() {
			return ProductName;
		}
		public String getProductDescription() {
			return ProductDescription;
		}
		public String getProductSrc() {
			return ProductSrc;
		}
		public Double getProductPrice() {
			return ProductPrice;
		}
		public void setAmount(int Amount)
		{
			ProductAmount = Amount;
		}
		public int getAmount()
		{
			return ProductAmount;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(ProductName);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof Product))
				return false;
			Product other = (Product) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(ProductName, other.ProductName);
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
			//VBox.setVgrow(VBoxItemDescrption,Priority.ALWAYS);
			return gridAnchor;
		}
		/**
		 * @return
		 */
		private HBox ProductButtons()
		{
			HBox AddToCartHBox;
			Label AmountDesc = new Label();
			TextField Amount  = new TextField();
			
			AddToCartHBox = new HBox();
			AddToCartHBox.setAlignment(Pos.CENTER);
			AddToCartHBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			AddToCartHBox.setPrefHeight(47.0);
			AddToCartHBox.setPrefWidth(280.0);		
			
			AddBtn = new Button();
			AddBtn.setId("AddButton");
			AddBtn.setEllipsisString("");
			AddBtn.setMnemonicParsing(false);
			AddBtn.setCursor(Cursor.HAND);
			AddBtn.setText("Add");
			HBox.setHgrow(AddBtn, Priority.ALWAYS);
			HBox.setMargin(AddBtn, new Insets(0, 10.0, 0, 0));
			AddBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					myShoppingCart.addItem(new Product(Product.this));
				}
			});			
			
			MinusBtn = new Button();
			MinusBtn.setId("MinusButton");
			MinusBtn.setMaxWidth(34.0);
			MinusBtn.setMinWidth(34.0);
			MinusBtn.setMnemonicParsing(false);
			MinusBtn.setPrefWidth(34.0);
			MinusBtn.setCursor(Cursor.HAND);
			MinusBtn.setText("-");
			HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));
			
			MinusBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
						if(ProductAmount <= 1)
						{
							return;
						}
						ProductAmount=ProductAmount-1;
						Amount.setText(Integer.toString(ProductAmount));
				}
			});

			AmountDesc.setText("יח'");
			HBox.setMargin(AmountDesc, new Insets(5.0, 5.0, 5.0, 5.0));
			
			
			Amount.setAlignment(Pos.CENTER);
			Amount.setPrefHeight(25.0);
			Amount.setPrefWidth(58.0);
			Amount.setText(String.valueOf(ProductAmount));
			HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
			
			PlusButton = new Button();
			PlusButton.setId("PlusButton");
			PlusButton.setMnemonicParsing(false);
			PlusButton.setText("+");
			PlusButton.setCursor(Cursor.HAND);
			PlusButton.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
			HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
			PlusButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
						ProductAmount=ProductAmount+1;
						Amount.setText(Integer.toString(ProductAmount));
				}
			});
			
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
			
			ImageView ItemPicture = CreateImage(ProductSrc,150.0,150.0,true,true, null);
			VBox.setVgrow(ItemPicture, Priority.NEVER);

			Text ItemName = new Text(ProductName);
			ItemName.setId("ProductName");
			VBox.setVgrow(ItemName, Priority.ALWAYS);
			
			Text ItemDescription = new Text(ProductDescription);
			ItemDescription.setId("ProductName");
			
			Text ItemPrice = new Text((ProductPrice + "₪")); 
			ItemPrice.setId("ProductName");
			
			VBoxItemDescrption.getChildren().addAll(ItemPicture,ItemName,ItemDescription,ItemPrice);
			return VBoxItemDescrption;
		}
		
		private CatalogViewController2 getEnclosingInstance() {
			return CatalogViewController2.this;
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

}

