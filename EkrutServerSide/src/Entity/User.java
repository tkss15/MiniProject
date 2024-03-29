package Entity;

public class User 
{
	private String ID,UserName,FirstName,LastName,Password,Phone,Email,OnlineStatus;
	

	public User(String iD, String userName, String firstName, String lastName, String password, String phone,
			String email, String onlineStatus) 
	{
		super();
		ID = iD;
		UserName = userName;
		FirstName = firstName;
		LastName = lastName;
		Password = password;
		Phone = phone;
		Email = email;
		OnlineStatus = onlineStatus;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return UserName;
	}

	public void setName(String name) {
		UserName = name;
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
	
}
