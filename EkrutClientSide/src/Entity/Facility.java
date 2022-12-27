package Entity;

public class Facility 
{
	private int FacilityID;
	private String FacilityLocation,FacilityName;
	private int FacilityThresholder;
	
	public Facility(int FacilityID, String FacilityLocation, String FacilityName, int FacilityThresholder)
	{
		this.FacilityID = FacilityID;
		this.FacilityLocation = FacilityLocation;
		this.FacilityName = FacilityName;
		this.FacilityID = FacilityThresholder;
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
}
