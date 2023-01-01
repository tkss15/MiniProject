package Entity;

import Entity.Delivery;
import javafx.scene.control.ComboBox;

public class DeliveryRow extends Delivery {

	private ComboBox<DeliveryStatus> deliveryStatusCombobox;
	
	public DeliveryRow(int orderCode, boolean costumerApproval, DeliveryStatus deliveryStatus,ComboBox<DeliveryStatus> deliveryStatusCombobox) {
		super(orderCode, costumerApproval, deliveryStatus);
		this.deliveryStatusCombobox = deliveryStatusCombobox;
		this.deliveryStatusCombobox.getItems().addAll(DeliveryStatus.valueOf("Dispatched"), DeliveryStatus.valueOf("Received"),
				DeliveryStatus.valueOf("Done")); 
	}
	
	public DeliveryRow(Delivery delivery,ComboBox<DeliveryStatus> deliveryStatusCombobox) {
		super(delivery.getOrderCode(),delivery.getCostumerApproval(),delivery.getDeliveryStatus());
		this.deliveryStatusCombobox = deliveryStatusCombobox;
		this.deliveryStatusCombobox.getItems().addAll(DeliveryStatus.valueOf("Dispatched"), DeliveryStatus.valueOf("Received"),
				DeliveryStatus.valueOf("Done"));
	}

	
	public ComboBox<DeliveryStatus> getDeliveryStatusCombobox() {
		return deliveryStatusCombobox;
	}

	public void setDeliveryStatusCombobox(ComboBox<DeliveryStatus> deliveryStatusCombobox) {
		this.deliveryStatusCombobox = deliveryStatusCombobox;
	}

}
