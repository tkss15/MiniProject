package Entity;

public class Facility 
{
	private int FacilityID;
	private String FacilityLocation,FacilityName;
	private int FacilityThresholder;
	
	private String FacilityArea;
	private boolean FacilityEK;
	
	
	public Facility(int FacilityID, String FacilityArea, String FacilityLocation, String FacilityName, int FacilityThresholder,boolean FacilityEK)
	{
		this.FacilityID = FacilityID;
		this.FacilityLocation = FacilityLocation;
		this.FacilityName = FacilityName;
		this.FacilityEK =FacilityEK;
		this.FacilityArea = FacilityArea;
		this.FacilityThresholder = FacilityThresholder;
	}
	public int getFacilityID() {
		return FacilityID;
	}
	
	public boolean getFacilityEK() {
		return FacilityEK;
	}
	public void setFacilityEK(boolean facilityEK) {
		FacilityEK = facilityEK;
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
	
	public String getFacilityArea() {
		return FacilityArea;
	}
	public void setFacilityArea(String facilityArea) {
		FacilityArea = facilityArea;
	}

	
	public void setFacilityThresholder(int facilityThresholder) {
		FacilityThresholder = facilityThresholder;
	}
	
	public String toString()
	{
		return this.FacilityName;
	}
}
