package Entity;

public class User
{
	private String ID,UserName,FirstName,LastName,Password,Phone,Email,OnlineStatus;
	private String area;

	public User(String userName, String password)
	{
		UserName = userName;
		Password = password;
	}
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
