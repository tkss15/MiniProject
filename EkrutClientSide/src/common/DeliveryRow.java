package common;

import Entity.Delivery;
import javafx.scene.control.ComboBox;

public class DeliveryRow extends Delivery {

	private ComboBox<DeliveryStatus> deliveryStatusCombobox;
	
	public DeliveryRow(int orderCode, boolean costumerApproval, DeliveryStatus deliveryStatus,ComboBox<DeliveryStatus> deliveryStatusCombobox) {
		super(orderCode, costumerApproval, deliveryStatus);
		this.deliveryStatusCombobox = deliveryStatusCombobox;
		this.deliveryStatusCombobox.getItems().addAll(DeliveryStatus.valueOf("DISPATCHED"), DeliveryStatus.valueOf("RECEIVED"),
				DeliveryStatus.valueOf("DONE"));
	}

	public ComboBox<DeliveryStatus> getDeliveryStatusCombobox() {
		return deliveryStatusCombobox;
	}

	public void setDeliveryStatusCombobox(ComboBox<DeliveryStatus> deliveryStatusCombobox) {
		this.deliveryStatusCombobox = deliveryStatusCombobox;
	}

}
