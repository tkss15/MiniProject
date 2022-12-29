package Entity;

public class ImportedUser extends User{

	private String area,roleType;

	public ImportedUser(User user,String area, String roleType) {
		super(user.getFirstName(),user.getLastName(),user.getPhone()
				,user.getEmail(),user.getID(),user.getUserName(),user.getPassword());
		this.area = area;
		this.roleType= roleType;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
