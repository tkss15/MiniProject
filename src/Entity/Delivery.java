package Entity;

public class Delivery {

	public enum DeliveryStatus {
		Dispatched("Dispatched"), Received("Received"), Done("Done");

		private String status;

		DeliveryStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return status;
		}
	};

	private int orderCode;
	private DeliveryStatus deliveryStatus; 
	private boolean costumerApproval;

	public Delivery() {
	};

	public Delivery(int orderCode, boolean costumerApproval, DeliveryStatus deliveryStatus) {
		this.orderCode = orderCode;
		this.deliveryStatus = deliveryStatus;
		this.costumerApproval = costumerApproval;
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public boolean getCostumerApproval() {
		return costumerApproval;
	}

	public void setCostumerApproval(int costumerApproval) {
		if(costumerApproval == 1) this.costumerApproval = true;
		else this.costumerApproval = false;
	}

	@Override
	public String toString() {
		return "Delivery [orderCode=" + orderCode + ", deliveryStatus=" + deliveryStatus + ", costumerApproval="
				+ costumerApproval + "]";
	}

}
