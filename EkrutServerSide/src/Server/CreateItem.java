package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import common.MessageHandler;
import database.DBConnect;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateItem extends Application {

	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		int ItemsA=0;
		VBox Rows = new VBox();
		HBox Items = new HBox();
		Connection conn = connectToDB();
        try 
        {          
        	Statement stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM item;"));
    		
			while(rs.next())
			{
				VBox VBoxChips = CreateNewItem(rs.getString(2),rs.getString(3), rs.getDouble(4), rs.getString(5));
				Items.getChildren().add(VBoxChips);
				if(ItemsA % 2 == 1)
				{
					HBox.setMargin(VBoxChips, new Insets(0, 0, 0, 30.0));
					Rows.getChildren().add(Items);
					Items = new HBox();
					ItemsA = 0;
				}
				else ItemsA++;
			}	
			rs.close();
			
        	System.out.println("SQL connection succeeded");
	    } catch (Exception ex) {
	    	System.out.println("Failed to Query");
	    }
        
//		VBox VBoxChips = CreateNewItem("Cruch Chips Original","Classic Potato Chips flavor", 12.90, "chips.png");
//		VBox VCola = CreateNewItem("Coke Cola Zero","350 mill coke zero glass", 7.5, "cola-removebg-preview.png");
//		VBox VCola2 = CreateNewItem("Coke Cola Zero","350 mill coke zero glass", 7.5, "cola-removebg-preview.png");
//		HBox Items = new HBox();
		//HBox.setMargin(VCola, new Insets(0, 0, 0, 30.0));
		//Items.getChildren().addAll(VBoxChips,VCola,VCola2);
		Scene scene = new Scene(Rows);
		scene.getStylesheets().add("/gui/CatalogViewerNew.css");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	public Connection connectToDB()
	{
		Connection conn = null;
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeeded");
        } catch (Exception ex) {
        	System.out.println("Driver definition failed");
        }
        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ekrutdatabase?serverTimezone=IST","root","nset1234!@#$");

            System.out.println("SQL connection succeeded");
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
        	System.out.println("SQLException: " + ex.getMessage());
        	System.out.println("SQLState: " + ex.getSQLState());
        	System.out.println("VendorError: " + ex.getErrorCode());
        } 
        return conn;
   	}
	
	private VBox CreateItemDescription(String strName, String strDesc, Double doubPrice, String strItemPic)
	{
		VBox VBoxItemDescrption = new VBox();
		VBoxItemDescrption.setMaxHeight(Double.MAX_VALUE);
		VBoxItemDescrption.setMaxWidth(Double.MAX_VALUE);
		VBoxItemDescrption.setAlignment(Pos.CENTER);
		VBoxItemDescrption.setMinHeight(Double.NEGATIVE_INFINITY);
		VBoxItemDescrption.setMinWidth(Double.NEGATIVE_INFINITY);
		VBoxItemDescrption.setPrefWidth(198.0);
		VBox.setVgrow(VBoxItemDescrption,Priority.ALWAYS);

		ImageView ItemPicture = new ImageView();
		ItemPicture.setFitHeight(150);
		ItemPicture.setFitWidth(150);
		ItemPicture.setPickOnBounds(true);
		ItemPicture.setPreserveRatio(true);
		VBox.setVgrow(ItemPicture, Priority.NEVER);
		
		Image image = new Image(strItemPic);
		ItemPicture.setImage(image);

		Text ItemName = CreateText(strName, 14.0);
		VBox.setVgrow(ItemName, Priority.ALWAYS);
		Text ItemDescription = CreateText(strDesc, 14.0);
		Text ItemPrice = CreateText((doubPrice + "₪"), 18.0);
		
		VBoxItemDescrption.getChildren().addAll(ItemPicture,ItemName,ItemDescription,ItemPrice);
		return VBoxItemDescrption;
	}
	private VBox CreateNewItem(String strName, String strDesc, Double doubPrice, String strItemPic)
	{
		VBox gridAnchor = new VBox();
		gridAnchor.setId("gridAnchor");
		gridAnchor.setPrefHeight(300.0);
		gridAnchor.setPrefWidth(300.0);
		
		
		VBox VBoxItemDescrption = CreateItemDescription(strName,strDesc, doubPrice, strItemPic);
		HBox AddToCartHBox = CreateButtons();
		gridAnchor.getChildren().addAll(VBoxItemDescrption, AddToCartHBox);
		return gridAnchor;
	}
	private HBox CreateButtons()
	{
		HBox AddToCartHBox = new HBox();
		AddToCartHBox.setId("gridAnchor");
		AddToCartHBox.setAlignment(Pos.CENTER);
		AddToCartHBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		AddToCartHBox.setPrefHeight(47.0);
		AddToCartHBox.setPrefWidth(280.0);		
		
		Button AddBtn = new Button();
		AddBtn.setId("AddButton");
		AddBtn.setEllipsisString("");
		AddBtn.setMnemonicParsing(false);
		AddBtn.setText("Add");
		HBox.setHgrow(AddBtn, Priority.ALWAYS);
		HBox.setMargin(AddBtn, new Insets(0, 10.0, 0, 0));
		
		Button MinusBtn = new Button();
		MinusBtn.setId("MinusButton");
		MinusBtn.setMaxWidth(34.0);
		MinusBtn.setMinWidth(34.0);
		MinusBtn.setMnemonicParsing(false);
		MinusBtn.setPrefWidth(34.0);
		MinusBtn.setText("-");
		HBox.setMargin(MinusBtn, new Insets(5.0, 5.0, 5.0, 5.0));

		Label AmountDesc = new Label();
		AmountDesc.setText("יח'");
		HBox.setMargin(AmountDesc, new Insets(5.0, 5.0, 5.0, 5.0));
		
		
		TextField Amount  = new TextField();
		Amount.setAlignment(Pos.CENTER);
		Amount.setPrefHeight(25.0);
		Amount.setPrefWidth(58.0);
		Amount.setText("1");
		HBox.setMargin(Amount, new Insets(5.0, 5.0, 5.0, 5.0));
		
		Button PlusButton = new Button();
		PlusButton.setId("PlusButton");
		PlusButton.setMnemonicParsing(false);
		PlusButton.setText("+");
		PlusButton.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
		HBox.setMargin(PlusButton, new Insets(5.0, 5.0, 5.0, 5.0));
		
		AddToCartHBox.getChildren().addAll(AddBtn, MinusBtn, AmountDesc, Amount, PlusButton);
		HBox.setMargin(AddToCartHBox, new Insets(0, 5.0, 5.0, 5.0));
		return AddToCartHBox;
	}
	private Text CreateText(String Name, Double fontsize)
	{
		Text ItemName = new Text();
		ItemName.setText(Name);
		ItemName.setFont(new Font(fontsize));
		return ItemName;
	}
}
//
//Button AddButton = new Button();
//AddButton.setId("AddButton");
//AddButton.setEllipsisString("");
//AddButton.setMnemonicParsing(false);
//AddButton.setText("Add");
//AddButton.setHgrow(Priority.ALWAYS);
//
//Insets insets1 = new Insets(0, 10.0, 0, 0);
//AddButton.setMargin(insets1);
//
//Button MinusButton = new Button();
//
//<VBox id="gridAnchor" fx:id="Item" prefHeight="300.0" prefWidth="300.0">
//<children>
//   <VBox alignment="CENTER" maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308" minHeight="-Infinity" minWidth="-Infinity" prefWidth="198.0" VBox.vgrow="ALWAYS">
//      <children>
//         <ImageView fitHeight="150.0" fitWidth="150.0" pickOnBounds="true" preserveRatio="true" VBox.vgrow="NEVER">
//            <image>
//               <Image url="@pictures/chips.png" />
//            </image>
//            <VBox.margin>
//               <Insets />
//            </VBox.margin>
//            <viewport>
//               <Rectangle2D />
//            </viewport>
//         </ImageView>
//         <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Crunch Chips Original" VBox.vgrow="ALWAYS">
//            <font>
//               <Font size="14.0" />
//            </font>
//         </Text>
//         <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Classic Potato Chips flavor">
//            <font>
//               <Font size="14.0" />
//            </font>
//         </Text>
//         <Text strokeType="OUTSIDE" strokeWidth="0.0" text="12.90₪">
//            <font>
//               <Font size="18.0" />
//            </font>
//         </Text>
//      </children>
//   </VBox>
//   <HBox fx:id="AddToCartHBox" alignment="CENTER" nodeOrientation="LEFT_TO_RIGHT" prefHeight="47.0" prefWidth="280.0">
//      <children>
//         <Button fx:id="AddButton" ellipsisString="" mnemonicParsing="false" text="Add" HBox.hgrow="ALWAYS">
//            <HBox.margin>
//               <Insets right="10.0" />
//            </HBox.margin>
//         </Button>
//         <Button fx:id="MinusButton" maxWidth="34.0" minWidth="34.0" mnemonicParsing="false" prefWidth="34.0" text="-">
//            <HBox.margin>
//               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
//            </HBox.margin>
//         </Button>
//         <Label text="יח'">
//            <HBox.margin>
//               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
//            </HBox.margin>
//         </Label>
//         <TextField alignment="CENTER" prefHeight="25.0" prefWidth="58.0" text="1">
//            <HBox.margin>
//               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
//            </HBox.margin>
//         </TextField>
//         <Button fx:id="PlusButton" mnemonicParsing="false" text="+" textOverrun="CENTER_ELLIPSIS">
//            <HBox.margin>
//               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
//            </HBox.margin>
//         </Button>
//      </children>
//      <VBox.margin>
//         <Insets right="5.0" />
//      </VBox.margin>
//   </HBox>
//</children>
//<padding>
//   <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
//</padding>
//</VBox>