package Entity;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class OrderRow {

	private int orderCode;
	private double finalPrice;
	private int facilityID;
	private Button orderDetails;
	private boolean hasDelivery;
	

	private CheckBox orderReceptionAcception;

	public OrderRow(int orderCode, double finalPrice, int facilityID, Button orderDetails) {
		this.orderCode = orderCode;
		this.finalPrice = finalPrice;
		this.facilityID = facilityID;
		this.orderDetails = orderDetails;
		this.orderDetails.setText("Show Details");
	}
	


	public boolean isHasDelivery() {
		return hasDelivery;
	}


	public void setHasDelivery(boolean hasDelivery) {
		this.hasDelivery = hasDelivery;
	}



	public CheckBox getOrderReceptionAcception() {
		return orderReceptionAcception;
	}


	public void setOrderReceptionAcception(CheckBox orderReceptionAcception) {
		this.orderReceptionAcception = orderReceptionAcception;
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}


	public int getFacilityID() {
		return facilityID;
	}

	public void setFacilityID(int facilityID) {
		this.facilityID = facilityID;
	}

	public Button getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Button orderDetails) {
		this.orderDetails = orderDetails;
	}

}
