package Entity;

public class Facility 
{
	private Integer FacilityID;
	private String FacilityArea, FacilityLocation,FacilityName;

	private Integer FacilityThresholder;
	private Boolean FacilityEK;
	
	public Facility(Integer FacilityID, String FacilityArea, String FacilityLocation, String FacilityName, Integer FacilityThresholder, Boolean FacilityEK)
	{
		this.FacilityID = FacilityID;
		this.FacilityArea = FacilityArea;
		this.FacilityLocation = FacilityLocation;
		this.FacilityName = FacilityName;
		this.FacilityThresholder = FacilityThresholder;
		this.FacilityEK = FacilityEK;
	}
	public String getFacilityArea() {
		return FacilityArea;
	}
	public void setFacilityArea(String facilityArea) {
		FacilityArea = facilityArea;
	}
	public boolean isFacilityEK() {
		return FacilityEK;
	}
	public void setFacilityEK(boolean facilityEK) {
		FacilityEK = facilityEK;
	}
	public int getFacilityID() {
		return FacilityID;
	}
	public void setFacilityID(int facilityID) {
		FacilityID = facilityID;
	}
	public String getFacilityLocation() {
		return FacilityLocation;
	}
	public void setFacilityLocation(String facilityLocation) {
		FacilityLocation = facilityLocation;
	}
	public String getFacilityName() {
		return FacilityName;
	}
	public void setFacilityName(String facilityName) {
		FacilityName = facilityName;
	}
	public int getFacilityThresholder() {
		return FacilityThresholder;
	}
	public void setFacilityThresholder(int facilityThresholder) {
		FacilityThresholder = facilityThresholder;
	}
	
	public String toString()
	{
		return this.FacilityName;
	}
}
