package Entity;

/***
 * 
 * @author galmu
 * The Delivery class represents a delivery object that contains information about an order and its delivery status.
 * It contains an enumeration called DeliveryStatus that contains the possible statuses of a delivery: SentToProvider, Dispatched and Done.
 * The class has three private attributes: orderCode, deliveryStatus and costumerApproval.
 * The Delivery class has a default constructor and a constructor that takes three parameters: orderCode, costumerApproval and deliveryStatus.
 * There are also getters and setters for each attribute and a toString() method to return a string representation of the Delivery object.
 *
 */
public class Delivery {

	public enum DeliveryStatus {
		SentToProvider("SentToProvider"),Dispatched("Dispatched"), Done("Done");

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
