package Entity;

public class ImportedUser extends User{

	private String roleType;
	private boolean isSentToManager;

	public ImportedUser(User user,String area, String roleType,boolean isSentToManager) {
		super(user.getFirstName(),user.getLastName(),user.getPhone()
				,user.getEmail(),user.getID(),user.getUserName(),user.getPassword(),user.getArea());

		this.roleType= roleType;
		this.isSentToManager = isSentToManager;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public boolean isSentToManager() {
		return isSentToManager;
	}

	public void setSentToManager(boolean isSentToManager) {
		this.isSentToManager = isSentToManager;
	}

}