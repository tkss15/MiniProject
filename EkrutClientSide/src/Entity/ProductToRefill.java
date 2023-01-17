package Entity;

public class ProductToRefill {
	private String productName;
	private String productQuantity;
	private String area;
	private String facilityLocation;
	private String facilityName;
	private String facilityThresholdLevel;
	/**
	 * @param: productName- the name of the product
	 * @param: productQuantity- the quantity of the product
	 * @param: area - the area the product is in
	 * @param: facilityLocation- the location of the facility
	 * @param:facilityName - the name of the facility
	 * @param: facilityThresholdLevel - the thresholdlevel of the current facility
	 * */
	public ProductToRefill(String productName, String productQuantity, String area, String facilityLocation,
			String facilityName, String facilityThresholdLevel) {
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.area = area;
		this.facilityLocation = facilityLocation;
		this.facilityName = facilityName;
		this.facilityThresholdLevel = facilityThresholdLevel;
	}
	
	/**
	 * getters and setters for this entity
	 * */
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFacilityLocation() {
		return facilityLocation;
	}
	public void setFacilityLocation(String facilityLocation) {
		this.facilityLocation = facilityLocation;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getFacilityThresholdLevel() {
		return facilityThresholdLevel;
	}
	public void setFacilityThresholdLevel(String facilityThresholdLevel) {
		this.facilityThresholdLevel = facilityThresholdLevel;
	}
	/**
	 * when this method is being called, this string is returned
	 * */
	@Override
	public String toString() {
		return "ProductToRefill [productName=" + productName + ", productQuantity=" + productQuantity + ", area=" + area
				+ ", facilityLocation=" + facilityLocation + ", facilityName=" + facilityName
				+ ", facilityThresholdLevel=" + facilityThresholdLevel + "]";
	}
	
	
}
