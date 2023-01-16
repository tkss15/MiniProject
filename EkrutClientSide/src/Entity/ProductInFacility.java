package Entity;

public class ProductInFacility {
	
	int ProductCode;
	int ProductAmount;
	int FacilityID;
	public ProductInFacility(int productCode, int productAmount, int facilityID) {
		ProductCode = productCode;
		ProductAmount = productAmount;
		FacilityID = facilityID;
	}
	public int getProductCode() {
		return ProductCode;
	}
	public void setProductCode(int productCode) {
		ProductCode = productCode;
	}
	public int getProductAmount() {
		return ProductAmount;
	}
	public void setProductAmount(int productAmount) {
		ProductAmount = productAmount;
	}
	public int getFacilityID() {
		return FacilityID;
	}
	public void setFacilityID(int facilityID) {
		FacilityID = facilityID;
	}
	
	
	
}
