package gui;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Product;
import client.ClientUI;
import common.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OrderInvoiceController implements Initializable,IController {

		@FXML
		private VBox VboxLayout;
	    @FXML
	    private AnchorPane AnchorPaneAble;
	    @FXML
	    private Button MinusButton;

	    @FXML
	    private Button PlusButton;

	    @FXML
	    private Button MinusButton1;

	    @FXML
	    private Button PlusButton1;

	    @FXML
	    private Button MinusButton11;

	    @FXML
	    private Button PlusButton11;

	    @FXML
	    private Button CloseButton;

	    @FXML
	    private Button CloseButton3;

	    @FXML
	    private Button CloseButton2;
	    
	    
	    @FXML
	    private Button btnCancelInvoice;

	    @FXML
	    private Button btnAcceptInvoice;

	    @FXML
	    private Button btnBack;
	    
	    @FXML
	    private Text textFullPrice;

	    @FXML
	    void ClickAcceptInvoice(ActionEvent event) {

	    }

	    @FXML
	    void ClickBack(ActionEvent event) {
	    	ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);
	    }
	    private void updatePrice()
	    {
			textFullPrice.setText((String.format("%.2f",ClientUI.clientController.getClientOrder().getFinalPrice()) + "₪"));
			btnAcceptInvoice.setText("Accept Invoice for " + textFullPrice.getText());
	    }
	    @FXML
	    void ClickCancelInvoice(ActionEvent event) 
	    {
	    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    	alert.setTitle("Confirmation");
	    	alert.setHeaderText("Are you sure you want to cancel your Invoice?");
	    	alert.setContentText("This action cannot be undone. Please confirm your choice.");
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if (result.get() == ButtonType.OK) 
	    	{
	    		ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);	    	  
	    	} 
	    }

	    @FXML
	    void ClickCloseWindow(ActionEvent event) {

	    }
	    
		@Override
		public void initialize(URL location, ResourceBundle resources) 
		{
			AnchorPane AnchorPaneAble = new AnchorPane();
			AnchorPaneAble.getChildren().clear();
			VboxLayout.getChildren().clear();
			for(Product tempProduct : ClientUI.clientController.getClientOrder().myCart)
			{
				System.out.println(tempProduct);
				ProductUI ProductUITemp = new ProductUI(tempProduct);
				HBox HProductUI = ProductUITemp.CreateProductSection();
				HProductUI.setId(String.valueOf(tempProduct.getProductCode()));
				
				VboxLayout.getChildren().add(HProductUI);
			}
			updatePrice();
		}
	    @FXML
	    void closeWindow(ActionEvent event) {

	    }
	    
	    void RemoveValue(Product product)
	    {
	    	HBox hTemp = null;
	    	for(Node temp : VboxLayout.getChildren())
	    	{
	    		if(!temp.getId().equals(String.valueOf(product.getProductCode())))
	    			continue;
	    		hTemp = (HBox)temp;
	    		break;
	    	}
	    	if(hTemp != null)
	    		VboxLayout.getChildren().remove(hTemp);
	    }
	    class ProductUI {

	    	Button AddBtn, PlusButton, MinusBtn;
			Product Product;
			public ProductUI(Product product) 
			{
				this.Product = product;
			}
	    	
			public HBox CreateProductSection()
			{
				Text textTotalPrice, textPrice;
				
				textTotalPrice = new Text((String.format("%.2f",ClientUI.clientController.getClientOrder().PriceItem(Product)) + "₪"));
				textPrice = new Text((Product.getProductPrice() + "₪"));
				
				HBox ProductSection = new HBox();
				ProductSection.setPadding(new Insets(10));

				// Product Description
				// *****************************************************
				HBox ProductDescrition = new HBox();
				ProductDescrition.setId("BorderBoxSizing");
				ProductDescrition.setPrefWidth(310);
				ProductDescrition.setMinWidth(310);
				ImageView ProductPicture = CreateImage(Product.getPathPicture(), 100.0, 100.0, true, true, null);
				
				VBox ProductDetails = new VBox();
				ProductDetails.setPadding(new Insets(10));
				
				Text ProductName = new Text(Product.getProductName());
				ProductName.setId("ProductName");
				Text ProductDescription = new Text(Product.getProductDescription());
				ProductDescription.setId("ProdcutDescription");
				VBox.setMargin(ProductDescription, new Insets(10,0,0,0));
				
				HBox ButtonsProduct = new HBox();
				
				Label ProductQuntity = new Label("Qunatity");
				ProductQuntity.setId("Quantity");
				HBox.setMargin(ProductQuntity, new Insets(10,0,0,0));
				
				TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
				Amount.setAlignment(Pos.CENTER);
				Amount.setPrefHeight(25);
				Amount.setPrefWidth(58);
				HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
				
				MinusBtn = new Button();
				MinusBtn.setGraphic(CreateImage("Minus.png", 15.0,15.0,true,true,Cursor.HAND));
				MinusBtn.setMnemonicParsing(false);
				MinusBtn.setEllipsisString("");
				MinusBtn.setOnAction(event -> {
					if(Product.getProductAmount() <= 1)
					{
						RemoveValue(Product);
						ClientUI.clientController.getClientOrder().removeItem(Product);
						return;					
					}
					ClientUI.clientController.getClientOrder().UpdateItem(Product, Product.getProductAmount() - 1);
					textTotalPrice.setText((String.format("%.2f",ClientUI.clientController.getClientOrder().PriceItem(Product)) + "₪"));
					textPrice.setText((Product.getProductPrice() + "₪"));
					updatePrice();					
					Amount.setText(Integer.toString(Product.getProductAmount()));
					event.consume();				
				});
				MinusBtn.setId("ChangeButton");
				MinusBtn.setPrefHeight(27);
				MinusBtn.setPrefHeight(27);
				HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));
				
				
				PlusButton = new Button();
				PlusButton.setGraphic(CreateImage("Plus.png", 15.0,15.0,true,true,Cursor.HAND));
				PlusButton.setMnemonicParsing(false);
				PlusButton.setEllipsisString("");
				PlusButton.setOnAction(event -> {
					if(Product.getProductAmount() + 1 > Product.getMaxAmount())
					{
						Alert alert = new Alert(AlertType.ERROR, "Error Currently Avaliable only" + Product.getMaxAmount());
						alert.showAndWait();
						return;
					}
					ClientUI.clientController.getClientOrder().UpdateItem(Product, Product.getProductAmount() + 1);
					textTotalPrice.setText((String.format("%.2f",ClientUI.clientController.getClientOrder().PriceItem(Product)) + "₪"));
					textPrice.setText((Product.getProductPrice() + "₪"));
					updatePrice();
					Amount.setText(Integer.toString(Product.getProductAmount()));
					event.consume();
				});
				PlusButton.setId("ChangeButton");
				HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
				
				ButtonsProduct.getChildren().addAll(ProductQuntity,MinusBtn,Amount,PlusButton);
				ProductDetails.getChildren().addAll(ProductName,ProductDescription,ButtonsProduct);
				ProductDescrition.getChildren().addAll(ProductPicture,ProductDetails);

				// Product Price
				// *****************************************************
				
				HBox ProductPrice = new HBox();
				ProductPrice.setId("BorderBoxSizing");
				ProductPrice.setPrefHeight(100);
				ProductPrice.setPrefWidth(550);
				HBox ProductPricePerUnit =  new HBox();
				
				ProductPrice.setAlignment(Pos.BOTTOM_RIGHT);
				ProductPricePerUnit.setAlignment(Pos.BOTTOM_RIGHT);
				
				HBox.setMargin(ProductPricePerUnit, new Insets(0, 200.0, 0, 0));
				
				Label LabelPricePerUnit = new Label("Price per unit:");
				LabelPricePerUnit.setId("ProductName");
				textPrice.setId("ProductName");
				
				ProductPricePerUnit.getChildren().addAll(LabelPricePerUnit, textPrice);
				
				Label LabelTotalPrice = new Label("Total Price:");
				LabelTotalPrice.setId("ProductName");
				
				textTotalPrice.setId("ProductName");
				ProductPrice.getChildren().addAll(ProductPricePerUnit,LabelTotalPrice,textTotalPrice);
				
				ProductSection.getChildren().addAll(ProductDescrition, ProductPrice);
				return ProductSection;
			}
			public HBox ProductDescription()
			{
				HBox ProductDescrition = new HBox();
				ProductDescrition.setId("BorderBoxSizing");
				ProductDescrition.setPrefWidth(310);
				ProductDescrition.setMinWidth(310);
				ImageView ProductPicture = CreateImage(Product.getPathPicture(), 100.0, 100.0, true, true, null);
				
				VBox ProductDetails = new VBox();
				ProductDetails.setPadding(new Insets(10));
				
				Text ProductName = new Text(Product.getProductName());
				ProductName.setId("ProductName");
				Text ProductDescription = new Text(Product.getProductDescription());
				ProductDescription.setId("ProdcutDescription");
				VBox.setMargin(ProductDescription, new Insets(10,0,0,0));
				
				HBox ButtonsProduct = new HBox();
				
				Label ProductQuntity = new Label("Qunatity");
				ProductQuntity.setId("Quantity");
				HBox.setMargin(ProductQuntity, new Insets(10,0,0,0));
				
				TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
				Amount.setAlignment(Pos.CENTER);
				Amount.setPrefHeight(25);
				Amount.setPrefWidth(58);
				HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
				
				MinusBtn = new Button();
				MinusBtn.setGraphic(CreateImage("Minus.png", 15.0,15.0,true,true,Cursor.HAND));
				MinusBtn.setMnemonicParsing(false);
				MinusBtn.setEllipsisString("");
				MinusBtn.setOnAction(event -> {
					if(Product.getProductAmount() <= 1)
					{
						ClientUI.clientController.getClientOrder().removeItem(Product);
						return;					
					}
					Product.setAmount(Product.getProductAmount() - 1);
					Amount.setText(Integer.toString(Product.getProductAmount()));
					event.consume();				
				});
				MinusBtn.setId("ChangeButton");
				MinusBtn.setPrefHeight(27);
				MinusBtn.setPrefHeight(27);
				HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));
				
				
				PlusButton = new Button();
				PlusButton.setGraphic(CreateImage("Plus.png", 15.0,15.0,true,true,Cursor.HAND));
				PlusButton.setMnemonicParsing(false);
				PlusButton.setEllipsisString("");
				PlusButton.setOnAction(event -> {
					if(Product.getProductAmount() + 1 > Product.getMaxAmount())
					{
						Alert alert = new Alert(AlertType.ERROR, "Error Currently Avaliable only" + Product.getMaxAmount());
						alert.showAndWait();
						return;
					}
					Product.setAmount(Product.getProductAmount() + 1);
					Amount.setText(Integer.toString(Product.getProductAmount()));
					event.consume();
				});
				PlusButton.setId("ChangeButton");
				HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
				
				ButtonsProduct.getChildren().addAll(ProductQuntity,MinusBtn,Amount,PlusButton);
				ProductDetails.getChildren().addAll(ProductName,ProductDescription,ButtonsProduct);
				ProductDescrition.getChildren().addAll(ProductPicture,ProductDetails);
				
				return ProductDescrition;
				
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


}
