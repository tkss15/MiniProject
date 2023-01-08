package Entity;

public class ProductToRefill {
	private String productName;
	private String productQuantity;
	private String area;
	private String facilityLocation;
	private String facilityName;
	private String facilityThresholdLevel;
	public ProductToRefill(String productName, String productQuantity, String area, String facilityLocation,
			String facilityName, String facilityThresholdLevel) {
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.area = area;
		this.facilityLocation = facilityLocation;
		this.facilityName = facilityName;
		this.facilityThresholdLevel = facilityThresholdLevel;
	}
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
	@Override
	public String toString() {
		return "ProductToRefill [productName=" + productName + ", productQuantity=" + productQuantity + ", area=" + area
				+ ", facilityLocation=" + facilityLocation + ", facilityName=" + facilityName
				+ ", facilityThresholdLevel=" + facilityThresholdLevel + "]";
	}
	
	
}
