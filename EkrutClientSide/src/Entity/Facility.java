package Entity;


/**
 * @author galmu
 * The Facility class represents a facility object and contains information about the facility
 * such as its ID, area, location, name, threshold value, and whether it is in use or not.
 * 
 * @field FacilityID - an Integer representing the unique ID of the facility
 * @field FacilityArea - a string representing the area of the facility
 * @field FacilityLocation - a string representing the location of the facility
 * @field FacilityName - a string representing the name of the facility
 * @field FacilityThresholder - an Integer representing the threshold value of the facility
 * @field FacilityEK - a Boolean representing whether the facility is in use.
 * 
 * @constructor Facility(Integer FacilityID, String FacilityArea, String FacilityLocation, String FacilityName, Integer FacilityThresholder, Boolean FacilityEK) - Constructor for creating a new Facility object.
 * 
 * @method getFacilityArea() - returns the area of the facility as a string
 * @method setFacilityArea(String facilityArea) - sets the area of the facility
 * @method isFacilityEK() - returns a boolean value indicating whether the facility is in use or not
 * @method setFacilityEK(boolean facilityEK) - sets the in-use status of the facility
 * @method getFacilityID() - returns the ID of the facility as an integer
 * @method setFacilityID(int facilityID) - sets the ID of the facility
 * @method getFacilityLocation() - returns the location of the facility as a string
 * @method setFacilityLocation(String facilityLocation) - sets the location of the facility
 * @method getFacilityName() - returns the name of the facility as a string
 * @method setFacilityName(String facilityName) - sets the name of the facility
 * @method getFacilityThresholder() - returns the threshold value of the facility as an integer
 * @method setFacilityThresholder(int facilityThresholder) - sets the threshold value of the facility
 * @method toString() - returns the name of the facility as a string
 */
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
