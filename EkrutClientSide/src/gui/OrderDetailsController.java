package gui;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Entity.Product;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class OrderDetailsController implements Initializable, IController {

	ArrayList<Product> RequestedProducts = new ArrayList<>();
	Integer orderCode;
    @FXML
    private Button CloseButton;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    private Button btnCancelInvoice, btnAcceptInvoice, btnPayLater;

    @FXML
    private Text OrderDate, OrderType, OrderTotalPrice;

    @FXML
    private VBox DeliveryOption;
    
    @FXML
    private TableView<ProudctTable> ProductsTable;

    @FXML
    private TableColumn<ProudctTable, String> ProductName;

    @FXML
    private TableColumn<ProudctTable, Integer> ProductAmount;

    @FXML
    private TableColumn<ProudctTable, String> ProductPrice;

    @FXML
    private Button btnBack;
    
    @FXML
    void closeWindow(ActionEvent event) {
    	ClientUI.clientController.UserDissconnected();
    	System.exit(0);
    }
    @FXML
    void PaynowButton(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/ClientPayment.fxml");
//    	String sql = "SELECT products.*, productsinfacility.ProductAmount FROM products LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode WHERE productsinfacility.FacilityID = " + ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() + " ORDER BY products.ProductCode";
//    	RequestObjectClient request = new RequestObjectClient("#REQUEST_AVAILABLE_PRODUCTS",sql,"*");  
//    	ClientUI.clientController.accept(request);
    }
	
	@FXML 
	void PayLaterAction(ActionEvent event)
	{
		Task<Void> task = new Task<Void>() {
			  @Override
			  public Void call() throws Exception {
				  	int myFacility = ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID();
					RequestObjectClient request;

			    	for(Product myProduct : ClientUI.clientController.getClientOrder().myCart)
			    	{
			    		for(Product facilityProduct : ClientUI.clientController.getArrProducts())
			    		{
			    			if(myProduct.equals(facilityProduct))
			    			{
			    				if(myProduct.getProductAmount() > facilityProduct.getMaxAmount())
			    				{
			    					System.out.println("Well you got fucked no enough for ya");
			    					return null;
			    				}
			    				int CurrentAmount = facilityProduct.getMaxAmount() - myProduct.getProductAmount();
			    				request = new RequestObjectClient("#REMOVE_ITEMS_FACILITY",String.format("table=productsinfacility#condition=FacilityID=%d&ProductCode=%d#values=ProductAmount=%d", myFacility, myProduct.getProductCode(),CurrentAmount),"PUT");  
			    				ClientUI.clientController.accept(request);
			    			}
			    		}
			    	}
//			    	
			    	request = new RequestObjectClient("#CREATE_NEW_ORDER",String.format("table=orders#values=isInvoiceConfirmed=1&FacilityID=%d&userName=%s", ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID(), ClientUI.clientController.getUser().getUserName()),"POST");  
			    	ClientUI.clientController.accept(request);
			    	
			    	request = new RequestObjectClient("#GET_ORDER_NUMBER",String.format("SELECT orders.orderCode FROM orders WHERE userName = \"%s\" AND orderCode = (SELECT MAX(orderCode) FROM orders);", ClientUI.clientController.getUser().getUserName()),"*");  
			    	ClientUI.clientController.accept(request);
//			    	

			    	for(Product myProduct : ClientUI.clientController.getClientOrder().myCart)
			    	{
			        	request = new RequestObjectClient("#ADD_ITEMS_TO_ORDER",String.format("table=productsinorder#values=orderCode=%d&ProductCode=%d&FacilityID=%d&ProductAmount=%d", orderCode, myProduct.getProductCode(), myFacility, myProduct.getProductAmount()),"POST");  
			        	ClientUI.clientController.accept(request);
			    	}
			    return null;
			  }
		};
		ClientUI.sceneManager.ShowPopup("../views/LoadingScreen.fxml");
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
            	ClientUI.sceneManager.SceneBack(event, "../views/LoadingScreen.fxml");
            	ClientUI.sceneManager.SceneBack(event, "../views/OrderDetails.fxml");
            	Platform.runLater(() -> ClientUI.sceneManager.ShowScene("../views/Homepage.fxml"));
                System.out.println("Done");
            }
        });
		new Thread(task).start();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientUI.clientController.setController(this);
		ProductName.setCellValueFactory(new PropertyValueFactory<ProudctTable, String>("ProductName"));
		ProductAmount.setCellValueFactory(new PropertyValueFactory<ProudctTable, Integer>("ProductAmount"));
		ProductPrice.setCellValueFactory(new PropertyValueFactory<ProudctTable, String>("ProductPrice"));
		
		Integer TotalAmount = 0;
		ObservableList<ProudctTable> colums = FXCollections.observableArrayList();
		for(Product tempProduct : ClientUI.clientController.getClientOrder().myCart)
		{
			TotalAmount += tempProduct.getProductAmount();
			ProudctTable tempRow = new ProudctTable(tempProduct.getProductName(), tempProduct.getProductAmount(), null);
			tempRow.setProductPrice(ClientUI.clientController.getClientOrder().PriceItem(tempProduct));
			colums.add(tempRow);
			ProductsTable.getItems().add(tempRow);
		}
		if(TotalAmount > 0)
		{
			ProudctTable tempRow = new ProudctTable("Total Products", TotalAmount, null);
			tempRow.setProductPrice( ClientUI.clientController.getClientOrder().getFinalPrice());			
			ProductsTable.getItems().add(tempRow);
		}
		//ProductsTable.setItems(colums);
		ProductsTable.refresh();
		// TODO Auto-generated method stub
		DeliveryOption.setVisible(false);
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
		OrderDate.setText(timeStamp);
		OrderType.setText(ClientUI.clientController.getClientOrder().getOrderType());
		OrderTotalPrice.setText((String.format("%.2f",ClientUI.clientController.getClientOrder().getFinalPrice()) + "₪"));
		
		if(ClientUI.clientController.getClientOrder().getOrderType().equals("Delivery"))
		{
			DeliveryOption.setVisible(true);
		}
	}
	@Override
	public void updatedata(Object data) {
		System.out.println("Currently in OrderDeatilsUpdate");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#REQUEST_AVAILABLE_PRODUCTS":
				{
					//ClientUI.clientController.getArrProducts().clear();
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);//Row 1 
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductSrc = (String) values[4];
						Integer ProductAmount = (Integer) values[6];
						
						Product anotherProduct = new Product(ProductCode,ProductName,ProductDesc, ProductSrc, ProductPrice, ProductAmount);
						if(ClientUI.clientController.getArrProducts().contains(anotherProduct))
						{
							int IndexOf = ClientUI.clientController.getArrProducts().indexOf(anotherProduct);
							ClientUI.clientController.getArrProducts().set(IndexOf, anotherProduct);
						}
					}
					break;
				}
				case "#GET_ORDER_NUMBER":
				{
					System.out.println("Hello");
					Object[] values =(Object[]) serverResponse.Responsedata.get(0);
					orderCode = (Integer) values[0];
					System.out.println(orderCode);
					break;
				}
			}
		}
	}
	public class ProudctTable
	{
		private String ProductName;
		private Integer ProductAmount;
		private String ProductPrice;
		public ProudctTable(String ProductName, Integer ProductAmount, String ProductPrice) 
		{
			this.ProductName = ProductName;
			this.ProductAmount = ProductAmount;
			this.ProductPrice = ProductPrice;
			// TODO Auto-generated constructor stub
		}
		public String getProductName() {
			return ProductName;
		}
		public void setProductName(String productName) {
			ProductName = productName;
		}
		public Integer getProductAmount() {
			return ProductAmount;
		}
		public void setProductAmount(Integer productAmount) {
			ProductAmount = productAmount;
		}
		public String getProductPrice() {
			return ProductPrice;
		}
		public void setProductPrice(Double productPrice) {
			ProductPrice = (String.format("%.2f",productPrice) + "₪");
		}
		
	}

}