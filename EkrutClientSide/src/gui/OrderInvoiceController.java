package gui;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
				VboxLayout.getChildren().add(ProductUITemp.ProductDescription());
			}
		}
	    @FXML
	    void closeWindow(ActionEvent event) {

	    }
	    class ProductUI {

			Product Product;
			Button AddBtn, PlusButton, MinusBtn;
			
			public ProductUI(Product product) 
			{
				this.Product = product;
			}
	    	
//			public HBox CreateProductSection()
//			{
//				HBox ProductSection = new HBox();
//				HBox ProductDescrition = ProductDescription();
//				return ProductSection;
//			}
			
			public HBox ProductDescription()
			{
				HBox ProductDescrition = new HBox();
				ImageView ProductPicture = CreateImage(Product.getPathPicture(), 100.0, 100.0, true, true, null);
				
				VBox ProductDetails = new VBox();
				
				Text ProductName = new Text(Product.getProductName());
				Text ProductDescription = new Text(Product.getProductDescription());
				
				HBox ButtonsProduct = new HBox();
				
				Label ProductQuntity = new Label("Qunatity");
				
				MinusBtn = new Button("-");
				MinusBtn.setMnemonicParsing(false);
				MinusBtn.setOnAction(event -> {});
				HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));
				
				TextField Amount  = new TextField(String.valueOf(Product.getProductAmount()));
				Amount.setAlignment(Pos.CENTER);
				
				PlusButton = new Button("+");
				PlusButton.setMnemonicParsing(false);
				PlusButton.setOnAction(event -> {});
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
