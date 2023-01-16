package Entity;

/**
 * The User class represents a user with various attributes such as ID, username, first name, last name, password, phone, email, and online status.
 * It has two constructors, one that takes in a username and password and another that takes in all user attributes.
 */
public class User
{
	private String ID,UserName,FirstName,LastName,Password,Phone,Email,OnlineStatus;
	private String area;

	/**
	 * @param userName the username of the user
	 * @param password the password of the user
	 */
	public User(String userName, String password)
	{
		UserName = userName;
		Password = password;
	}
	/**
	*

	@param firstName the first name of the user
	@param lastName the last name of the user
	@param phone the phone number of the user
	@param email the email of the user
	@param ID the ID of the user
	@param UserName the username of the user
	@param Password the password of the user
	@param area the area of the user
	*/
	public User(String firstName, String lastName, String phone, String email, String ID, String UserName,
			String Password, String area) 
	{
		this.ID = ID;
		this.UserName = UserName;
		FirstName = firstName;
		LastName = lastName;
		this.Password = Password;
		Phone = phone;
		Email = email;
		this.area = area;
	}
	
	public User() {} //Empty Constructor
	
	/*standard getters,setters and toString.*/
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getOnlineStatus() {
		return OnlineStatus;
	}
	public void setOnlineStatus(String onlineStatus) {
		OnlineStatus = onlineStatus;
	}
	@Override
	public String toString() {
		return "User [ID=" + ID + ", UserName=" + UserName + ", FirstName=" + FirstName + ", LastName=" + LastName
				+ ", Password=" + Password + ", Phone=" + Phone + ", Email=" + Email + ", OnlineStatus=" + OnlineStatus
				+ "]";
	}
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
