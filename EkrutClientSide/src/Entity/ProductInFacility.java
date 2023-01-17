package Entity;

public class ProductInFacility {
	
	int ProductCode;
	int ProductAmount;
	int FacilityID;
	/**
	 * @param - productCode - the unique code of the product
	 * @param - productAmount - the quantity of the product in the facility
	 * @param - facilityID - the id of the facility which the product is in
	 * */
	public ProductInFacility(int productCode, int productAmount, int facilityID) {
		ProductCode = productCode;
		ProductAmount = productAmount;
		FacilityID = facilityID;
	}
	/**
	 * getters and setters for the entity
	 * */
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
