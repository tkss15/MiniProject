package Entity;

public class DeliveryOrder {
	private enum deliveryStatusENUM{
		RECEIVED,
		DESPATCHED,
		DONE
	};
	private enum customerApprovalStatusENUM{
		APPROVED,
		PENDING,
		NOTAPROVED
	};
	
	
	private int orderCode;
	private deliveryStatusENUM deliveryStatus;
	private customerApprovalStatusENUM customerApprovalStatus;
	
	public DeliveryOrder(int orderCode,deliveryStatusENUM deliveryStatus
			,customerApprovalStatusENUM customerApprovalStatus){
		this.orderCode = orderCode;
		this.deliveryStatus = deliveryStatus;
		this.customerApprovalStatus = customerApprovalStatus;
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public deliveryStatusENUM getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(deliveryStatusENUM deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public customerApprovalStatusENUM getCustomerApprovalStatus() {
		return customerApprovalStatus;
	}

	public void setCustomerApprovalStatus(customerApprovalStatusENUM customerApprovalStatus) {
		this.customerApprovalStatus = customerApprovalStatus;
	}
	
}
