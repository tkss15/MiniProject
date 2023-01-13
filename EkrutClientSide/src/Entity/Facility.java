package Entity;

public class Facility 
{
	private Integer FacilityID;
	private String FacilityArea, FacilityLocation,FacilityName;

	private Integer FacilityThresholder;
	private Boolean FacilityEK;
	
	
	/**
	 * Constructor for creating a new Facility object.
	 * @param FacilityID- an Integer representing the unique ID of the facility
	 * @param FacilityArea- a string representing the area of the facility
	 * @param FacilityLocation- a string representing the location of the facility
	 * @param FacilityName- a string representing the name of the facility
	 * @param FacilityThresholder- an Integer representing the threshold value of the facility
	 * @param FacilityEK- a Boolean representing whether the facility has emergency kit or not
	 * */
	public Facility(Integer FacilityID, String FacilityArea, String FacilityLocation, String FacilityName, Integer FacilityThresholder, Boolean FacilityEK)
	{
		this.FacilityID = FacilityID;
		this.FacilityArea = FacilityArea;
		this.FacilityLocation = FacilityLocation;
		this.FacilityName = FacilityName;
		this.FacilityThresholder = FacilityThresholder;
		this.FacilityEK = FacilityEK;
	}
	
	/**
	 * getters and setters for the facility entity*/
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
